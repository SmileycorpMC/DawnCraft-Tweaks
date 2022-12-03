package com.afunproject.dawncraft.integration.quests.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.PacketListener;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.Level;
import net.smileycorp.atlas.api.network.SimpleAbstractMessage;

public class TriggerQuestCompleteMessage extends SimpleAbstractMessage {

	public TriggerQuestCompleteMessage() {}

	private int id;
	private boolean isAccepted;

	public TriggerQuestCompleteMessage(Mob entity, boolean isAccepted) {
		id = entity.getId();
		this.isAccepted = isAccepted;
	}

	public Mob get(Level level) {
		return (Mob) level.getEntity(id);
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
