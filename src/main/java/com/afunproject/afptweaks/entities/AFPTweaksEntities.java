package com.afunproject.afptweaks.entities;

import com.afunproject.afptweaks.ModDefinitions;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class AFPTweaksEntities {

	public static DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(ForgeRegistries.ENTITIES, ModDefinitions.MODID);

	public static RegistryObject<EntityType<Fallen>> FALLEN = ENTITIES.register("fallen",  () ->
	EntityType.Builder.<Fallen>of(Fallen::new, MobCategory.MONSTER).sized(0.6F, 1.95F).clientTrackingRange(16).build("fallen"));

}
