package com.afunproject.dawncraft.integration.quests;

import com.afunproject.dawncraft.integration.quests.custom.quests.QuestsRegistry;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;

public class DCQuestData implements QuestData {

	protected ResourceLocation quest;
	protected int phase;
	protected boolean accepted;

	public DCQuestData() {};

	public DCQuestData(ResourceLocation loc) {
		quest = loc;
	}

	public void setQuestPhase(int phase, boolean accepted) {
		this.phase = phase;
		this.accepted = accepted;
	}

	public int getQuestPhase() {
		return phase;
	}

	@Override
	public ResourceLocation getQuestLocation() {
		return quest;
	}

	@Override
	public String getText() {
		return QuestsRegistry.getQuest(quest).getText(phase, accepted);
	}

	@Override
	public QuestType getQuestType() {
		return QuestType.SPECIAL;
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
	}

}
