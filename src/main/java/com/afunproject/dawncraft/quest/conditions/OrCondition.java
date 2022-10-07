package com.afunproject.dawncraft.quest.conditions;

import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;

public class OrCondition implements QuestCondition {

	protected final QuestCondition[] conditions;

	public OrCondition(QuestCondition... conditions) {
		this.conditions = conditions;
	}

	@Override
	public boolean apply(Player player, Mob entity, int phase) {
		for (QuestCondition condition : conditions) if (condition.apply(player, entity, phase)) return true;
		return false;
	}

}