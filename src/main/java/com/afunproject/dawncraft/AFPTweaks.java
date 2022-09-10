package com.afunproject.dawncraft;

import org.slf4j.Logger;

import com.afunproject.afptweaks.effects.AFPTweaksEffects;
import com.afunproject.dawncraft.capability.CapabilitiesRegister;
import com.afunproject.dawncraft.client.ClientEventListener;
import com.afunproject.dawncraft.client.epicfight.EpicFightClientEventListener;
import com.afunproject.dawncraft.dungeon.block.DungeonBlocks;
import com.afunproject.dawncraft.dungeon.block.entity.DungeonBlockEntities;
import com.afunproject.dawncraft.dungeon.item.DungeonItems;
import com.afunproject.dawncraft.entities.AFPTweaksEntities;
import com.afunproject.dawncraft.network.AFPPacketHandler;
import com.afunproject.dawncraft.quest_giver.quest.task.QuestModule;
import com.mojang.logging.LogUtils;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLConstructModEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(ModDefinitions.MODID)
@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class AFPTweaks {

	private static final Logger LOGGER = LogUtils.getLogger();

	public AFPTweaks() {
		ModList.get().getModContainerById(ModDefinitions.MODID)
		.ifPresent(container -> ModDefinitions.VERSION = container.getModInfo().getVersion().toString());
		LOGGER.info("AFP Tweaks " + ModDefinitions.VERSION + " initialized");
		MinecraftForge.EVENT_BUS.register(new CapabilitiesRegister());
		if(ModList.get().isLoaded("quest_giver") && ModList.get().isLoaded("followme")) QuestModule.init();
		MinecraftForge.EVENT_BUS.register(new EventListener());
		AFPPacketHandler.initPackets();
	}

	@SubscribeEvent
	public static void modConstruction(FMLConstructModEvent event) {
		DungeonBlocks.BLOCKS.register(FMLJavaModLoadingContext.get().getModEventBus());
		DungeonBlocks.ITEMS.register(FMLJavaModLoadingContext.get().getModEventBus());
		DungeonItems.ITEMS.register(FMLJavaModLoadingContext.get().getModEventBus());
		DungeonBlockEntities.BLOCK_ENTITIES.register(FMLJavaModLoadingContext.get().getModEventBus());
		AFPTweaksEntities.ENTITIES.register(FMLJavaModLoadingContext.get().getModEventBus());
		AFPTweaksEffects.EFFECTS.register(FMLJavaModLoadingContext.get().getModEventBus());
	}

	@SubscribeEvent
	public static void clientSetup(FMLClientSetupEvent event) {
		MinecraftForge.EVENT_BUS.register(new ClientEventListener());
		if(ModList.get().isLoaded("epicfight")) {
			EpicFightCompat.registerKeybinds();
			if (ModList.get().isLoaded("paraglider")) MinecraftForge.EVENT_BUS.register(new EpicFightClientEventListener());
		}
	}

	public static void logInfo(Object message) {
		LOGGER.info(String.valueOf(message));
	}

}