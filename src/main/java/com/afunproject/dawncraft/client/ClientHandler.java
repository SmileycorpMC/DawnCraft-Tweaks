package com.afunproject.dawncraft.client;

import com.afunproject.dawncraft.integration.epicfight.client.EpicFightClientEvents;
import com.afunproject.dawncraft.integration.journeymap.client.JourneyMapPlugin;
import com.google.common.collect.Lists;
import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.blaze3d.platform.InputConstants.Key;
import net.minecraft.ChatFormatting;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Options;
import net.minecraft.client.gui.components.toasts.ToastComponent;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraftforge.fml.ModList;

import java.util.List;

public class ClientHandler {

	private static Options options = Minecraft.getInstance().options;

	public static List<KeyMapping> IMMOBILIZED_KEYS = Lists.newArrayList(options.keyAttack, options.keyUse, options.keyJump, options.keyShift,
			options.keyUp, options.keyLeft, options.keyDown, options.keyRight);
	public static List<KeyMapping> FROG_KEYS = Lists.newArrayList(options.keyAttack, options.keyUse);

	static {
		for (KeyMapping mapping : options.keyHotbarSlots) IMMOBILIZED_KEYS.add(mapping);
	}

	public static void displayMessage(String message) {
		Minecraft mc = Minecraft.getInstance();
		MutableComponent component = new TranslatableComponent(message).withStyle(ChatFormatting.AQUA);
		mc.gui.setOverlayMessage(component, false);
	}

	public static void displayAnimalMessage(int count) {
		Minecraft mc = Minecraft.getInstance();
		MutableComponent component = new TranslatableComponent("message.dawncraft.animal", count).withStyle(Style.EMPTY.applyFormat(ChatFormatting.DARK_GREEN).withBold(true));
		mc.gui.setOverlayMessage(component, false);
	}

	public static boolean shouldImmobilize(int keycode, boolean isMouse) {
		Key key = isMouse ? InputConstants.Type.MOUSE.getOrCreate(keycode) : InputConstants.Type.KEYSYM.getOrCreate(keycode);
		for (KeyMapping mapping : IMMOBILIZED_KEYS) if (mapping.getKey() == key) return true;
		return false;
	}

	public static boolean isUnusableByFrog(int keycode, boolean isMouse) {
		Key key = isMouse ? InputConstants.Type.MOUSE.getOrCreate(keycode) : InputConstants.Type.KEYSYM.getOrCreate(keycode);
		for (KeyMapping mapping : FROG_KEYS) if (mapping.getKey() == key) return true;
		return false;
	}

	public static void displayToast(byte b) {
		ToastComponent toasts = Minecraft.getInstance().getToasts();
		if (b > 0 && b < 3 || b == 4 && ModList.get().isLoaded("epicfight")) EpicFightClientEvents.displayToast(toasts, b);
		if (b == 8 && ModList.get().isLoaded("journeymap")) JourneyMapPlugin.getInstance().displayToast(toasts);
	}

}
