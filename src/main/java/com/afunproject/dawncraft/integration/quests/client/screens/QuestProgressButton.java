package com.afunproject.dawncraft.integration.quests.client.screens;

import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.TranslatableComponent;

public class QuestProgressButton extends Button {


	public QuestProgressButton(int x, int y) {
		super(x, y, 90, 22, new TranslatableComponent("text.dawncraft.quest_progress_"), (button)->{});
	}

}
