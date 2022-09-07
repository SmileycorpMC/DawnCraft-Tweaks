package com.afunproject.afptweaks;

import com.afunproject.afptweaks.capability.CapabilitiesRegister;
import com.afunproject.afptweaks.capability.IRestrictBlock;
import com.afunproject.afptweaks.entities.AFPTweaksEntities;
import com.afunproject.afptweaks.entities.QuestEntityBase;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.MoveTowardsRestrictionGoal;
import net.minecraft.world.level.GameType;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.player.PlayerEvent.PlayerRespawnEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

@EventBusSubscriber(modid = ModDefinitions.MODID, bus = Bus.MOD)
public class EventListener {

	@SubscribeEvent
	public void entityJoinWorld(EntityJoinWorldEvent event) {
		if (event.getEntity() instanceof PathfinderMob) {
			PathfinderMob entity = (PathfinderMob) event.getEntity();
			entity.goalSelector.addGoal(5, new MoveTowardsRestrictionGoal(entity, 1.0D));
			LazyOptional<IRestrictBlock> optional = entity.getCapability(CapabilitiesRegister.RESTRICT_BLOCK_CAPABILITY);
			if (optional.isPresent()) {
				IRestrictBlock cap = optional.resolve().get();
				if (cap.canRestrict(entity)) cap.applyRestriction(entity);
			}
		}
	}

	@SubscribeEvent
	public void playerRespawn(PlayerRespawnEvent event) {
		if (event.getPlayer() instanceof ServerPlayer) {
			ServerPlayer player = (ServerPlayer) event.getPlayer();
			if (!player.getAbilities().mayBuild) {
				player.setGameMode(GameType.SURVIVAL);
			}
		}
	}

	@SubscribeEvent
	public static void registerAttributes(EntityAttributeCreationEvent event) {
		event.put(AFPTweaksEntities.FALLEN.get(), QuestEntityBase.createAttributes());
		event.put(AFPTweaksEntities.WEREWOLF_PLAYER.get(), QuestEntityBase.createAttributes());
	}

}
