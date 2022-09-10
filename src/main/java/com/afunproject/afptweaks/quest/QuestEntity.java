package com.afunproject.afptweaks.quest;

import net.minecraft.nbt.CompoundTag;

public interface QuestEntity {

	public int getQuestPhase();

	public Quest getCurrentQuest();

	public String getQuestText();

	public void setQuestPhase(int phase);

	public void setQuest(Quest quest);

	public void setQuestText(String text);

	public boolean canSeeQuest();

	public CompoundTag saveQuestData(CompoundTag tag);

	public void loadQuestData(CompoundTag tag);

}
