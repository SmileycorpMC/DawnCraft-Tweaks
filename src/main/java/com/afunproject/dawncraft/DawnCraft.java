package com.afunproject.dawncraft;

import com.afunproject.dawncraft.capability.CapabilitiesRegister;
import com.afunproject.dawncraft.client.ClientEventListener;
import com.afunproject.dawncraft.dungeon.block.DungeonBlocks;
import com.afunproject.dawncraft.dungeon.block.entity.DungeonBlockEntities;
import com.afunproject.dawncraft.dungeon.item.DungeonItems;
import com.afunproject.dawncraft.effects.DawnCraftEffects;
import com.afunproject.dawncraft.entities.DawnCraftEntities;
import com.afunproject.dawncraft.integration.IntegrationHandler;
import com.afunproject.dawncraft.network.DCNetworkHandler;
import com.mojang.logging.LogUtils;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLConstructModEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

@Mod(Constants.MODID)
@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class DawnCraft {

	private static final Logger LOGGER = LogUtils.getLogger();

	public DawnCraft() {
		ModList.get().getModContainerById(Constants.MODID)
		.ifPresent(container -> Constants.VERSION = container.getModInfo().getVersion().toString());
		LOGGER.info("DawnCraft Tweaks " + Constants.VERSION + " initialized");
		ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, DCConfig.config);
		MinecraftForge.EVENT_BUS.register(new CapabilitiesRegister());
		MinecraftForge.EVENT_BUS.register(new EventListener());
	}

	@SubscribeEvent
	public static void modConstruction(FMLConstructModEvent event) {
		DCNetworkHandler.initPackets();
		DungeonBlocks.BLOCKS.register(FMLJavaModLoadingContext.get().getModEventBus());
		DungeonBlocks.ITEMS.register(FMLJavaModLoadingContext.get().getModEventBus());
		DungeonItems.ITEMS.register(FMLJavaModLoadingContext.get().getModEventBus());
		DungeonBlockEntities.BLOCK_ENTITIES.register(FMLJavaModLoadingContext.get().getModEventBus());
		DawnCraftEntities.ENTITIES.register(FMLJavaModLoadingContext.get().getModEventBus());
		DawnCraftEffects.EFFECTS.register(FMLJavaModLoadingContext.get().getModEventBus());
		IntegrationHandler.construct();
		DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {
			MinecraftForge.EVENT_BUS.register(new ClientEventListener());
			IntegrationHandler.clientSetup();
		});
	}

	@SubscribeEvent
	public static void modSetup(FMLCommonSetupEvent event) {
		IntegrationHandler.setup();
	}

	public static void logInfo(Object message) {
		LOGGER.info(String.valueOf(message));
	}

}