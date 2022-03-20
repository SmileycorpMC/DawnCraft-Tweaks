package com.silverminer.dungeon_quest.structure;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.JigsawConfiguration;
import net.minecraft.world.level.levelgen.structure.StructureSet;

public record DungeonQuestConfiguration(JigsawConfiguration jigsawConfiguration,
                                        Holder<StructureSet> structureSetHolder,
                                        int distance) implements FeatureConfiguration {
   public static final Codec<DungeonQuestConfiguration> CODEC = RecordCodecBuilder.create(dungeonQuestConfigurationInstance ->
         dungeonQuestConfigurationInstance.group(
                     JigsawConfiguration.CODEC.fieldOf("jigsaw_config").forGetter(DungeonQuestConfiguration::jigsawConfiguration),
                     StructureSet.CODEC.fieldOf("structure").forGetter(DungeonQuestConfiguration::structureSetHolder),
                     Codec.intRange(1, 32).fieldOf("max_distance").forGetter(DungeonQuestConfiguration::distance))
               .apply(dungeonQuestConfigurationInstance, DungeonQuestConfiguration::new));
}