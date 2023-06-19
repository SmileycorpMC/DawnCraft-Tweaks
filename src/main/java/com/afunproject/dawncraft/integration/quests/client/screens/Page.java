package com.afunproject.dawncraft.integration.quests.client.screens;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.components.AbstractWidget;

import java.util.List;

public interface Page {

	public void render(PoseStack poseStack, int mouseX, int mouseY, float partialTicks);

	public List<AbstractWidget> getWidgets();

}
