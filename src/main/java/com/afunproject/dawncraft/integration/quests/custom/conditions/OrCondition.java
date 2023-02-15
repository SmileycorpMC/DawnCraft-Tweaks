package com.afunproject.dawncraft.integration.quests.custom.conditions;

import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;

public class OrCondition implements QuestCondition {

	protected final QuestCondition[] conditions;

	public OrCondition(QuestCondition... conditions) {
		this.conditions = conditions;
	}

	@Override
	public boolean apply(Player player, Mob entity, int phase, boolean isTest) {
		for (QuestCondition condition : conditions) if (condition.apply(player, entity, phase, isTest)) return true;
		return false;
	}

}