package com.afunproject.dawncraft.integration.quests.network;

import com.afunproject.dawncraft.Constants;
import com.afunproject.dawncraft.client.ClientHandler;
import com.afunproject.dawncraft.integration.quests.client.QuestClientEvents;
import com.afunproject.dawncraft.integration.quests.custom.QuestEntity;
import com.afunproject.dawncraft.integration.quests.custom.quests.Quest;

import net.minecraft.world.entity.Mob;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent.Context;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;
import net.smileycorp.atlas.api.network.SimpleIntMessage;
import net.smileycorp.atlas.api.network.SimpleMessageDecoder;
import net.smileycorp.atlas.api.network.SimpleMessageEncoder;

public class QuestNetworkHandler {

	public static SimpleChannel NETWORK_INSTANCE;

	public static void initPackets() {
		NETWORK_INSTANCE = NetworkRegistry.newSimpleChannel(Constants.loc("quests"), ()-> "1", "1"::equals, "1"::equals);

		NETWORK_INSTANCE.registerMessage(1, TriggerQuestCompleteMessage.class, new SimpleMessageEncoder<TriggerQuestCompleteMessage>(),
				new SimpleMessageDecoder<TriggerQuestCompleteMessage>(TriggerQuestCompleteMessage.class), (T, K)-> processQuestCompleteMessage(T, K.get()));
		NETWORK_INSTANCE.registerMessage(2, OpenQuestMessage.class, new SimpleMessageEncoder<OpenQuestMessage>(),
				new SimpleMessageDecoder<OpenQuestMessage>(OpenQuestMessage.class), (T, K)-> processOpenQuestMessage(T, K.get()));
		NETWORK_INSTANCE.registerMessage(4, QuestSyncMessage.class, new SimpleMessageEncoder<QuestSyncMessage>(),
				new SimpleMessageDecoder<QuestSyncMessage>(QuestSyncMessage.class), (T, K)-> processSyncQuestMessage(T, K.get()));
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

	public static void processAnimalMessage(SimpleIntMessage message, Context ctx) {
		ctx.enqueueWork(() -> DistExecutor.safeRunWhenOn(Dist.CLIENT, () -> () -> ClientHandler.displayAnimalMessage(message.get())));
		ctx.setPacketHandled(true);
	}

	private static void processSyncQuestMessage(QuestSyncMessage message, Context ctx) {
		ctx.enqueueWork(() -> DistExecutor.safeRunWhenOn(Dist.CLIENT, () -> () -> QuestClientEvents.syncQuests(message)));
		ctx.setPacketHandled(true);
	}

}
