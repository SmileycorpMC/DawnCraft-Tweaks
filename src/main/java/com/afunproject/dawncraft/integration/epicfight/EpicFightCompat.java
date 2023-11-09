package com.afunproject.dawncraft.integration.epicfight;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import yesman.epicfight.world.capabilities.EpicFightCapabilities;
import yesman.epicfight.world.capabilities.entitypatch.player.PlayerPatch;

public class EpicFightCompat {

	public static void init() {
		MinecraftForge.EVENT_BUS.register(new EpicFightCompat());
	}

	@SubscribeEvent
	public void livingHurtEvent(LivingDamageEvent event) {
		if (event.isCanceled()) return;
		if (!event.getEntity().level.isClientSide) {
			DamageSource source = event.getSource();
			float amount = 0.5f;
			if (source.getDirectEntity() instanceof Player) {
				if (amount != 0.5f) event.setCanceled(true);
				if (isCombatMode((Player) source.getDirectEntity())) return;
				event.setAmount(event.getAmount() * amount);
			}
		}
	}

	public static boolean isCombatMode(Player player) {
		if (player == null) return false;
		PlayerPatch patch = EpicFightCapabilities.getEntityPatch(player, PlayerPatch.class);
		return patch == null ? false : patch.isBattleMode();
	}

}
