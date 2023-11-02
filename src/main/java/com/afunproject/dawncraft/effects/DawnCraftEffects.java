package com.afunproject.dawncraft.effects;

import com.afunproject.dawncraft.Constants;
import net.minecraft.world.effect.MobEffect;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class DawnCraftEffects {

	public static final DeferredRegister<MobEffect> EFFECTS = DeferredRegister.create(ForgeRegistries.MOB_EFFECTS, Constants.MODID);

	public static final RegistryObject<MobEffect> IMMOBILIZED = EFFECTS.register("immobilized", () -> new DCEffect(0xFFB8DBDB));

	public static final RegistryObject<MobEffect> TREMOR = EFFECTS.register("tremor", () -> new DCEffect(0xFFC80000));

	public static final RegistryObject<MobEffect> FRACTURED_SOUL = EFFECTS.register("fractured_soul", () -> new DCEffect(0xFFE7FFFF));

	public static final RegistryObject<MobEffect> FROGFORM = EFFECTS.register("frogform", () -> new FrogformEffect());

}
