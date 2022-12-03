package com.afunproject.dawncraft.integration.quests.client.screens;

import java.util.List;

import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.gui.components.AbstractWidget;

public interface Page {

	public void render(PoseStack poseStack, int mouseX, int mouseY, float partialTicks);

	public List<AbstractWidget> getWidgets();

}
