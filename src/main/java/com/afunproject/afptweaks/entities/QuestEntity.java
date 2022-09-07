package com.afunproject.afptweaks.entities;

import com.afunproject.afptweaks.QuestType;

public interface QuestEntity {

	public QuestType getQuestType();

	public boolean canSeeQuest();

	public void completeQuest(boolean accepted);

}
