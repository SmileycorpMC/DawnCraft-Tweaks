package com.afunproject.afptweaks.client;

import com.afunproject.afptweaks.ModDefinitions;
import com.afunproject.afptweaks.client.entity.FallenRenderer;
import com.afunproject.afptweaks.client.entity.QuestPlayerRenderer;
import com.afunproject.afptweaks.client.render.blockentity.DungeonDoorBlockEntityRenderer;
import com.afunproject.afptweaks.dungeon.block.DungeonBlocks;
import com.afunproject.afptweaks.dungeon.block.entity.DungeonBlockEntities;
import com.afunproject.afptweaks.entities.AFPTweaksEntities;

import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.EntityRenderersEvent.RegisterRenderers;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(modid = ModDefinitions.MODID, value = Dist.CLIENT, bus = Bus.MOD)
public class ClientEventRegister {

	@SubscribeEvent
	public static void clientSetup(FMLClientSetupEvent event){
		ItemBlockRenderTypes.setRenderLayer(DungeonBlocks.CHEST_SPAWNER.get(), RenderType.cutoutMipped());
		ItemBlockRenderTypes.setRenderLayer(DungeonBlocks.REDSTONE_ACTIVATOR.get(), RenderType.cutoutMipped());
		ItemBlockRenderTypes.setRenderLayer(DungeonBlocks.REDSTONE_TRIGGER.get(), RenderType.cutoutMipped());
	}

	@SubscribeEvent
	public static void registerEntityRenderers(RegisterRenderers event) {
		event.registerBlockEntityRenderer(DungeonBlockEntities.DUNGEON_DOOR.get(), DungeonDoorBlockEntityRenderer::new);
		event.registerEntityRenderer(AFPTweaksEntities.FALLEN.get(), FallenRenderer::new);
		event.registerEntityRenderer(AFPTweaksEntities.QUEST_PLAYER.get(), QuestPlayerRenderer::new);
	}

	@SubscribeEvent
	public static void registerLayers(EntityRenderersEvent.RegisterLayerDefinitions event) {
		event.registerLayerDefinition(QuestPlayerRenderer.MAIN_LAYER, QuestPlayerRenderer::createMainLayer);
		event.registerLayerDefinition(FallenRenderer.MAIN_LAYER, FallenRenderer::createMainLayer);
	}


	@SubscribeEvent
	public static void onModelBake(ModelBakeEvent event) {

	}

}
