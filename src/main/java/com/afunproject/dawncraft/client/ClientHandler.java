package com.afunproject.dawncraft.client;

import java.util.List;

import com.afunproject.dawncraft.client.screens.QuestScreen;
import com.afunproject.dawncraft.network.OpenQuestMessage;
import com.afunproject.dawncraft.quest.Quest;
import com.afunproject.dawncraft.quest.QuestEntity;
import com.afunproject.dawncraft.quest.QuestType;
import com.google.common.collect.Lists;
import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.blaze3d.platform.InputConstants.Key;

import net.minecraft.ChatFormatting;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Options;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.entity.Mob;

public class ClientHandler {

	private static Options options = Minecraft.getInstance().options;

	public static List<KeyMapping> IMMOBILIZED_KEYS = Lists.newArrayList(options.keyAttack, options.keyUse, options.keyJump, options.keyShift,
			options.keyUp, options.keyLeft, options.keyDown, options.keyRight);

	public static void displayMessage(String message) {
		Minecraft mc = Minecraft.getInstance();
		MutableComponent component = new TranslatableComponent(message).withStyle(ChatFormatting.AQUA);
		mc.gui.setOverlayMessage(component, false);
	}

	public static boolean shouldImmobilize(int keycode, boolean isMouse) {
		Key key = isMouse ? InputConstants.Type.MOUSE.getOrCreate(keycode) : InputConstants.Type.KEYSYM.getOrCreate(keycode);
		for (KeyMapping mapping : IMMOBILIZED_KEYS) if (mapping.getKey() == key) return true;
		return false;
	}

	public static void openQuestGUI(OpenQuestMessage message) {
		Minecraft mc = Minecraft.getInstance();
		Mob mob = message.get(mc.level);
		QuestEntity entity = null;
		if (mob instanceof QuestEntity) entity = (QuestEntity) mob;
		if (entity != null) {
			Quest quest = message.getQuest();
			QuestType type = quest == null ? QuestType.ACKNOWLEDGE : quest.getQuestType(message.getPhase() == 0 ? entity.getQuestPhase() : message.getPhase());
			String text = message.getMessage() == null ? entity.getQuestText() : message.getMessage();
			mc.setScreen(new QuestScreen(mob, new TranslatableComponent(text, mc.player), type));
		}
	}

}
