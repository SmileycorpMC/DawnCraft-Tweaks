package com.silverminer.dungeon_quest.data;

import com.silverminer.dungeon_quest.DungeonQuest;
import com.silverminer.dungeon_quest.structure.DungeonQuestConfiguration;
import com.silverminer.dungeon_quest.structure.StructureRegistration;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.data.worldgen.StructureSets;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.levelgen.feature.ConfiguredStructureFeature;
import net.minecraft.world.level.levelgen.feature.StructureFeature;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.JigsawConfiguration;
import org.jetbrains.annotations.NotNull;

public class ConfiguredStructureFeatures {
   public static final Holder<ConfiguredStructureFeature<?, ?>> QUEST_DUNGEON = register(Keys.QUEST_DUNGEON, StructureRegistration.UNDERGROUND.get().configured(new DungeonQuestConfiguration(new JigsawConfiguration(TemplatePools.QUEST_DUNGEON, 7), StructureSets.VILLAGES, 5), QuestBiomeTags.HAS_QUEST_DUNGEON));

   private static <FC extends FeatureConfiguration, F extends StructureFeature<FC>> @NotNull Holder<ConfiguredStructureFeature<?, ?>> register(ResourceKey<ConfiguredStructureFeature<?, ?>> resourceKey, ConfiguredStructureFeature<FC, F> configuredStructureFeature) {
      return BuiltinRegistries.register(BuiltinRegistries.CONFIGURED_STRUCTURE_FEATURE, resourceKey, configuredStructureFeature);
   }

   public static class Keys {
      public static final ResourceKey<ConfiguredStructureFeature<?, ?>> QUEST_DUNGEON = register("dungeon_quest");
      private static @NotNull ResourceKey<ConfiguredStructureFeature<?, ?>> register(String path) {
         return ResourceKey.create(Registry.CONFIGURED_STRUCTURE_FEATURE_REGISTRY, DungeonQuest.location(path));
      }
   }
}