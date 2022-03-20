package com.silverminer.dungeon_quest;

import com.mojang.logging.LogUtils;
import com.silverminer.dungeon_quest.structure.StructureRegistration;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

@Mod(DungeonQuest.MODID)
public class DungeonQuest {
   private static final Logger LOGGER = LogUtils.getLogger();
   public static final String MODID = "dungeon_quest";
   public static String VERSION = "N/A";

   public DungeonQuest() {
      ModList.get().getModContainerById(DungeonQuest.MODID)
            .ifPresent(container -> VERSION = container.getModInfo().getVersion().toString());
      LOGGER.info("Dungeon Quest " + VERSION + " initialized");
      StructureRegistration.STRUCTURE_FEATURE_REGISTRY.register(FMLJavaModLoadingContext.get().getModEventBus());
   }

   public static ResourceLocation location(String path) {
      return new ResourceLocation(MODID, path);
   }
}