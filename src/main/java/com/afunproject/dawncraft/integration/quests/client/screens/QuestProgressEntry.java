package com.afunproject.dawncraft.integration.quests.client.screens;

import com.afunproject.dawncraft.integration.quests.QuestData;
import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraftforge.client.ForgeHooksClient;

public class QuestProgressEntry extends Button {

	protected QuestData data;

	public QuestProgressEntry(QuestData data, int x, int y) {
		super(x, y, 90, 22, new TranslatableComponent(data.getTitle()), (button)->{
			ForgeHooksClient.pushGuiLayer(Minecraft.getInstance(), new QuestScreen(data));
		});
		this.data = data;
	}

	@Override
	public void render(PoseStack poseStack, int mouseX, int mouseY, float partialTicks) {
		Screen.
	}

}
