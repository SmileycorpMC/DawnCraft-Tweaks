package com.afunproject.afptweaks.client;

import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.TranslatableComponent;

public class ClientHandler {

	public static void displayMessage(String message) {
		Minecraft mc = Minecraft.getInstance();
		TranslatableComponent component = new TranslatableComponent(message);
		mc.gui.setOverlayMessage(component.withStyle(ChatFormatting.AQUA), false);
	}

}
