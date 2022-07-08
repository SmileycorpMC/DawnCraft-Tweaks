package com.afunproject.packtweaks;

import java.util.Optional;

import com.afunproject.packtweaks.capability.CapabilitiesRegister;
import com.afunproject.packtweaks.capability.IFollowQuest;
import com.afunproject.packtweaks.quest.task.FollowTask;
import com.feywild.quest_giver.quest.player.QuestData;
import com.mojang.datafixers.util.Pair;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.WrappedGoal;
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
							if (followedEntity instanceof ServerPlayer) {
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
									QuestData quests = QuestData.get((ServerPlayer) followedEntity);
									quests.checkComplete(FollowTask.INSTANCE, questCap.getStructure());
								}
							}
						}
					}
				}
			}
		}
	}

	private static boolean isInStructure(BlockPos pos, ServerLevel level, String structure) {
		if (structure.contains("#")) return isInStructureTag(pos, level , structure.replace("#", ""));
		if (!isValidResourceLocation(structure)) return false;
		Registry<ConfiguredStructureFeature<?, ?>> registry = level.registryAccess().registryOrThrow(Registry.CONFIGURED_STRUCTURE_FEATURE_REGISTRY);
		ResourceKey<ConfiguredStructureFeature<?, ?>> structureKey = ResourceKey.create(Registry.CONFIGURED_STRUCTURE_FEATURE_REGISTRY, new ResourceLocation(structure));
		Optional<Holder<ConfiguredStructureFeature<?, ?>>> structureOptional = registry.getHolder(structureKey);
		if (structureOptional.isPresent()) {
			Pair<BlockPos, Holder<ConfiguredStructureFeature<?, ?>>> pair = level.getChunkSource().getGenerator()
					.findNearestMapFeature(level, HolderSet.direct(structureOptional.get()), pos, 1, false);
			if (pair == null) return false;
			BlockPos villagePos = pair.getFirst();
			return Math.pow(villagePos.getX()-pos.getX(), 2) + Math.pow(villagePos.getZ()-pos.getZ(), 2)<=1024;
		} return false;
	}

	private static boolean isInStructureTag(BlockPos pos, ServerLevel level, String structure) {
		if (!isValidResourceLocation(structure)) return false;
		Registry<ConfiguredStructureFeature<?, ?>> registry = level.registryAccess().registryOrThrow(Registry.CONFIGURED_STRUCTURE_FEATURE_REGISTRY);
		TagKey<ConfiguredStructureFeature<?, ?>> structureTag = TagKey.create(Registry.CONFIGURED_STRUCTURE_FEATURE_REGISTRY, new ResourceLocation(structure));
		Optional<HolderSet.Named<ConfiguredStructureFeature<?, ?>>> structureOptional = registry.getTag(structureTag);
		if (structureOptional.isPresent()) {
			Pair<BlockPos, Holder<ConfiguredStructureFeature<?, ?>>> pair = level.getChunkSource().getGenerator()
					.findNearestMapFeature(level, structureOptional.get(), pos, 1, false);
			if (pair == null) return false;
			BlockPos villagePos = pair.getFirst();
			return Math.pow(villagePos.getX()-pos.getX(), 2) + Math.pow(villagePos.getZ()-pos.getZ(), 2)<=1024;
		} return false;
	}

	private static boolean isValidResourceLocation(String structure) {
		try {
			new ResourceLocation(structure.replace("#", ""));
			return true;
		} catch (Exception e) {
			return false;
		}
	}

}
