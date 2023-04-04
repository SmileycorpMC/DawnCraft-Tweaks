package com.afunproject.dawncraft.integration.quests.client.screens;

import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.TranslatableComponent;

public class QuestProgressScreen extends Screen {

	public QuestProgressScreen() {
		super(new TranslatableComponent("text.dawncraft.quest_progress_button"));
	}

	@Override
	public boolean isPauseScreen() {
		return false;
	}

}
