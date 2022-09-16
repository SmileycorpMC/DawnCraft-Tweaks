package com.afunproject.dawncraft.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.afunproject.dawncraft.effects.DawnCraftEffects;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.Level;

@Mixin(Mob.class)
public abstract class MixinMob extends LivingEntity {

	public MixinMob(Level level) {
		super(null, level);
	}

	@Inject(at=@At("HEAD"), method = "isNoAi()Z", cancellable = true)
	public void isNoAi(CallbackInfoReturnable<Boolean> callback) {
		if (hasEffect(DawnCraftEffects.IMMOBILIZED.get())) {
			callback.setReturnValue(true);
			callback.cancel();
		}
	}

}
