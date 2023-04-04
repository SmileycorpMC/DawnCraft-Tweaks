package com.afunproject.dawncraft.integration.quests.client;

import com.afunproject.dawncraft.capability.CapabilitiesRegister;
import com.afunproject.dawncraft.capability.QuestTracker;
import com.afunproject.dawncraft.client.ClientEventRegister;
import com.afunproject.dawncraft.client.entity.FallenRenderer;
import com.afunproject.dawncraft.client.entity.PlayerEntityRenderer;
import com.afunproject.dawncraft.integration.quests.client.screens.QuestScreen;
import com.afunproject.dawncraft.integration.quests.custom.QuestEntity;
import com.afunproject.dawncraft.integration.quests.custom.QuestResponseType;
import com.afunproject.dawncraft.integration.quests.custom.entity.QuestEntities;
import com.afunproject.dawncraft.integration.quests.custom.quests.Quest;
import com.afunproject.dawncraft.integration.quests.network.OpenQuestMessage;
import com.afunproject.dawncraft.integration.quests.network.QuestSyncMessage;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen;
import net.minecraft.client.gui.screens.inventory.EffectRenderingInventoryScreen;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraftforge.client.event.EntityRenderersEvent.RegisterRenderers;
import net.minecraftforge.client.event.ScreenEvent.DrawScreenEvent;
import net.minecraftforge.client.event.ScreenEvent.MouseClickedEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class QuestClientEvents {

	protected static Button button = new Button(0, 0, 90, 20, new TranslatableComponent("text.dawncraft.quest_progress_button"), (button)->{
		Minecraft mc = Minecraft.getInstance();
		mc.setScreen(new QuestScreen(new Creeper(EntityType.CREEPER, mc.level), new TranslatableComponent("text.dawncraft.quest.lorem_ipsum"), QuestType.AUTO_CLOSE));
	});

	public static void init() {
		MinecraftForge.EVENT_BUS.register(new QuestClientEvents());
		ClientEventRegister.RENDERER_REGISTERS.add(QuestClientEvents::registerEntityRenderers);
	}

	public static void registerEntityRenderers(RegisterRenderers event) {
		event.registerEntityRenderer(QuestEntities.FALLEN_ADVENTURER.get(), FallenRenderer::new);
		event.registerEntityRenderer(QuestEntities.QUEST_PLAYER.get(), PlayerEntityRenderer::new);
	}

	public static void openQuestGUI(OpenQuestMessage message) {
		Minecraft mc = Minecraft.getInstance();
		Mob mob = message.get(mc.level);
		QuestEntity entity = QuestEntity.safeCast(mob);
		Quest quest = message.getQuest();
		QuestResponseType type = quest == null ? QuestResponseType.ACKNOWLEDGE : quest.getQuestType(message.getPhase() == 0 ? entity.getQuestPhase() : message.getPhase());
		String text = message.getMessage() == null ? entity.getQuestText() : message.getMessage();
		mc.setScreen(new QuestScreen(mob, new TranslatableComponent(text, mc.player), type));
	}

	public static void syncQuests(QuestSyncMessage message) {
		Minecraft mc = Minecraft.getInstance();
		Player player = mc.player;
		LazyOptional<QuestTracker> optional = player.getCapability(CapabilitiesRegister.QUEST_TRACKER);
		if (optional.isPresent()) optional.resolve().get().updateQuest(message.getId(), message.getData(), player);
  }
  
	@SubscribeEvent
	public void drawScreen(DrawScreenEvent.Post event) {
		Screen screen = event.getScreen();
		if (screen instanceof EffectRenderingInventoryScreen) {
			button.x = screen instanceof CreativeModeInventoryScreen ? screen.width/2-180 : (screen.width-90)/2;
			button.y = screen instanceof CreativeModeInventoryScreen ? screen.height/2+70 : screen.height/2-100;
			button.render(event.getPoseStack(), event.getMouseX(), event.getMouseY(), event.getPartialTicks());
		}
	}

	@SubscribeEvent
	public void mouseClick(MouseClickedEvent event) {
		if (event.getScreen() instanceof EffectRenderingInventoryScreen) if (button.isMouseOver(event.getMouseX(), event.getMouseY())) button.onPress();
	}

}
