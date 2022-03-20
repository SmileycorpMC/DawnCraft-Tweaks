package com.silverminer.dungeon_quest.structure;

import com.silverminer.dungeon_quest.DungeonQuest;
import net.minecraft.world.level.levelgen.feature.StructureFeature;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class StructureRegistration {
   public static final DeferredRegister<StructureFeature<?>> STRUCTURE_FEATURE_REGISTRY =
         DeferredRegister.create(ForgeRegistries.STRUCTURE_FEATURES, DungeonQuest.MODID);
   public static final RegistryObject<DungeonQuestStructureFeature> UNDERGROUND =
         STRUCTURE_FEATURE_REGISTRY.register("quest_dungeon", DungeonQuestStructureFeature::new);
}