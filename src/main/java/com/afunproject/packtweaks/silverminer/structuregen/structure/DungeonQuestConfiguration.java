/*
 * Copyright © 2022 AFunProject
 *
 * Permission is hereby granted, free of charge,
 * to any person obtaining a copy of this software
 * and associated documentation files (the “Software”),
 * to deal in the Software without restriction, including
 * without limitation the rights to use, copy, modify, merge,
 * publish, distribute, sublicense, and/or sell copies of the
 * Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED “AS IS”, WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL
 * THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
 * WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
 * CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.afunproject.packtweaks.silverminer.structuregen.structure;

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