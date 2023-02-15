package com.afunproject.dawncraft.integration.quests.custom.quests;

import com.afunproject.dawncraft.integration.quests.custom.QuestEntity;
import com.afunproject.dawncraft.integration.quests.custom.QuestType;

import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;

public class IntroQuest extends Quest {

	@Override
	public void completeQuest(Player quest_completer, Mob entity, int phase, boolean accepted) {
		QuestEntity questEntity = QuestEntity.safeCast(entity);
		if (questEntity.getQuestPhase() < 2) {
			questEntity.setQuestPhase(2);
			entity.getPersistentData().putBoolean("dialogue_end", true);
		}
	}

	@Override
	public String getText(int phase, boolean accepted) {
		return "text.dawncraft.quest.intro";
	}

	@Override
	public QuestType getQuestType(int phase) {
		return QuestType.ACKNOWLEDGE;
	}

	@Override
	public boolean isQuestActive(Mob entity, int phase) {
		return QuestEntity.safeCast(entity).getQuestPhase() < 2;
	}

}
