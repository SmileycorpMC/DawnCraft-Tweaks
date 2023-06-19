package com.afunproject.dawncraft.integration.quests.custom;

import com.afunproject.dawncraft.integration.quests.custom.quests.Quest;
import net.minecraft.nbt.CompoundTag;

public class DummyQuestEntity implements QuestEntity {

	@Override
	public int getQuestPhase() {
		return 0;
	}

	@Override
	public Quest getCurrentQuest() {
		return null;
	}

	@Override
	public String getQuestText() {
		return "";
	}

	@Override
	public void setQuestPhase(int phase) {}

	@Override
	public void setQuest(Quest quest) {}

	@Override
	public void setQuestText(String text) {}

	@Override
	public boolean canSeeQuest() {
		return false;
	}

	@Override
	public CompoundTag saveQuestData(CompoundTag tag) {
		return null;
	}

	@Override
	public void loadQuestData(CompoundTag tag) {}

}
