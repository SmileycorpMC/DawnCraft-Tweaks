package com.afunproject.dawncraft.integration.epicfight;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import yesman.epicfight.world.capabilities.EpicFightCapabilities;
import yesman.epicfight.world.capabilities.entitypatch.EntityPatch;
import yesman.epicfight.world.capabilities.entitypatch.player.PlayerPatch;
import yesman.epicfight.world.damagesource.EpicFightDamageSource;

public class EpicFightCompat {

	public static void init() {
		MinecraftForge.EVENT_BUS.register(new EpicFightCompat());
	}

	@SubscribeEvent
	public void livingHurtEvent(LivingDamageEvent event) {
		if (!event.getEntity().level.isClientSide) {
			DamageSource source = event.getSource();
			if (source.getMsgId().equals("player") &! (source instanceof EpicFightDamageSource)) {
				event.setAmount(event.getAmount()/2f);
			}
		}
	}

	@SuppressWarnings("rawtypes")
	public static boolean isCombatMode(Player player) {
		LazyOptional<EntityPatch> optional = player.getCapability(EpicFightCapabilities.CAPABILITY_ENTITY);
		if (optional.isPresent()) if (((PlayerPatch<?>)optional.resolve().get()).isBattleMode()) return true;
		return false;
	}

}
