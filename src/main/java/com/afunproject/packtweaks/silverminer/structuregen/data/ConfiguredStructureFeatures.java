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

package com.afunproject.packtweaks.silverminer.structuregen.data;

import org.jetbrains.annotations.NotNull;

import com.afunproject.packtweaks.PackTweaks;
import com.afunproject.packtweaks.silverminer.structuregen.structure.DungeonQuestConfiguration;
import com.afunproject.packtweaks.silverminer.structuregen.structure.StructureRegistration;

import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.data.worldgen.StructureSets;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.levelgen.feature.ConfiguredStructureFeature;
import net.minecraft.world.level.levelgen.feature.StructureFeature;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.JigsawConfiguration;

public class ConfiguredStructureFeatures {
	public static final Holder<ConfiguredStructureFeature<?, ?>> QUEST_DUNGEON = register(Keys.QUEST_DUNGEON, StructureRegistration.UNDERGROUND.get().configured(new DungeonQuestConfiguration(new JigsawConfiguration(TemplatePools.QUEST_DUNGEON, 7), StructureSets.VILLAGES, 5), QuestBiomeTags.HAS_QUEST_DUNGEON));

	private static <FC extends FeatureConfiguration, F extends StructureFeature<FC>> @NotNull Holder<ConfiguredStructureFeature<?, ?>> register(ResourceKey<ConfiguredStructureFeature<?, ?>> resourceKey, ConfiguredStructureFeature<FC, F> configuredStructureFeature) {
		return BuiltinRegistries.register(BuiltinRegistries.CONFIGURED_STRUCTURE_FEATURE, resourceKey, configuredStructureFeature);
	}

	public static class Keys {
		public static final ResourceKey<ConfiguredStructureFeature<?, ?>> QUEST_DUNGEON = register("quest_dungeon");
		private static @NotNull ResourceKey<ConfiguredStructureFeature<?, ?>> register(String path) {
			return ResourceKey.create(Registry.CONFIGURED_STRUCTURE_FEATURE_REGISTRY, PackTweaks.location(path));
		}
	}
}