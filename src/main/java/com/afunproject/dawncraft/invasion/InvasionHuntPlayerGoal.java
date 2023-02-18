package com.afunproject.dawncraft.invasion;

import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.world.entity.player.Player;
import net.smileycorp.atlas.api.entity.ai.GoToEntityPositionGoal;

public class InvasionHuntPlayerGoal extends GoToEntityPositionGoal {

	public InvasionHuntPlayerGoal(Mob entity, Player player) {
		super(entity, player, 1.5f);
	}

	@Override
	public boolean canContinueToUse() {
		if (entity.goalSelector.getRunningGoals().anyMatch(goal->goal.getGoal() instanceof AvoidEntityGoal)) return false;
		return super.canContinueToUse();
	}

}
