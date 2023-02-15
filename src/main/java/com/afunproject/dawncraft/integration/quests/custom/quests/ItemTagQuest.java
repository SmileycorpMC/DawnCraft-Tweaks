package com.afunproject.dawncraft.integration.quests.custom.quests;

import com.afunproject.dawncraft.integration.quests.custom.QuestEntity;
import com.afunproject.dawncraft.integration.quests.custom.QuestType;
import com.afunproject.dawncraft.integration.quests.custom.conditions.AndCondition;
import com.afunproject.dawncraft.integration.quests.custom.conditions.TagCondition;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;

public abstract class ItemTagQuest extends Quest {

	protected final int end_phase;

	public ItemTagQuest(ResourceLocation loc, int count) {
		super(new AndCondition((player, entity, phase, isTest)->phase==2), new TagCondition(TagKey.m_203882_(Registry.ITEM_REGISTRY, loc), count));
		end_phase = 3;
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
		if (entity instanceof QuestEntity) ((QuestEntity) entity).setQuestText(getText(((QuestEntity) entity).getQuestPhase(), accepted));
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

}
