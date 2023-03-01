package com.afunproject.dawncraft.entities.ai;

import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.FollowMobGoal;

public class FollowMobPredicateGoal extends FollowMobGoal {

	protected final Class<? extends Mob> target;

	public FollowMobPredicateGoal(Mob p_25271_, double p_25272_, float p_25273_, float p_25274_, Class<? extends Mob> target) {
		super(p_25271_, p_25272_, p_25273_, p_25274_);
		this.target = target;
		followPredicate = target::isInstance;
	}

}
