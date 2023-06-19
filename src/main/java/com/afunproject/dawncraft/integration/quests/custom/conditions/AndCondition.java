package com.afunproject.dawncraft.integration.quests.custom.conditions;

import com.afunproject.dawncraft.DawnCraft;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;

public class AndCondition implements QuestCondition {

	protected final QuestCondition[] conditions;

	public AndCondition(QuestCondition... conditions) {
		this.conditions = conditions;
	}

	@Override
	public boolean apply(Player player, Mob entity, int phase, boolean isTest) {
		for (int i = 0; i < conditions.length; i++) if (!conditions[i].apply(player, entity, phase, isTest)) {
			DawnCraft.logInfo("failed on test " + (i+1) + " of " + conditions.length);
			return false;
		}
		return true;
	}

}