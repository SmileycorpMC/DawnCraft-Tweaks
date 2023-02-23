package com.afunproject.dawncraft.client;

import java.util.List;
import java.util.function.Consumer;

import com.afunproject.dawncraft.ModDefinitions;
import com.afunproject.dawncraft.client.entity.FrogModel;
import com.afunproject.dawncraft.client.entity.FrogRenderer;
import com.afunproject.dawncraft.client.entity.PlayerEntityRenderer;
import com.afunproject.dawncraft.client.entity.RitualItemEntityRenderer;
import com.afunproject.dawncraft.client.render.blockentity.DungeonDoorBlockEntityRenderer;
import com.afunproject.dawncraft.dungeon.block.DungeonBlocks;
import com.afunproject.dawncraft.dungeon.block.entity.DungeonBlockEntities;
import com.afunproject.dawncraft.dungeon.item.DungeonItems;
import com.afunproject.dawncraft.dungeon.item.RebirthStaffItem;
import com.afunproject.dawncraft.entities.DawnCraftEntities;
import com.google.common.collect.Lists;

import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.item.ItemProperties;
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
	public static void clientSetup(FMLClientSetupEvent event) {
		event.enqueueWork(() -> {
			ItemBlockRenderTypes.setRenderLayer(DungeonBlocks.CHEST_SPAWNER.get(), RenderType.cutoutMipped());
			ItemBlockRenderTypes.setRenderLayer(DungeonBlocks.REDSTONE_ACTIVATOR.get(), RenderType.cutoutMipped());
			ItemBlockRenderTypes.setRenderLayer(DungeonBlocks.REDSTONE_TRIGGER.get(), RenderType.cutoutMipped());
			ItemProperties.register(DungeonItems.REBIRTH_STAFF.get(), ModDefinitions.getResource("inert"),
					(stack, level, entity, i) -> RebirthStaffItem.isPowered(stack) ? 0f : 1f);
		});
	}

	@SubscribeEvent
	public static void registerEntityRenderers(RegisterRenderers event) {
		event.registerBlockEntityRenderer(DungeonBlockEntities.DUNGEON_DOOR.get(), DungeonDoorBlockEntityRenderer::new);
		for (Consumer<RegisterRenderers> supplier : RENDERER_REGISTERS) supplier.accept(event);
		event.registerEntityRenderer(DawnCraftEntities.RITUAL_ITEM.get(), RitualItemEntityRenderer::new);
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
