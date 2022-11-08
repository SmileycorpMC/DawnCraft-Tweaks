package com.afunproject.dawncraft.client.screens;

import java.util.Collection;
import java.util.List;

import com.feywild.quest_giver.screen.widget.BackgroundWidget;
import com.google.common.collect.Lists;
import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiComponent;

public class TextPage implements Page {

	protected int lineIndex = 0;
	protected int position = 0;
	protected final List<String> lines;

	protected final BackgroundWidget bg;

	public TextPage(QuestScreen screen, Collection<String> textLines) {
		lines = Lists.newArrayList(textLines);
		bg = new BackgroundWidget(screen, 50, 120){
			@Override
			public void onClick(double mouseX, double mouseY) {
				if (lineIndex < lines.size() || position < lines.get(lineIndex).length()) {
					lineIndex = lines.size() - 1;
					position = lines.get(lineIndex).length() - 1;
				}
			}
		};
	}

	@Override
	public void render(PoseStack poseStack, int mouseX, int mouseY, float partialTicks) {
		Minecraft mc = Minecraft.getInstance();
		bg.render(poseStack, mouseX, mouseY, partialTicks);
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

}
