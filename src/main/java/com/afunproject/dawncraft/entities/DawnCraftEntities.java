package com.afunproject.dawncraft.entities;

import com.afunproject.dawncraft.ModDefinitions;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class DawnCraftEntities {

	public static DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(ForgeRegistries.ENTITIES, ModDefinitions.MODID);

	public static RegistryObject<EntityType<Fallen>> FALLEN = ENTITIES.register("fallen",  () ->
	EntityType.Builder.<Fallen>of(Fallen::new, MobCategory.MONSTER).sized(0.6F, 1.95F).clientTrackingRange(16).build("fallen"));

	public static RegistryObject<EntityType<QuestPlayer>> QUEST_PLAYER = ENTITIES.register("quest_player",  () ->
	EntityType.Builder.<QuestPlayer>of(QuestPlayer::new, MobCategory.MONSTER).sized(0.6F, 1.95F).clientTrackingRange(16).build("quest_player"));

}
