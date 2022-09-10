package com.afunproject.afptweaks.quest;

import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.ItemStack;

public abstract class SimpleItemQuest extends Quest {

	public SimpleItemQuest(ItemStack stack) {
		super((player, entity, phase)->phase==2, new ItemCondition(stack));
	}

	@Override
	public void completeQuest(Mob entity, int phase, boolean accepted) {
		if (phase == 1 && accepted) {
			setPhase(entity, 2);
		}
		else if (phase == 3) {
			completeItemQuest(entity, phase, accepted);
		}
		if (entity instanceof QuestEntity) ((QuestEntity) entity).setQuestText(getText(((QuestEntity) entity).getQuestPhase(), accepted));
	}

	@Override
	public String getText(int phase, boolean accepted) {
		return getText() + phase;
	}

	@Override
	public QuestType getQuestType(int phase) {
		if (phase == 1) return QuestType.ACCEPT_QUEST;
		if (phase == 3) return QuestType.AUTO_CLOSE;
		return QuestType.DENY;
	}

	@Override
	public boolean isQuestActive(Mob entity, int phase) {
		return true;
	}

	protected abstract void completeItemQuest(Mob entity, int phase, boolean accepted);

	protected abstract String getText();


}
