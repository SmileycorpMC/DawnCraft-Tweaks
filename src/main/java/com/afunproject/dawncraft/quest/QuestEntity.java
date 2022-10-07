package com.afunproject.dawncraft.quest;

import com.afunproject.dawncraft.capability.CapabilitiesRegister;
import com.afunproject.dawncraft.quest.quests.Quest;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.common.util.LazyOptional;

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

	public static QuestEntity safeCast(Entity entity) {
		if (entity instanceof QuestEntity) return (QuestEntity) entity;
		LazyOptional<QuestEntity> optional = entity.getCapability(CapabilitiesRegister.QUEST_ENTITY);
		if (optional.isPresent()) return optional.resolve().get();
		return new DummyQuestEntity();
	}

}
