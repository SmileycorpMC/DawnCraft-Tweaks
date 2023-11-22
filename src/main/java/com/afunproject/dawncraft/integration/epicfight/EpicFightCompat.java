package com.afunproject.dawncraft.integration.epicfight;

import com.afunproject.dawncraft.capability.CapabilitiesRegister;
import com.afunproject.dawncraft.capability.Toasts;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.LazyOptional;
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
			if (event.getEntity() instanceof ServerPlayer) {
				ServerPlayer entity = (ServerPlayer) event.getEntity();
				if (!isCombatMode(entity)) return;
				LazyOptional<Toasts> cap = entity.getCapability(CapabilitiesRegister.TOASTS);
				if (cap.isPresent()) cap.orElseGet(null).sendDodge(entity);
			}
			if (event.getSource().getDirectEntity() instanceof ServerPlayer) {
				ServerPlayer entity = (ServerPlayer) event.getSource().getDirectEntity();
				float amount = 0.5f;
				if (amount != 0.5f) event.setCanceled(true);
				if (isCombatMode(entity)) return;
				event.setAmount(event.getAmount() * amount);
				LazyOptional<Toasts> cap = entity.getCapability(CapabilitiesRegister.TOASTS);
				if (cap.isPresent()) cap.orElseGet(null).sendCombat(entity);
			}
		}
	}

	public static boolean isCombatMode(Player player) {
		if (player == null) return false;
		PlayerPatch patch = EpicFightCapabilities.getEntityPatch(player, PlayerPatch.class);
		return patch == null ? false : patch.isBattleMode();
	}

}
