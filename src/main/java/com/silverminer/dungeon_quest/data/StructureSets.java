package com.silverminer.dungeon_quest.data;

import com.silverminer.dungeon_quest.DungeonQuest;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.levelgen.structure.StructureSet;
import net.minecraft.world.level.levelgen.structure.placement.RandomSpreadStructurePlacement;
import net.minecraft.world.level.levelgen.structure.placement.RandomSpreadType;
import net.minecraft.world.level.levelgen.structure.placement.StructurePlacement;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class StructureSets {
   public static final Holder<StructureSet> QUEST_DUNGEON = register(Keys.QUEST_DUNGEON, List.of(StructureSet.entry(ConfiguredStructureFeatures.QUEST_DUNGEON)), new RandomSpreadStructurePlacement(34, 11, RandomSpreadType.LINEAR, 10387312));

   private static @NotNull Holder<StructureSet> register(ResourceKey<StructureSet> resourceKey, List<StructureSet.StructureSelectionEntry> structureSet, StructurePlacement structurePlacement) {
      StructureSet element = new StructureSet(structureSet, structurePlacement);
      return BuiltinRegistries.register(BuiltinRegistries.STRUCTURE_SETS, resourceKey, element);
   }

   public static void bootstrap() {
   }

   public static class Keys {
      public static final ResourceKey<StructureSet> QUEST_DUNGEON = register("dungeon_quest");

      private static @NotNull ResourceKey<StructureSet> register(String path) {
         return ResourceKey.create(Registry.STRUCTURE_SET_REGISTRY, DungeonQuest.location(path));
      }
   }
}