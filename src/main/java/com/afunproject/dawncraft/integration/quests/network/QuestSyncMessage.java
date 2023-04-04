package com.afunproject.dawncraft.integration.quests.network;

import com.afunproject.dawncraft.integration.quests.QuestData;
import com.afunproject.dawncraft.integration.quests.QuestDataSerializer;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.PacketListener;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.Level;
import net.smileycorp.atlas.api.network.SimpleAbstractMessage;

public class QuestSyncMessage extends SimpleAbstractMessage {

	protected int id;
	protected QuestData data;

	public QuestSyncMessage() {}

	public QuestSyncMessage(int id, QuestData data) {
		this.id = id;
		this.data = data;
	}

	public Mob get(Level level) {
		return (Mob) level.getEntity(id);
	}

	@Override
	public void write(FriendlyByteBuf buf) {
		if (data != null) {
			CompoundTag tag = QuestDataSerializer.saveQuestData(data);
			tag.putInt("id", id);
			buf.writeNbt(tag);
		}
	}

	@Override
	public void read(FriendlyByteBuf buf) {
		CompoundTag tag = buf.readNbt();
		if (tag != null) {
			data = QuestDataSerializer.loadQuestData(tag);
			id = tag.getInt("id");
		}
	}

	public int getId() {
		return id;
	}

	public QuestData getData() {
		return data;
	}

	@Override
	public void handle(PacketListener listener) {}

}
