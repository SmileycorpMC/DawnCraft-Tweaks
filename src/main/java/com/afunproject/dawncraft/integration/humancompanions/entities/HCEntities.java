package com.afunproject.dawncraft.integration.humancompanions.entities;

import com.afunproject.dawncraft.Constants;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class HCEntities {

	public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(ForgeRegistries.ENTITIES, Constants.MODID);

	public static final RegistryObject<EntityType<KnightPlayer>> KNIGHT_PLAYER = ENTITIES.register("knight_player",  () ->
	EntityType.Builder.<KnightPlayer>of(KnightPlayer::new, MobCategory.AMBIENT).sized(0.6F, 1.8F).build("knight_player"));

}
