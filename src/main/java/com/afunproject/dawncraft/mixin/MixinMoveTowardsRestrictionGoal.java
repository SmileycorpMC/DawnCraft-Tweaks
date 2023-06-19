package com.afunproject.dawncraft.mixin;

import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.MoveTowardsRestrictionGoal;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MoveTowardsRestrictionGoal.class)
public abstract class MixinMoveTowardsRestrictionGoal extends Goal {

	@Shadow
	protected PathfinderMob mob;

	@Inject(at=@At("HEAD"), method = "canUse()Z", cancellable = true)
	public void canUse(CallbackInfoReturnable<Boolean> callback) {
		if (mob.getTarget() != null) {
			callback.setReturnValue(false);
			callback.cancel();
		}
	}

	@Inject(at=@At("HEAD"), method = "canContinueToUse()Z", cancellable = true)
	public void canContinueToUse(CallbackInfoReturnable<Boolean> callback) {
		if (mob.getTarget() != null) {
			callback.setReturnValue(false);
			callback.cancel();
		}
	}

}
