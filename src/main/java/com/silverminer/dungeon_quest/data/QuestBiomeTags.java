package com.silverminer.dungeon_quest.data;

import com.silverminer.dungeon_quest.DungeonQuest;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;
import org.jetbrains.annotations.NotNull;

public class QuestBiomeTags {
   public static final TagKey<Biome> HAS_QUEST_DUNGEON = create("has_structure/quest_dungeon");

   private static @NotNull TagKey<Biome> create(String id) {
      return TagKey.create(Registry.BIOME_REGISTRY, new ResourceLocation(DungeonQuest.MODID, id));
   }
}