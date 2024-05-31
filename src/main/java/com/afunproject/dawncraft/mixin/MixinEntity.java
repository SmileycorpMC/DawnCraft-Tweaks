package com.afunproject.dawncraft.mixin;

import com.afunproject.dawncraft.effects.DawnCraftEffects;
import com.afunproject.dawncraft.effects.FrogformEffect;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public class MixinEntity {

	@Shadow
	private Vec3 position;

	@Inject(at=@At("HEAD"), method = "getEyeY()D", cancellable = true)
	public void dctweaks$getEyeY(CallbackInfoReturnable<Double> callback) {
		if ((Object)this instanceof LivingEntity && ((LivingEntity)(Object)this).hasEffect(DawnCraftEffects.FROGFORM.get()))
			callback.setReturnValue(position.y + FrogformEffect.FROG_EYE_HEIGHT);
	}

	@Inject(at=@At("HEAD"), method = "getEyeHeight()F", cancellable = true)
	public void dctweaks$getEyeHeight(CallbackInfoReturnable<Float> callback) {
		if ((Object)this instanceof LivingEntity && ((LivingEntity)(Object)this).hasEffect(DawnCraftEffects.FROGFORM.get()))
			callback.setReturnValue(FrogformEffect.FROG_EYE_HEIGHT);
	}

	@Inject(at=@At("HEAD"), method = "getBoundingBox()Lnet/minecraft/world/phys/AABB;", cancellable = true)
	public void dctweaks$getBoundingBox(CallbackInfoReturnable<AABB> callback) {
		if ((Object)this instanceof LivingEntity && (((LivingEntity)(Object)this).hasEffect(DawnCraftEffects.FROGFORM.get())))
			callback.setReturnValue(FrogformEffect.FROG_AABB.move(position));
	}

}
