package com.afunproject.dawncraft.entities;

import com.afunproject.dawncraft.Constants;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class DawnCraftEntities {

	public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(ForgeRegistries.ENTITIES, Constants.MODID);

	public static final RegistryObject<EntityType<RitualItemEntity>> RITUAL_ITEM = ENTITIES.register("ritual_item",  () ->
	EntityType.Builder.<RitualItemEntity>of(RitualItemEntity::new, MobCategory.MISC).sized(0.5F, 0.5F).clientTrackingRange(16).build("ritual_item"));

	/*public static final RegistryObject<EntityType<GreaterSandwyrm>> GREATER_SANDWYRM = ENTITIES.register("greater_sandwyrm",  () ->
	EntityType.Builder.<GreaterSandwyrm>of(GreaterSandwyrm::new, MobCategory.MONSTER).sized(0.6F, 1.8F).build("greater_sandwyrm"));

	public static final RegistryObject<EntityType<GreaterSandwyrmSegment>> GREATER_SANDWYRM_SEGMENT = ENTITIES.register("greater_sandwyrm_segment",  () ->
	EntityType.Builder.<GreaterSandwyrmSegment>of(GreaterSandwyrmSegment::new, MobCategory.MONSTER).sized(0.6F, 1.8F).build("greater_sandwyrm_segment"));*/


}
