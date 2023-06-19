package com.afunproject.dawncraft.integration.quests.client.screens;

import com.feywild.quest_giver.screen.button.QuestButton;
import com.google.common.collect.Lists;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.TranslatableComponent;

import java.util.List;

public class DCQuestButton extends QuestButton {

	protected final QuestScreen parent;
	protected final List<Page> pages = Lists.newArrayList();

	public DCQuestButton(int x, int y, QuestScreen parent, String text, Button back_button) {
		super(x, y, true, parent.entity.blockPosition(), new TranslatableComponent(text + ".title"), (button)->{});
		this.parent = parent;
		pages.addAll(QuestScreen.generatePages(parent, new TranslatableComponent(text + ".text")));
		if (pages.size() > 0) ((TextPage)pages.get(pages.size()-1)).addWidget(back_button);
	}

	@Override
	public void onPress() {
		super.onPress();
		parent.pages.addAll(pages);
		parent.pageIndex++;
	}


}
