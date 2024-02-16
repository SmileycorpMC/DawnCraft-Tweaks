package com.afunproject.dawncraft.integration.quests.custom.entity;

import com.afunproject.dawncraft.Constants;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class QuestEntities {

	public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(ForgeRegistries.ENTITIES, Constants.MODID);

	public static final RegistryObject<EntityType<Fallen>> FALLEN_ADVENTURER = ENTITIES.register("fallen",  () ->
	EntityType.Builder.of(Fallen::new, MobCategory.MONSTER).sized(0.6F, 1.95F).clientTrackingRange(16).build("fallen"));

	public static final RegistryObject<EntityType<QuestPlayer>> QUEST_PLAYER = ENTITIES.register("quest_player",  () ->
	EntityType.Builder.of(QuestPlayer::new, MobCategory.CREATURE).sized(0.6F, 1.95F).clientTrackingRange(16).build("quest_player"));

}
