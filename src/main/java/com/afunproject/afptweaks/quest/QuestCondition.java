package com.afunproject.afptweaks.quest;

import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;

public interface QuestCondition {

	public boolean apply(Player player, Mob entity, int phase);

}
