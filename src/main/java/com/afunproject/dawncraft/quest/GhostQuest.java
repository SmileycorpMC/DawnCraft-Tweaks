package com.afunproject.dawncraft.quest;

import com.afunproject.dawncraft.entities.Fallen;

import net.mcreator.simplemobs.init.SimpleMobsModItems;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class GhostQuest extends SimpleItemQuest {

	public GhostQuest() {
		super(new ItemStack(Items.IRON_INGOT, 6));
	}

	@Override
	protected void completeItemQuest(Mob entity, int phase, boolean accepted) {
		if (entity instanceof Fallen) {
			((Fallen)entity).startFading(new ItemStack(SimpleMobsModItems.SUMMONINGBELL.get()));
		}
	}

	@Override
	protected String getText() {
		return "text.dawncraft.quest.ghost";
	}

}
