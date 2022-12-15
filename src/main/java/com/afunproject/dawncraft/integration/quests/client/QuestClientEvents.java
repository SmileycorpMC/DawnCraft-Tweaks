package com.afunproject.dawncraft.integration.quests.client;

import com.afunproject.dawncraft.client.ClientEventRegister;
import com.afunproject.dawncraft.client.entity.FallenRenderer;
import com.afunproject.dawncraft.client.entity.PlayerEntityRenderer;
import com.afunproject.dawncraft.integration.quests.client.screens.QuestScreen;
import com.afunproject.dawncraft.integration.quests.custom.QuestEntity;
import com.afunproject.dawncraft.integration.quests.custom.QuestType;
import com.afunproject.dawncraft.integration.quests.custom.entity.QuestEntities;
import com.afunproject.dawncraft.integration.quests.custom.quests.Quest;
import com.afunproject.dawncraft.integration.quests.network.OpenQuestMessage;

import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.entity.Mob;
import net.minecraftforge.client.event.EntityRenderersEvent.RegisterRenderers;
import net.minecraftforge.common.MinecraftForge;

public class QuestClientEvents {

	public static void init() {
		MinecraftForge.EVENT_BUS.register(QuestClientEvents.class);
		ClientEventRegister.RENDERER_REGISTERS.add(QuestClientEvents::registerEntityRenderers);
	}

	public static void registerEntityRenderers(RegisterRenderers event) {
		event.registerEntityRenderer(QuestEntities.FALLEN.get(), FallenRenderer::new);
		event.registerEntityRenderer(QuestEntities.QUEST_PLAYER.get(), PlayerEntityRenderer::new);
	}

	public static void openQuestGUI(OpenQuestMessage message) {
		Minecraft mc = Minecraft.getInstance();
		Mob mob = message.get(mc.level);
		QuestEntity entity = QuestEntity.safeCast(mob);
		Quest quest = message.getQuest();
		QuestType type = quest == null ? QuestType.ACKNOWLEDGE : quest.getQuestType(message.getPhase() == 0 ? entity.getQuestPhase() : message.getPhase());
		String text = message.getMessage() == null ? entity.getQuestText() : message.getMessage();
		mc.setScreen(new QuestScreen(mob, new TranslatableComponent(text, mc.player), type));
	}

}
