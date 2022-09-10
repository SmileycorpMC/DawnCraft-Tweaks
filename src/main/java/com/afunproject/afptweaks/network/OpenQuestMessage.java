package com.afunproject.afptweaks.network;

import com.afunproject.afptweaks.quest.Quest;
import com.afunproject.afptweaks.quest.QuestEntity;
import com.afunproject.afptweaks.quest.QuestsRegistry;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.PacketListener;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.Level;
import net.smileycorp.atlas.api.network.SimpleAbstractMessage;

public class OpenQuestMessage extends SimpleAbstractMessage {

	public OpenQuestMessage() {}

	private int id;
	private String quest;
	private String message;
	private int phase;

	public OpenQuestMessage(Mob entity, Quest quest) {
		id = entity.getId();
		this.quest = quest.getRegistryName().toString();
		if (entity instanceof QuestEntity) {
			message = ((QuestEntity) entity).getQuestText();
			phase = ((QuestEntity) entity).getQuestPhase();
		}
	}

	public Mob get(Level level) {
		return (Mob) level.getEntity(id);
	}

	@Override
	public void write(FriendlyByteBuf buf) {
		buf.writeInt(id);
		buf.writeUtf(quest);
		buf.writeUtf(message);
		buf.writeInt(phase);
	}

	@Override
	public void read(FriendlyByteBuf buf) {
		id = buf.readInt();
		quest = buf.readUtf();
		message = buf.readUtf();
		phase = buf.readInt();
	}

	@Override
	public void handle(PacketListener listener) {}

	public Quest getQuest() {
		return QuestsRegistry.getQuest(new ResourceLocation(quest));
	}

	public String getMessage() {
		return message;
	}

	public int getPhase() {
		return phase;
	}

}
