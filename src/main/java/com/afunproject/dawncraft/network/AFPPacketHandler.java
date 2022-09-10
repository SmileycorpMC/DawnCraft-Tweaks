package com.afunproject.dawncraft.network;

import com.afunproject.dawncraft.ModDefinitions;
import com.afunproject.dawncraft.client.ClientHandler;
import com.afunproject.dawncraft.quest.Quest;
import com.afunproject.dawncraft.quest.QuestEntity;

import net.minecraft.world.entity.Mob;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent.Context;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;
import net.smileycorp.atlas.api.network.SimpleMessageDecoder;
import net.smileycorp.atlas.api.network.SimpleMessageEncoder;
import net.smileycorp.atlas.api.network.SimpleStringMessage;

public class AFPPacketHandler {

	public static SimpleChannel NETWORK_INSTANCE;

	public static void initPackets() {
		NETWORK_INSTANCE = NetworkRegistry.newSimpleChannel(ModDefinitions.getResource("main"), ()-> "1", "1"::equals, "1"::equals);
		NETWORK_INSTANCE.registerMessage(0, SimpleStringMessage.class, new SimpleMessageEncoder<SimpleStringMessage>(),
				new SimpleMessageDecoder<SimpleStringMessage>(SimpleStringMessage.class), (T, K)-> processNotificationMessage(T, K.get()));
		NETWORK_INSTANCE.registerMessage(1, TriggerQuestCompleteMessage.class, new SimpleMessageEncoder<TriggerQuestCompleteMessage>(),
				new SimpleMessageDecoder<TriggerQuestCompleteMessage>(TriggerQuestCompleteMessage.class), (T, K)-> processQuestCompleteMessage(T, K.get()));
		NETWORK_INSTANCE.registerMessage(2, OpenQuestMessage.class, new SimpleMessageEncoder<OpenQuestMessage>(),
				new SimpleMessageDecoder<OpenQuestMessage>(OpenQuestMessage.class), (T, K)-> processOpenQuestMessage(T, K.get()));
	}

	public static void processNotificationMessage(SimpleStringMessage message, Context ctx) {
		ctx.enqueueWork(() -> DistExecutor.safeRunWhenOn(Dist.CLIENT, () -> () -> ClientHandler.displayMessage(message.getText())));
		ctx.setPacketHandled(true);
	}

	public static void processQuestCompleteMessage(TriggerQuestCompleteMessage message, Context ctx) {
		ctx.enqueueWork(() -> {
			Mob entity = message.get(ctx.getSender().level);
			if (entity instanceof QuestEntity) {
				Quest quest = ((QuestEntity) entity).getCurrentQuest();
				if (quest != null) {
					quest.completeQuest(entity, ((QuestEntity) entity).getQuestPhase(), message.isAccepted());
				}
			}
		});
		ctx.setPacketHandled(true);
	}

	private static void processOpenQuestMessage(OpenQuestMessage message, Context ctx) {
		ctx.enqueueWork(() -> DistExecutor.safeRunWhenOn(Dist.CLIENT, () -> () -> ClientHandler.openQuestGUI(message)));
		ctx.setPacketHandled(true);
	}

}
