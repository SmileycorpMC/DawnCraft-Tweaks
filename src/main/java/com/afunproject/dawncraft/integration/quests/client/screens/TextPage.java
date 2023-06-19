package com.afunproject.dawncraft.integration.quests.client.screens;

import com.feywild.quest_giver.screen.widget.BackgroundWidget;
import com.google.common.collect.Lists;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Button;

import java.util.Collection;
import java.util.List;

public class TextPage implements Page {

	protected int lineIndex = 0;
	protected int position = 0;
	protected final List<String> lines;
	protected final QuestScreen parent;

	protected final List<AbstractWidget> widgets = Lists.newArrayList();

	public TextPage(QuestScreen parent, Collection<String> textLines, Button... buttons) {
		lines = Lists.newArrayList(textLines);
		this.parent = parent;
		widgets.add(new BackgroundWidget(parent, 50, 120){
			@Override
			public void onClick(double mouseX, double mouseY) {
				if (lineIndex < lines.size() || position < lines.get(lineIndex).length()) {
					lineIndex = lines.size() - 1;
					position = lines.get(lineIndex).length() - 1;
				}
			}
		});
		for (Button button : buttons) widgets.add(button);
	}

	@Override
	public void render(PoseStack poseStack, int mouseX, int mouseY, float partialTicks) {
		Minecraft mc = Minecraft.getInstance();
		GuiComponent.drawString(poseStack, mc.font, parent.getTitle(), (parent.width / 2) - (mc.font.width(parent.getTitle())), 125, 0xFFFFFF);
		for (AbstractWidget widget : widgets) widget.render(poseStack, mouseX, mouseY, partialTicks);
		int i = 0;
		for (String string : lines) {
			GuiComponent.drawString(poseStack, mc.font, i < lineIndex ? string : string.substring(0, position), 72, 142 + (i*10), 0xFFFFFF);
			if (i == lineIndex) {
				if (position++ >= string.length()) {
					position = 0;
					lineIndex++;
				}
				break;
			}
			i++;
		}
	}

	@Override
	public List<AbstractWidget> getWidgets() {
		return widgets;
	}

	protected void addWidget(AbstractWidget widget) {
		widgets.add(widget);
	}

}
