package com.afunproject.dawncraft.integration.quests;

import com.afunproject.dawncraft.integration.quests.custom.quests.QuestsRegistry;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;

public class QuestGiverQuestData implements QuestData {

	protected ResourceLocation quest;
	protected int phase;
	protected boolean accepted;
	protected boolean is_main;

	public QuestGiverQuestData() {};

	public QuestGiverQuestData(ResourceLocation loc) {
		quest = loc;
		is_main = false;
	}

	public void setQuestPhase(int phase, boolean accepted) {
		this.phase = phase;
		this.accepted = accepted;
	}

	public int getQuestPhase() {
		return phase;
	}

	@Override
	public String getText() {
		return QuestsRegistry.getQuest(quest).getText(phase, accepted);
	}

	@Override
	public QuestType getQuestType() {
		return is_main ? QuestType.MAIN_QUEST : QuestType.SIDE_QUEST;
	}

	@Override
	public boolean isDCQuest() {
		return true;
	}

	@Override
	public CompoundTag save() {
		CompoundTag tag = new CompoundTag();
		if (quest != null) tag.putString("quest", quest.toString());
		tag.putInt("phase", phase);
		tag.putBoolean("accepted", accepted);
		return tag;
	}

	@Override
	public void load(CompoundTag tag) {
		if (tag.contains("quest")) quest = new ResourceLocation(tag.getString("quest"));
		if (tag.contains("phase")) phase = tag.getInt("phase");
		if (tag.contains("accepted")) accepted = tag.getBoolean("accepted");
		if (tag.contains("isMain")) is_main = tag.getBoolean("isMain");
	}

	@Override
	public String getID() {
		return quest.toString();
	}

}
