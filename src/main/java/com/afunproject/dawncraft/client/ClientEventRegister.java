package com.afunproject.dawncraft.client;

import java.util.List;
import java.util.function.Consumer;

import com.afunproject.dawncraft.ModDefinitions;
import com.afunproject.dawncraft.client.entity.FrogModel;
import com.afunproject.dawncraft.client.entity.FrogRenderer;
import com.afunproject.dawncraft.client.entity.PlayerEntityRenderer;
import com.afunproject.dawncraft.client.render.blockentity.DungeonDoorBlockEntityRenderer;
import com.afunproject.dawncraft.dungeon.block.DungeonBlocks;
import com.afunproject.dawncraft.dungeon.block.entity.DungeonBlockEntities;
import com.google.common.collect.Lists;

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

	public static final List<Consumer<RegisterRenderers>> RENDERER_REGISTERS = Lists.newArrayList();

	@SubscribeEvent
	public static void clientSetup(FMLClientSetupEvent event){
		ItemBlockRenderTypes.setRenderLayer(DungeonBlocks.CHEST_SPAWNER.get(), RenderType.cutoutMipped());
		ItemBlockRenderTypes.setRenderLayer(DungeonBlocks.REDSTONE_ACTIVATOR.get(), RenderType.cutoutMipped());
		ItemBlockRenderTypes.setRenderLayer(DungeonBlocks.REDSTONE_TRIGGER.get(), RenderType.cutoutMipped());
	}

	@SubscribeEvent
	public static void registerEntityRenderers(RegisterRenderers event) {
		event.registerBlockEntityRenderer(DungeonBlockEntities.DUNGEON_DOOR.get(), DungeonDoorBlockEntityRenderer::new);
		for (Consumer<RegisterRenderers> supplier : RENDERER_REGISTERS) supplier.accept(event);
	}

	@SubscribeEvent
	public static void registerLayers(EntityRenderersEvent.AddLayers event) {
		FrogRenderer.init();
	}

	@SubscribeEvent
	public static void registerLayers(EntityRenderersEvent.RegisterLayerDefinitions event) {
		event.registerLayerDefinition(PlayerEntityRenderer.DEFAULT, () -> PlayerEntityRenderer.createLayer(false));
		event.registerLayerDefinition(PlayerEntityRenderer.SLIM, () -> PlayerEntityRenderer.createLayer(true));
		event.registerLayerDefinition(FrogRenderer.DEFAULT, FrogModel::createBodyLayer);
	}

	@SubscribeEvent
	public static void onModelBake(ModelBakeEvent event) {

	}

}
