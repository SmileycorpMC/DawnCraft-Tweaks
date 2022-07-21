package com.afunproject.afptweaks.client;

import com.afunproject.afptweaks.ModDefinitions;
import com.afunproject.afptweaks.client.render.blockentity.DungeonDoorBlockEntityRenderer;
import com.afunproject.afptweaks.dungeon.block.DungeonBlocks;
import com.afunproject.afptweaks.dungeon.block.entity.DungeonBlockEntities;

import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent.RegisterRenderers;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(modid = ModDefinitions.MODID, value = Dist.CLIENT, bus = Bus.MOD)
public class ClientEventRegister {

	@SubscribeEvent
	public static void clientSetup(FMLClientSetupEvent event){
		ItemBlockRenderTypes.setRenderLayer(DungeonBlocks.CHEST_SPAWNER.get(), RenderType.cutoutMipped());
		ItemBlockRenderTypes.setRenderLayer(DungeonBlocks.REDSTONE_TRIGGER.get(), RenderType.cutoutMipped());
	}

	@SubscribeEvent
	public static void registerEntityRenderers(RegisterRenderers event) {
		event.registerBlockEntityRenderer(DungeonBlockEntities.DUNGEON_DOOR.get(), DungeonDoorBlockEntityRenderer::new);
	}

	/*@SubscribeEvent
	public static void itemColourRegistry(ColorHandlerEvent.Block event) {
		BlockColors colors = event.getBlockColors();
		colors.register((state, reader, pos, index) -> doorColour(state, reader, pos, index), DungeonBlocks.FIRE_DOOR.get());
		colors.register((state, reader, pos, index) -> doorColour(state, reader, pos, index), DungeonBlocks.RUST_DOOR.get());
		colors.register((state, reader, pos, index) -> doorColour(state, reader, pos, index), DungeonBlocks.SAND_DOOR.get());
		colors.register((state, reader, pos, index) -> doorColour(state, reader, pos, index), DungeonBlocks.STONE_DOOR.get());
		colors.register((state, reader, pos, index) -> doorColour(state, reader, pos, index), DungeonBlocks.WOOD_DOOR.get());
	}

	@SubscribeEvent
	public static void onModelBake(ModelBakeEvent event) {

	}


	protected static int doorColour(BlockState state, BlockAndTintGetter reader, BlockPos pos, int index) {
		Optional<DungeonDoorBlockEntity> optional = reader.getBlockEntity(pos, DungeonBlockEntities.DUNGEON_DOOR.get());
		if (optional.isPresent()) {
			KeyColour colour = optional.get().getLockColour();
			if (colour != null) return colour.getColour();
		}
		return Color.WHITE.getRGB();
	}*/

}
