package com.afunproject.packtweaks;

import java.util.Optional;

import com.afunproject.packtweaks.capability.CapabilitiesRegister;
import com.afunproject.packtweaks.capability.IFollowQuest;
import com.mojang.datafixers.util.Pair;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.WrappedGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.levelgen.feature.ConfiguredStructureFeature;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.smileycorp.followme.common.FollowHandler;
import net.smileycorp.followme.common.FollowMe;
import net.smileycorp.followme.common.ai.FollowUserGoal;
import net.smileycorp.followme.common.capability.IFollower;

public class EventListener {

	@SubscribeEvent
	public void entityTick(LivingUpdateEvent event) {
		if (event.getEntity() instanceof Mob) {
			Mob entity  = (Mob) event.getEntity();
			if (entity.level instanceof ServerLevel) {
				LazyOptional<IFollowQuest> questOptional = entity.getCapability(CapabilitiesRegister.FOLLOW_QUEST_CAPABILITY);
				if (questOptional.isPresent()) {
					IFollowQuest questCap = questOptional.resolve().get();
					if (questCap.hasStructure()) {
						LazyOptional<IFollower> followOptional = entity.getCapability(FollowMe.FOLLOW_CAPABILITY);
						if (followOptional.isPresent()) {
							IFollower followCap = followOptional.resolve().get();
							LivingEntity followedEntity = followCap.getFollowedEntity();
							if (followedEntity instanceof Player) {
								if (isInStructure(entity.blockPosition(), (ServerLevel)entity.level, questCap.getStructure())) {
									//remove follow ai
									for (WrappedGoal entry : entity.goalSelector.getRunningGoals().toArray(WrappedGoal[]::new)) {
										if (entry.getGoal() instanceof FollowUserGoal) {
											FollowUserGoal task = (FollowUserGoal) entry.getGoal();
											if (task.getUser() == followedEntity) {
												FollowHandler.removeAI(task);
											}
											break;
										}
									};
									//remove capabilities
									followCap.setForcedToFollow(false);
									questCap.setStructure(null);
									//give quest code goes here
								}
							}
						}
					}
				}
			}
		}
	}

	private static boolean isInStructure(BlockPos pos, ServerLevel level, ResourceLocation loc) {
		Registry<ConfiguredStructureFeature<?, ?>> registry = level.registryAccess().registryOrThrow(Registry.CONFIGURED_STRUCTURE_FEATURE_REGISTRY);
		TagKey<ConfiguredStructureFeature<?, ?>> village = TagKey.create(Registry.CONFIGURED_STRUCTURE_FEATURE_REGISTRY, loc);
		Optional<HolderSet.Named<ConfiguredStructureFeature<?, ?>>> villageOptional = registry.getTag(village);
		if (villageOptional.isPresent()) {
			Pair<BlockPos, Holder<ConfiguredStructureFeature<?, ?>>> pair = level.getChunkSource().getGenerator().findNearestMapFeature(level, villageOptional.get(), pos, 1, false);
			if (pair == null) return false;
			BlockPos villagePos = pair.getFirst();
			return Math.pow(villagePos.getX()-pos.getX(), 2) + Math.pow(villagePos.getZ()-pos.getZ(), 2)<=1024;
		} return false;
	}

}
