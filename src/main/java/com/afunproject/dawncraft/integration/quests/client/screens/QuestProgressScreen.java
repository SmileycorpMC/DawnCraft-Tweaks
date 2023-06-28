package com.afunproject.dawncraft.integration.quests.client.screens;

import java.util.List;

import com.afunproject.dawncraft.capability.CapabilitiesRegister;
import com.afunproject.dawncraft.capability.QuestTracker;
import com.afunproject.dawncraft.integration.quests.QuestData;
import com.afunproject.dawncraft.integration.quests.QuestType;
import com.google.common.collect.Lists;
import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraftforge.common.util.LazyOptional;

public class QuestProgressScreen extends Screen {

	protected QuestType filter = null;

	protected List<List<QuestProgressEntry>> shown_quests = Lists.newArrayList();

	protected final int ENTRIES_PER_PAGE = 9;
	protected final int ENTRY_START_Y = 100;
	protected final int ENTRY_HEIGHT = 60;

	protected int index = 0;

	public QuestProgressScreen() {
		super(new TranslatableComponent("text.dawncraft.quest_progress_button"));
		fillQuests();
	}

	private void fillQuests() {
		index  = 0;
		shown_quests.clear();
		LazyOptional<QuestTracker> op = minecraft.player.getCapability(CapabilitiesRegister.QUEST_TRACKER);
		if (op.isPresent()) {
			List<QuestData> quests = Lists.newArrayList();
			QuestTracker tracker = op.resolve().get();
			if (filter == null) quests.addAll(tracker.getQuests());
			else quests.addAll(tracker.getQuests(filter));
			if (quests.size() > 1) {
				for (int i = 0; i < Math.ceil((float)quests.size()/(float)ENTRIES_PER_PAGE); i++) {
					List<QuestData> sublist = quests.subList(i*9, Math.min(quests.size() - 1, i*9+1));
					List<QuestProgressEntry> entries = Lists.newArrayList();
					for (int j = 0; j < sublist.size(); j++) {
						entries.add(new QuestProgressEntry(sublist.get(j), width/2-80, ENTRY_START_Y + (ENTRY_HEIGHT*j)));
					}
				}
			}
		}
	}

	@Override
	public void render(PoseStack poseStack, int mouseX, int mouseY, float partialTicks) {
		if (shown_quests.size() >= index+1) {
			for (QuestProgressEntry data : shown_quests.get(index)) {
				data.render(poseStack, mouseX, mouseY, partialTicks);
			}
		}
	}

	@Override
	public boolean mouseClicked(double mouseX, double mouseY, int p_95587_) {
		for (QuestProgressEntry data : shown_quests.get(index)) {
			if (data.isMouseOver(mouseX, mouseY)) data.mouseClicked(mouseX, mouseY, p_95587_);
		}
		return super.mouseClicked(mouseX, mouseY, p_95587_);
	}


	@Override
	public boolean isPauseScreen() {
		return false;
	}



}
