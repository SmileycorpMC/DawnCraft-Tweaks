package com.silverminer.dungeon_quest.data;

import com.silverminer.dungeon_quest.DungeonQuest;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.BiomeTagsProvider;
import net.minecraft.tags.BiomeTags;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

public class BiomeTagProvider extends BiomeTagsProvider {
   public BiomeTagProvider(DataGenerator dataGenerator, @Nullable ExistingFileHelper existingFileHelper) {
      super(dataGenerator, DungeonQuest.MODID, existingFileHelper);
   }

   @Override
   protected void addTags() {
      this.tag(QuestBiomeTags.HAS_QUEST_DUNGEON).addTag(BiomeTags.HAS_VILLAGE_DESERT).addTag(BiomeTags.HAS_VILLAGE_PLAINS)
            .addTag(BiomeTags.HAS_VILLAGE_TAIGA).addTag(BiomeTags.HAS_VILLAGE_SNOWY).addTag(BiomeTags.HAS_VILLAGE_SAVANNA);
   }
}