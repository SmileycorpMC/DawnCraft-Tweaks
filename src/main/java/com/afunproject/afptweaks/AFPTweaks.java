package com.afunproject.afptweaks;

import org.slf4j.Logger;

import com.afunproject.afptweaks.capability.CapabilitiesRegister;
import com.afunproject.afptweaks.client.ClientEventListener;
import com.afunproject.afptweaks.dungeon.block.DungeonBlocks;
import com.afunproject.afptweaks.dungeon.block.entity.DungeonBlockEntities;
import com.afunproject.afptweaks.dungeon.item.DungeonItems;
import com.afunproject.afptweaks.network.AFPPacketHandler;
import com.afunproject.afptweaks.quest.task.FollowTask;
import com.feywild.quest_giver.quest.task.TaskTypes;
import com.mojang.logging.LogUtils;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLConstructModEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(ModDefinitions.MODID)
@Mod.EventBusSubscriber(modid = ModDefinitions.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class AFPTweaks {

	private static final Logger LOGGER = LogUtils.getLogger();

	public AFPTweaks() {
		ModList.get().getModContainerById(ModDefinitions.MODID)
		.ifPresent(container -> ModDefinitions.VERSION = container.getModInfo().getVersion().toString());
		LOGGER.info("AFP Tweaks " + ModDefinitions.VERSION + " initialized");
		MinecraftForge.EVENT_BUS.register(new CapabilitiesRegister());
		MinecraftForge.EVENT_BUS.register(new EventListener());
		TaskTypes.register(ModDefinitions.getResource("follow_quest"), FollowTask.INSTANCE);
		AFPPacketHandler.initPackets();
	}

	@SubscribeEvent
	public static void modConstruction(FMLConstructModEvent event) {
		DungeonBlocks.BLOCKS.register(FMLJavaModLoadingContext.get().getModEventBus());
		DungeonBlocks.ITEMS.register(FMLJavaModLoadingContext.get().getModEventBus());
		DungeonItems.ITEMS.register(FMLJavaModLoadingContext.get().getModEventBus());
		DungeonBlockEntities.BLOCK_ENTITIES.register(FMLJavaModLoadingContext.get().getModEventBus());
	}

	@SubscribeEvent
	public static void clientSetup(FMLClientSetupEvent event) {
		MinecraftForge.EVENT_BUS.register(new ClientEventListener());
	}
}