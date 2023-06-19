package com.afunproject.dawncraft.integration.quests.client.screens;

import com.feywild.quest_giver.screen.button.QuestButtonSmall;
import com.google.common.collect.Lists;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.TranslatableComponent;

import java.util.List;

public class OptionsPage implements Page {

	protected final List<AbstractWidget> widgets = Lists.newArrayList();
	protected final Button BACK_BUTTON;

	public OptionsPage(QuestScreen screen, List<String> options) {
		final int position = screen.pages.size();
		widgets.add(new QuestButtonSmall(380, 230, true, screen.entity.blockPosition(), new TranslatableComponent("text.dawncraft.close"), button -> screen.onClose()));
		BACK_BUTTON = new QuestButtonSmall(380, 230, true, screen.entity.blockPosition(), new TranslatableComponent("text.dawncraft.back"), button -> {
			for (int i = screen.pages.size()-1; i > position; i--) screen.pages.remove(i);
			screen.pageIndex=position;
		});
		for (int i = 0; i < options.size(); i++) {
			widgets.add(new DCQuestButton(200, 250 - (int)Math.floor((options.size() - i)*(150f/(float)options.size())), screen, options.get(i), BACK_BUTTON));
		}
	}

	@Override
	public void render(PoseStack poseStack, int mouseX, int mouseY, float partialTicks) {
		for (AbstractWidget widget : widgets) widget.render(poseStack, mouseX, mouseY, partialTicks);
	}

	@Override
	public List<AbstractWidget> getWidgets() {
		return widgets;
	}

}
