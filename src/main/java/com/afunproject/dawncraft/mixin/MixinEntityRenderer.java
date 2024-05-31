package com.afunproject.dawncraft.mixin;

import com.afunproject.dawncraft.client.EntityRenderProperties;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EntityRenderer.class)
public abstract class MixinEntityRenderer {

	@Inject(at=@At("HEAD"), method = "shouldShowName(Lnet/minecraft/world/entity/Entity;)Z", cancellable = true)
	protected void dctweaks$shouldShowName(Entity entity, CallbackInfoReturnable<Boolean> callback) {
		if (!EntityRenderProperties.shouldRenderNameplate()) callback.setReturnValue(false);
	}

}
