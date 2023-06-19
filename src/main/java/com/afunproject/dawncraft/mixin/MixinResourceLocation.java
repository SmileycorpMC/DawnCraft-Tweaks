package com.afunproject.dawncraft.mixin;

import com.afunproject.dawncraft.Constants;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ResourceLocation.class)
public abstract class MixinResourceLocation {

	@Shadow
	protected String namespace;

	//automatically convert reasourcelocations from the old modid to the new one to make old worlds compatible
	@Inject(at=@At("TAIL"), method = "<init>([Ljava/lang/String;)V", cancellable = true)
	private void ResourceLocation(String[] strings, CallbackInfo callback) {
		if (namespace.equals(Constants.LEGACY_MODID)) namespace = Constants.MODID;
	}

}
