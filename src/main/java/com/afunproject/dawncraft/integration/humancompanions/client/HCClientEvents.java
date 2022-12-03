package com.afunproject.dawncraft.integration.humancompanions.client;

import com.afunproject.dawncraft.client.entity.PlayerEntityRenderer;
import com.afunproject.dawncraft.integration.humancompanions.entities.HCEntities;

import net.minecraftforge.client.event.EntityRenderersEvent.RegisterRenderers;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class HCClientEvents {

	public static void init() {
		MinecraftForge.EVENT_BUS.register(HCClientEvents.class);
	}

	@SubscribeEvent
	public static void registerEntityRenderers(RegisterRenderers event) {
		event.registerEntityRenderer(HCEntities.KNIGHT_PLAYER.get(), PlayerEntityRenderer::new);
	}

}
