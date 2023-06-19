package com.afunproject.dawncraft.integration.quests.custom.quests.dc;

import com.afunproject.dawncraft.integration.quests.custom.entity.QuestEntityBase;
import com.afunproject.dawncraft.integration.quests.custom.quests.ItemQuest;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity.RemovalReason;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import tictim.paraglider.contents.Contents;

public class SorcererQuest extends ItemQuest {

	public SorcererQuest() {
		super(new ItemStack(Items.PRISMARINE_SHARD));
	}

	@Override
	protected void completeItemQuest(Player quest_completer, Mob entity, int phase, boolean accepted) {
		if (phase == end_phase) {
			if (entity instanceof QuestEntityBase) {
				((QuestEntityBase) entity).setDespawnable(true);
			}
			ItemStack stack = new ItemStack(Contents.SPIRIT_ORB.get(), 1);
			giveItem(quest_completer, stack);
			quest_completer.addEffect(new MobEffectInstance(MobEffects.BLINDNESS, 20));
			entity.setRemoved(RemovalReason.DISCARDED);
		}
	}

	@Override
	protected String getText() {
		return "text.dawncraft.quest.sorcerer";
	}

}
