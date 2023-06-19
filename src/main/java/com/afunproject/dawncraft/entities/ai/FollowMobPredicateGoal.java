package com.afunproject.dawncraft.entities.ai;

import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.FollowMobGoal;
import net.smileycorp.atlas.api.util.Func;

import java.util.List;

public class FollowMobPredicateGoal extends FollowMobGoal {

	protected final Class<? extends Mob> target;

	public FollowMobPredicateGoal(Mob entity, double speed, float stopDistance, float range, Class<? extends Mob> target) {
		super(entity, speed, stopDistance, range);
		this.target = target;
	}

	@Override
	public boolean canUse() {
		List<? extends Mob> list = mob.level.getEntitiesOfClass(target, mob.getBoundingBox().inflate(areaSize), Func::True);
		if (!list.isEmpty()) {
			for(Mob mob : list) {
				if (!mob.isInvisible()) {
					followingMob = mob;
					return true;
				}
			}
		}

		return false;
	}

}
