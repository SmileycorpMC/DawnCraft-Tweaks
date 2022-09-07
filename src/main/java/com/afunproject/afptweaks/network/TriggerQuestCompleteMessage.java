package com.afunproject.afptweaks.network;

import com.afunproject.afptweaks.entities.QuestEntity;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.PacketListener;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.smileycorp.atlas.api.network.SimpleAbstractMessage;

public class TriggerQuestCompleteMessage<T extends Entity & QuestEntity> extends SimpleAbstractMessage {

	public TriggerQuestCompleteMessage() {}

	private int id;
	private boolean isAccepted;

	public TriggerQuestCompleteMessage(T entity, boolean isAccepted) {
		this.id = entity.getId();
		this.isAccepted = isAccepted;
	}

	@SuppressWarnings("unchecked")
	public T get(Level level) {
		return (T) level.getEntity(id);
	}

	@Override
	public void write(FriendlyByteBuf buf) {
		buf.writeInt(id);
		buf.writeBoolean(isAccepted);
	}

	@Override
	public void read(FriendlyByteBuf buf) {
		id = buf.readInt();
		isAccepted = buf.readBoolean();
	}

	@Override
	public void handle(PacketListener listener) {}

	public boolean isAccepted() {
		return isAccepted;
	}


}
