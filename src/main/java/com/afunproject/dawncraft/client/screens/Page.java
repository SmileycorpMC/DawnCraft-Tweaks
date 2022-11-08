package com.afunproject.dawncraft.client.screens;

import com.mojang.blaze3d.vertex.PoseStack;

public interface Page {

	public void render(PoseStack poseStack, int mouseX, int mouseY, float partialTicks);

}
