package com.silverminer.dungeon_quest.data;

import com.mojang.datafixers.util.Pair;
import com.silverminer.dungeon_quest.DungeonQuest;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.structure.pools.StructurePoolElement;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class TemplatePools {
   public static final Holder<StructureTemplatePool> QUEST_DUNGEON = register(Keys.QUEST_DUNGEON, new StructureTemplatePool(DungeonQuest.location("quest_dungeon"), new ResourceLocation("empty"), List.of(
         Pair.of(StructurePoolElement.single("quest_dungeon").apply(StructureTemplatePool.Projection.RIGID), 1)
   )));

   private static @NotNull Holder<StructureTemplatePool> register(ResourceKey<StructureTemplatePool> resourceKey, StructureTemplatePool structureTemplatePool) {
      return BuiltinRegistries.register(BuiltinRegistries.TEMPLATE_POOL, resourceKey, structureTemplatePool);
   }

   public static class Keys {
      public static final ResourceKey<StructureTemplatePool> QUEST_DUNGEON = register("quest_dungeon");

      private static @NotNull ResourceKey<StructureTemplatePool> register(String path) {
         return ResourceKey.create(Registry.TEMPLATE_POOL_REGISTRY, DungeonQuest.location(path));
      }
   }
}