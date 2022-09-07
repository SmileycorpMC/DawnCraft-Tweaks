package com.afunproject.afptweaks.network;

import com.afunproject.afptweaks.ModDefinitions;
import com.afunproject.afptweaks.client.ClientHandler;

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

	@SuppressWarnings("rawtypes")
	public static void initPackets() {
		NETWORK_INSTANCE = NetworkRegistry.newSimpleChannel(ModDefinitions.getResource("main"), ()-> "1", "1"::equals, "1"::equals);
		NETWORK_INSTANCE.registerMessage(1, SimpleStringMessage.class, new SimpleMessageEncoder<SimpleStringMessage>(),
				new SimpleMessageDecoder<SimpleStringMessage>(SimpleStringMessage.class), (T, K)-> processNotificationMessage(T, K.get()));
		NETWORK_INSTANCE.registerMessage(1, TriggerQuestCompleteMessage.class, new SimpleMessageEncoder<TriggerQuestCompleteMessage>(),
				new SimpleMessageDecoder<TriggerQuestCompleteMessage>(TriggerQuestCompleteMessage.class), (T, K)-> processQuestCompleteMessage(T, K.get()));
	}

	public static void processNotificationMessage(SimpleStringMessage message, Context ctx) {
		ctx.enqueueWork(() ->  DistExecutor.safeRunWhenOn(Dist.CLIENT, () -> () -> ClientHandler.displayMessage(message.getText())));
		ctx.setPacketHandled(true);
	}

	public static void processQuestCompleteMessage(TriggerQuestCompleteMessage<?> message, Context ctx) {
		ctx.enqueueWork(() -> message.get(ctx.getSender().level).completeQuest(message.isAccepted()));
		ctx.setPacketHandled(true);
	}

}
