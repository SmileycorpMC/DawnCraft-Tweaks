package com.afunproject.dawncraft.integration.quests;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Mob;

public interface QuestData {

	public String getTitle();

	public String getID();

	public String getText();

	public QuestType getQuestType();

	public boolean isDCQuest();

	public Mob getRenderEntity();

	public CompoundTag save();

	public void load(CompoundTag tag);

}
