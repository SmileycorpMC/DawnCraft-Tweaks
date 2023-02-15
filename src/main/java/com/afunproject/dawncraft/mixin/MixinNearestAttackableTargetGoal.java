package com.afunproject.dawncraft.mixin;

import java.util.List;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.afunproject.dawncraft.dungeon.item.DungeonItems;
import com.google.common.collect.Lists;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.goal.target.TargetGoal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.AABB;

@Mixin(NearestAttackableTargetGoal.class)
public abstract class MixinNearestAttackableTargetGoal extends TargetGoal {

	public MixinNearestAttackableTargetGoal(Mob p_26140_, boolean p_26141_) {
		super(p_26140_, p_26141_);
	}

	@Shadow
	protected Class<? extends LivingEntity> targetType;

	@Shadow
	protected LivingEntity target;

	@Shadow
	protected TargetingConditions targetConditions;

	@Shadow
	protected abstract AABB getTargetSearchArea(double p_26069_);

	@Inject(at=@At("HEAD"), method = "findTarget()V", cancellable = true)
	protected void findTarget(CallbackInfo callback) {
		if (targetType != Player.class && targetType != ServerPlayer.class) {
			target = mob.level.getNearestEntity(mob.level.getEntitiesOfClass(targetType, this.getTargetSearchArea(this.getFollowDistance()), (e) -> {
				return e.getItemBySlot(EquipmentSlot.HEAD).getItem() != DungeonItems.CURSED_MASK.get();
			}), targetConditions, mob, mob.getX(), mob.getEyeY(), mob.getZ());
		} else {
			List<Player> players = Lists.newArrayList();
			for (Player player : mob.level.players()) if (player.getItemBySlot(EquipmentSlot.HEAD).getItem() != DungeonItems.CURSED_MASK.get()) players.add(player);
			target = mob.level.getNearestEntity(players, targetConditions, mob, mob.getX(), mob.getEyeY(), mob.getZ());
		}
		callback.cancel();
	}

}
