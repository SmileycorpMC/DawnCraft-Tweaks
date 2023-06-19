package com.afunproject.dawncraft.integration.quests.custom.quests;

import com.afunproject.dawncraft.integration.quests.custom.QuestEntity;
import com.afunproject.dawncraft.integration.quests.custom.QuestType;
import com.afunproject.dawncraft.integration.quests.custom.conditions.AndCondition;
import com.afunproject.dawncraft.integration.quests.custom.conditions.ItemCondition;
import com.afunproject.dawncraft.integration.quests.custom.conditions.OrCondition;
import com.afunproject.dawncraft.integration.quests.custom.conditions.QuestCondition;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public abstract class ItemQuest extends Quest {

	protected final int end_phase;

	public ItemQuest(ItemStack... stacks) {
		super(getConditions(stacks));
		end_phase = 2*stacks.length+1;
	}

	@Override
	public void completeQuest(Player quest_completer, Mob entity, int phase, boolean accepted) {
		if (phase == end_phase) {
			completeItemQuest(quest_completer, entity, phase, accepted);
		}
		else if (phase % 2 == 1 && accepted) {
			setPhase(entity, phase + 1);
			completeItemQuest(quest_completer, entity, phase, accepted);
		}
		QuestEntity.safeCast(entity).setQuestText(getText(QuestEntity.safeCast(entity).getQuestPhase(), accepted));
	}

	@Override
	public String getText(int phase, boolean accepted) {
		return getText() + phase;
	}

	@Override
	public QuestType getQuestType(int phase) {
		if (phase == end_phase) return QuestType.AUTO_CLOSE;
		if (phase % 2 == 1) return QuestType.ACCEPT_QUEST;
		return QuestType.DENY;
	}

	@Override
	public boolean isQuestActive(Mob entity, int phase) {
		return true;
	}

	protected abstract void completeItemQuest(Player quest_completer, Mob entity, int phase, boolean accepted);

	protected abstract String getText();

	private static QuestCondition getConditions(ItemStack[] stacks) {
		QuestCondition[] conditions = new QuestCondition[stacks.length];
		for (int i = 0; i < stacks.length; i++) {
			int j = 2*(i+1);
			conditions[i] = new AndCondition((player, entity, phase, isTest)->phase==j, new ItemCondition(stacks[i]));
		}
		return new OrCondition(conditions);
	}

}
