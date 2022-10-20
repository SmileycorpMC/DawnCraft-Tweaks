package com.afunproject.dawncraft.effects;

import com.afunproject.dawncraft.ModDefinitions;

import net.minecraft.world.effect.MobEffect;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class DawnCraftEffects {

	public static final DeferredRegister<MobEffect> EFFECTS = DeferredRegister.create(ForgeRegistries.MOB_EFFECTS, ModDefinitions.MODID);

	public static RegistryObject<MobEffect> IMMOBILIZED = EFFECTS.register("immobilized", () -> new DCEffect(0xFFB8DBDB));

	public static RegistryObject<MobEffect> TREMOR = EFFECTS.register("tremor", () -> new DCEffect(0xFF2A7475));

}
