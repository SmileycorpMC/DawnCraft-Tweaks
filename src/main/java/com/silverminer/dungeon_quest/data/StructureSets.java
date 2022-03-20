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