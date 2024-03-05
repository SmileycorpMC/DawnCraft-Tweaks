package com.afunproject.dawncraft.mixin;

import com.afunproject.dawncraft.dungeon.item.DungeonItems;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.goal.target.TargetGoal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.Predicate;

@Mixin(NearestAttackableTargetGoal.class)
public abstract class MixinNearestAttackableTargetGoal extends TargetGoal {

	public MixinNearestAttackableTargetGoal(Mob p_26140_, boolean p_26141_) {
		super(p_26140_, p_26141_);
	}

	@Shadow
	protected TargetingConditions targetConditions;
	
	@Inject(at=@At("TAIL"), method = "<init>(Lnet/minecraft/world/entity/Mob;Ljava/lang/Class;IZZLjava/util/function/Predicate;)V", cancellable = true)
	public void init(Mob p_26053_, Class clazz, int p_26055_, boolean p_26056_, boolean p_26057_, Predicate targetSelector, CallbackInfo callback) {
		targetConditions.selector(targetSelector == null ? MixinNearestAttackableTargetGoal::hasMask : e -> targetSelector.test(e) && hasMask(e));
	}
	
	private static boolean hasMask(LivingEntity entity) {
		return entity.getItemBySlot(EquipmentSlot.HEAD).is(DungeonItems.MASK_OF_ATHORA.get());
	}

}
