package com.afunproject.dawncraft.entities.ai;

import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.MoveTowardsRestrictionGoal;

public class DCMoveTowardsRestrictionGoal extends MoveTowardsRestrictionGoal {

	public DCMoveTowardsRestrictionGoal(PathfinderMob p_25633_, double p_25634_) {
		super(p_25633_, p_25634_);
	}

	@Override
	public boolean canUse() {
		return mob.getTarget() == null && super.canUse();
	}

	@Override
	public boolean canContinueToUse() {
		return mob.getTarget() == null && super.canContinueToUse();
	}

}
