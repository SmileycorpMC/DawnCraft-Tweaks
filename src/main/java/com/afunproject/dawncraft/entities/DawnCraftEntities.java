package com.afunproject.dawncraft.entities;

import com.afunproject.dawncraft.ModDefinitions;

import net.minecraft.world.entity.EntityType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class DawnCraftEntities {

	public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(ForgeRegistries.ENTITIES, ModDefinitions.MODID);

	/*public static final RegistryObject<EntityType<GreaterSandwyrm>> GREATER_SANDWYRM = ENTITIES.register("greater_sandwyrm",  () ->
	EntityType.Builder.<GreaterSandwyrm>of(GreaterSandwyrm::new, MobCategory.MONSTER).sized(0.6F, 1.8F).build("greater_sandwyrm"));

	public static final RegistryObject<EntityType<GreaterSandwyrmSegment>> GREATER_SANDWYRM_SEGMENT = ENTITIES.register("greater_sandwyrm_segment",  () ->
	EntityType.Builder.<GreaterSandwyrmSegment>of(GreaterSandwyrmSegment::new, MobCategory.MONSTER).sized(0.6F, 1.8F).build("greater_sandwyrm_segment"));*/

}
