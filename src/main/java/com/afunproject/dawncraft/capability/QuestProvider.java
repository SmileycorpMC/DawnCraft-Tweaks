package com.afunproject.dawncraft.capability;

import com.afunproject.dawncraft.integration.quests.custom.QuestEntity;
import com.afunproject.dawncraft.integration.quests.custom.quests.Quest;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;


public class QuestProvider implements ICapabilitySerializable<CompoundTag> {

	private final QuestEntity impl;

	public QuestProvider() {
		impl = new QuestEntityWrapper();
	}

	@Override
	public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
		return cap == DCCapabilities.QUEST_ENTITY ? LazyOptional.of(() -> impl).cast() : LazyOptional.empty();
	}

	@Override
	public CompoundTag serializeNBT() {
		return impl.saveQuestData(new CompoundTag());
	}

	@Override
	public void deserializeNBT(CompoundTag nbt) {
		impl.loadQuestData(nbt);
	}

	protected static class QuestEntityWrapper implements QuestEntity {

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
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public void setQuestPhase(int phase) {
			// TODO Auto-generated method stub

		}

		@Override
		public void setQuest(Quest quest) {
			// TODO Auto-generated method stub

		}

		@Override
		public void setQuestText(String text) {
			// TODO Auto-generated method stub

		}

		@Override
		public boolean canSeeQuest() {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public CompoundTag saveQuestData(CompoundTag tag) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public void loadQuestData(CompoundTag tag) {
			// TODO Auto-generated method stub

		}

	}

}

