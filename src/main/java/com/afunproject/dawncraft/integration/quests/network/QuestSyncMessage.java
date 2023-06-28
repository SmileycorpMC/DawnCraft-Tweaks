package com.afunproject.dawncraft.integration.quests.network;

import com.afunproject.dawncraft.integration.quests.QuestData;
import com.afunproject.dawncraft.integration.quests.QuestDataSerializer;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.PacketListener;
import net.smileycorp.atlas.api.network.SimpleAbstractMessage;

public class QuestSyncMessage extends SimpleAbstractMessage {

	protected QuestData data;

	public QuestSyncMessage() {}

	public QuestSyncMessage(QuestData data) {
		this.data = data;
	}

	@Override
	public void write(FriendlyByteBuf buf) {
		if (data != null) {
			CompoundTag tag = QuestDataSerializer.saveQuestData(data);
			buf.writeNbt(tag);
		}
	}

	@Override
	public void read(FriendlyByteBuf buf) {
		CompoundTag tag = buf.readNbt();
		if (tag != null) {
			data = QuestDataSerializer.loadQuestData(tag);
		}
	}

	public QuestData getData() {
		return data;
	}

	@Override
	public void handle(PacketListener listener) {}

}
