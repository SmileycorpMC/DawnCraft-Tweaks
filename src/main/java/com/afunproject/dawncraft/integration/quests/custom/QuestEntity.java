package com.afunproject.dawncraft.integration.quests.custom;

import com.afunproject.dawncraft.capability.DCCapabilities;
import com.afunproject.dawncraft.integration.quests.custom.quests.Quest;
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

	static QuestEntity safeCast(Entity entity) {
		if (entity instanceof QuestEntity) return (QuestEntity) entity;
		LazyOptional<QuestEntity> optional = entity.getCapability(DCCapabilities.QUEST_ENTITY);
		if (optional.isPresent()) return optional.resolve().get();
		return new DummyQuestEntity();
	}

}
