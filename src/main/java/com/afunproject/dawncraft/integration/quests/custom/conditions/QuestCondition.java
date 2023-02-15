package com.afunproject.dawncraft.integration.quests.custom.conditions;

import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;

public interface QuestCondition {

	public boolean apply(Player player, Mob entity, int phase, boolean isTest);

}
