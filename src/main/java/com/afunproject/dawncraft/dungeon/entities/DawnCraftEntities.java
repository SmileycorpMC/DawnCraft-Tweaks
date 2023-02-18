package com.afunproject.dawncraft.dungeon.entities;

import com.afunproject.dawncraft.ModDefinitions;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class DawnCraftEntities {

	public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(ForgeRegistries.ENTITIES, ModDefinitions.MODID);

	public static final RegistryObject<EntityType<RitualItemEntity>> RITUAL_ITEM = ENTITIES.register("fallen",  () ->
	EntityType.Builder.<RitualItemEntity>of(RitualItemEntity::new, MobCategory.MISC).sized(0.5F, 0.5F).clientTrackingRange(16).build("ritual_item"));


}
