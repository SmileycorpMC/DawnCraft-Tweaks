package com.afunproject.dawncraft.integration.quests;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;

public interface QuestData {

	public ResourceLocation getQuestLocation();

	public String getText();

	public QuestType getQuestType();

	public boolean isDCQuest();

	public CompoundTag save();

	public void load(CompoundTag tag);

}
