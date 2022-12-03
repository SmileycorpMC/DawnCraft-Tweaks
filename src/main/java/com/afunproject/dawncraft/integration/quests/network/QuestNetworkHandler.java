package com.afunproject.dawncraft.integration.quests.network;

import com.afunproject.dawncraft.integration.quests.client.QuestClientEvents;
import com.afunproject.dawncraft.integration.quests.custom.QuestEntity;
import com.afunproject.dawncraft.integration.quests.custom.quests.Quest;
import com.afunproject.dawncraft.network.DCNetworkHandler;

import net.minecraft.world.entity.Mob;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent.Context;
import net.smileycorp.atlas.api.network.SimpleMessageDecoder;
import net.smileycorp.atlas.api.network.SimpleMessageEncoder;

public class QuestNetworkHandler {

	public static void initPackets() {
		DCNetworkHandler.NETWORK_INSTANCE.registerMessage(1, TriggerQuestCompleteMessage.class, new SimpleMessageEncoder<TriggerQuestCompleteMessage>(),
				new SimpleMessageDecoder<TriggerQuestCompleteMessage>(TriggerQuestCompleteMessage.class), (T, K)-> processQuestCompleteMessage(T, K.get()));
		DCNetworkHandler.NETWORK_INSTANCE.registerMessage(2, OpenQuestMessage.class, new SimpleMessageEncoder<OpenQuestMessage>(),
				new SimpleMessageDecoder<OpenQuestMessage>(OpenQuestMessage.class), (T, K)-> processOpenQuestMessage(T, K.get()));
	}

	public static void processQuestCompleteMessage(TriggerQuestCompleteMessage message, Context ctx) {
		ctx.enqueueWork(() -> {
			Mob entity = message.get(ctx.getSender().level);
			QuestEntity questEntity = QuestEntity.safeCast(entity);
			Quest quest = questEntity.getCurrentQuest();
			if (quest != null) {
				quest.completeQuest(ctx.getSender(), entity, questEntity.getQuestPhase(), message.isAccepted());
			}
		});
		ctx.setPacketHandled(true);
	}

	private static void processOpenQuestMessage(OpenQuestMessage message, Context ctx) {
		ctx.enqueueWork(() -> DistExecutor.safeRunWhenOn(Dist.CLIENT, () -> () -> QuestClientEvents.openQuestGUI(message)));
		ctx.setPacketHandled(true);
	}

}
