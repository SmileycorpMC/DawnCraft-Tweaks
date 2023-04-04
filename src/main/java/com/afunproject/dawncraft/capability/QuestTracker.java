package com.afunproject.dawncraft.capability;

import java.util.Collection;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.afunproject.dawncraft.integration.quests.QuestData;
import com.afunproject.dawncraft.integration.quests.QuestDataSerializer;
import com.afunproject.dawncraft.integration.quests.QuestType;
import com.afunproject.dawncraft.integration.quests.network.QuestNetworkHandler;
import com.afunproject.dawncraft.integration.quests.network.QuestSyncMessage;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.network.NetworkDirection;

public interface QuestTracker {

	public void updateQuest(int id, QuestData data, Player player);

	public Collection<Entry<Integer, QuestData>> getQuests();

	public Collection<Entry<Integer, QuestData>> getQuests(QuestType type);

	public QuestData getQuest(Entity entity);

	public void readNBT(ListTag nbt);

	public ListTag writeNBT();

	public static class Implementation implements QuestTracker {

		protected final Map<Integer, QuestData> quest_data = Maps.newLinkedHashMap();

		@Override
		public void updateQuest(int id, QuestData data, Player player) {
			quest_data.put(id, data);
			if (player instanceof ServerPlayer) {
				QuestNetworkHandler.NETWORK_INSTANCE.sendTo(new QuestSyncMessage(id, data), ((ServerPlayer) player).connection.connection, NetworkDirection.PLAY_TO_CLIENT);
			}

		}

		@Override
		public QuestData getQuest(Entity entity) {
			return quest_data.get(entity.getId());
		}

		@Override
		public Collection<Entry<Integer, QuestData>> getQuests() {
			return quest_data.entrySet();
		}

		@Override
		public Collection<Entry<Integer, QuestData>> getQuests(QuestType type) {
			Set<Entry<Integer, QuestData>> entries = Sets.newHashSet();
			for (Entry<Integer, QuestData> entry : quest_data.entrySet()) {
				if (entry.getValue().getQuestType() == type) entries.add(entry);
			}
			return entries;
		}

		@Override
		public void readNBT(ListTag list) {
			for (Tag nbt : list) {
				CompoundTag tag = (CompoundTag) nbt;
				if (tag.contains("id")) {
					QuestData data = QuestDataSerializer.loadQuestData(tag);
					if (data != null) quest_data.put(tag.getInt("id"), data);
				}
			}
		}

		@Override
		public ListTag writeNBT() {
			ListTag list = new ListTag();
			for (Entry<Integer, QuestData> entry : quest_data.entrySet()) {
				CompoundTag tag = QuestDataSerializer.saveQuestData(entry.getValue());
				tag.putInt("id", entry.getKey());
				list.add(tag);
			}
			return list;
		}

	}

	public static class Provider implements ICapabilitySerializable<ListTag> {

		private final QuestTracker impl;

		public Provider() {
			impl = new Implementation();
		}

		@Override
		public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
			return cap == CapabilitiesRegister.QUEST_TRACKER ? LazyOptional.of(() -> impl).cast() : LazyOptional.empty();
		}

		@Override
		public ListTag serializeNBT() {
			return impl.writeNBT();
		}

		@Override
		public void deserializeNBT(ListTag nbt) {
			impl.readNBT(nbt);
		}

	}

}
