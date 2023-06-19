package com.afunproject.dawncraft.integration.quests.custom.quests.dc;

import com.afunproject.dawncraft.integration.quests.custom.entity.Fallen;
import com.afunproject.dawncraft.integration.quests.custom.quests.ItemQuest;
import net.mcreator.simplemobs.init.SimpleMobsModItems;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class BellGhostQuest extends ItemQuest {

	public BellGhostQuest() {
		super(new ItemStack(Items.IRON_INGOT, 6));
	}

	@Override
	protected void completeItemQuest(Player quest_completer, Mob entity, int phase, boolean accepted) {
		if (entity instanceof Fallen && phase == end_phase) {
			((Fallen)entity).startFading(new ItemStack(SimpleMobsModItems.SUMMONINGBELL.get()));
		}
	}

	@Override
	protected String getText() {
		return "text.dawncraft.quest.ghost";
	}

}
