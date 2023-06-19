package com.afunproject.dawncraft.integration.quests.custom.quests.dc;

import com.afunproject.dawncraft.dungeon.item.DungeonItems;
import com.afunproject.dawncraft.dungeon.item.RebirthStaffItem;
import com.afunproject.dawncraft.integration.quests.custom.entity.QuestEntityBase;
import com.afunproject.dawncraft.integration.quests.custom.quests.ItemQuest;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.ForgeRegistries;

public class KingQuest extends ItemQuest {

	public KingQuest() {
		super(new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation("alexsmobs:soul_heart"))), RebirthStaffItem.createPowered());
	}

	@Override
	protected void completeItemQuest(Player quest_completer, Mob entity, int phase, boolean accepted) {
		if (phase == 3) {
			giveItem(quest_completer, new ItemStack(DungeonItems.REBIRTH_STAFF.get()));
		}
		if (phase == end_phase) {
			if (entity instanceof QuestEntityBase) {
				((QuestEntityBase) entity).setDespawnable(true);
			}
			giveItem(quest_completer, new ItemStack(DungeonItems.EXECUTIONER.get()));
		}
	}

	@Override
	protected String getText() {
		return "text.dawncraft.quest.king";
	}

}
