package com.afunproject.dawncraft.network;

import com.afunproject.dawncraft.Constants;
import com.afunproject.dawncraft.client.ClientHandler;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent.Context;
import net.minecraftforge.network.simple.SimpleChannel;
import net.smileycorp.atlas.api.network.NetworkUtils;
import net.smileycorp.atlas.api.network.SimpleIntMessage;
import net.smileycorp.atlas.api.network.SimpleStringMessage;

public class DCNetworkHandler {

	public static SimpleChannel NETWORK_INSTANCE;

	public static void initPackets() {
		NETWORK_INSTANCE = NetworkUtils.createChannel(Constants.loc("main"));
		NetworkUtils.registerMessage(NETWORK_INSTANCE, 0, SimpleStringMessage.class, (T, K) -> processNotificationMessage(T, K.get()));
		NetworkUtils.registerMessage(NETWORK_INSTANCE, 1, SimpleIntMessage.class, (T, K) -> processAnimalMessage(T, K.get()));
		NetworkUtils.registerMessage(NETWORK_INSTANCE, 2, ToastMessage.class, (T, K) -> processToastMessage(T, K.get()));
	}

	public static void processNotificationMessage(SimpleStringMessage message, Context ctx) {
		ctx.enqueueWork(() -> DistExecutor.safeRunWhenOn(Dist.CLIENT, () -> () -> ClientHandler.displayMessage(message.getText())));
		ctx.setPacketHandled(true);
	}

	public static void processAnimalMessage(SimpleIntMessage message, Context ctx) {
		ctx.enqueueWork(() -> DistExecutor.safeRunWhenOn(Dist.CLIENT, () -> () -> ClientHandler.displayAnimalMessage(message.get())));
		ctx.setPacketHandled(true);
	}

	public static void processToastMessage(ToastMessage message, Context ctx) {
		ctx.enqueueWork(() -> DistExecutor.safeRunWhenOn(Dist.CLIENT, () -> () -> ClientHandler.displayToast(message.get())));
		ctx.setPacketHandled(true);
	}

}
