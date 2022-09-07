package com.afunproject.afptweaks.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.afunproject.afptweaks.client.EntityRenderDispatcherExtension;

import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.world.entity.Entity;

@Mixin(EntityRenderer.class)
public abstract class MixinEntityRenderer {

	@Shadow
	protected EntityRenderDispatcher entityRenderDispatcher;

	@Inject(at=@At("HEAD"), method = "shouldShowName(Lnet/minecraft/world/entity/Entity;)Z", cancellable = true)
	protected void shouldShowName(Entity entity, CallbackInfoReturnable<Boolean> callback) {
		if (!((EntityRenderDispatcherExtension)entityRenderDispatcher).shouldRenderNameplate()) callback.setReturnValue(false);
	}

}
