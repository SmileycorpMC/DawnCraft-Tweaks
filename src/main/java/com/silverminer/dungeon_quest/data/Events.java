package com.silverminer.dungeon_quest.data;

import com.silverminer.dungeon_quest.DungeonQuest;
import net.minecraft.data.info.WorldgenRegistryDumpReport;
import net.minecraft.world.level.levelgen.feature.StructureFeature;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.forge.event.lifecycle.GatherDataEvent;
import org.jetbrains.annotations.NotNull;

@Mod.EventBusSubscriber(modid = DungeonQuest.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class Events {
   @SubscribeEvent(priority = EventPriority.LOW)
   public static void registerStructures(RegistryEvent.Register<StructureFeature<?>> event) {
      StructureSets.bootstrap();
   }

   @SubscribeEvent
   public static void onGatherDataEvent(@NotNull GatherDataEvent event) {
      event.getGenerator().addProvider(new BiomeTagProvider(event.getGenerator(), event.getExistingFileHelper()));
      event.getGenerator().addProvider(new WorldgenRegistryDumpReport(event.getGenerator()));
   }
}
