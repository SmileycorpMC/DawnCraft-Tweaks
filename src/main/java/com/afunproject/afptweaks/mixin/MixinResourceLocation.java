package com.afunproject.afptweaks.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.afunproject.afptweaks.ModDefinitions;

import net.minecraft.resources.ResourceLocation;

@Mixin(ResourceLocation.class)
public abstract class MixinResourceLocation {

	@Shadow
	protected String namespace;

	//automatically convert reasourcelocations from the old modid to the new one to make old worlds compatible
	@Inject(at=@At("TAIL"), method = "<init>([Ljava/lang/String;)V", cancellable = true)
	private void ResourceLocation(String[] strings, CallbackInfo callback) {
		if (namespace.equals(ModDefinitions.LEGACY_MODID)) namespace = ModDefinitions.MODID;
	}

}
