package com.afunproject.dawncraft.mixin;

import com.afunproject.dawncraft.effects.DawnCraftEffects;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public abstract class MixinLivingEntity extends Entity {

	public MixinLivingEntity(EntityType<?> p_19870_, Level p_19871_) {
		super(p_19870_, p_19871_);
	}

	@Inject(at=@At("HEAD"), method = "getJumpPower()F", cancellable = true)
	protected void dctweaks$getJumpPower(CallbackInfoReturnable<Float> callback) {
		if (hasEffect(DawnCraftEffects.FROGFORM.get())) callback.setReturnValue(0.77f * getBlockJumpFactor());
	}

	@Inject(at=@At("TAIL"), method = "calculateFallDamage(FF)I", cancellable = true)
	protected void dctweaks$calculateFallDamage(CallbackInfoReturnable<Integer> callback) {
		if (hasEffect(DawnCraftEffects.FROGFORM.get())) callback.setReturnValue(callback.getReturnValue() - 5);
	}

	@Shadow
	protected abstract boolean hasEffect(MobEffect mobEffect);

}
