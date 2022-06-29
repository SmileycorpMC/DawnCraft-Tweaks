package com.afunproject.packtweaks.data;

import com.afunproject.packtweaks.PackTweaks;

import net.minecraft.data.DataGenerator;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.forge.event.lifecycle.GatherDataEvent;

@EventBusSubscriber(modid = PackTweaks.MODID, bus = Bus.MOD)
public class DataGenerators {

	@SubscribeEvent
	public static void generateData(GatherDataEvent event) {
		DataGenerator generator = event.getGenerator();
		if (event.includeServer()) {
			//event.addProvider(new LootTableProvider(generator));
		}
		//generator.addProvider(new PackTweaksBlockstates(generator, event.getExistingFileHelper()));

	}

}
