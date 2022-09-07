package com.afunproject.afptweaks.client.screens;

import java.util.List;
import java.util.Random;

import com.afunproject.afptweaks.AFPTweaks;
import com.afunproject.afptweaks.QuestType;
import com.afunproject.afptweaks.client.EntityRenderDispatcherExtension;
import com.afunproject.afptweaks.entities.QuestEntity;
import com.afunproject.afptweaks.network.TriggerQuestCompleteMessage;
import com.feywild.quest_giver.screen.BackgroundWidget;
import com.feywild.quest_giver.screen.QuestButton;
import com.feywild.quest_giver.screen.QuestButtonSmall;
import com.google.common.collect.Lists;
import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.entity.LivingEntity;

public class QuestScreen extends Screen {

	private static final int TEXT_WIDTH = 56;

	protected final LivingEntity entity;
	protected final QuestType questType;
	protected List<List<Component>> screens = Lists.newArrayList();
	protected Random random = new Random();

	protected int screen = 0;

	public QuestScreen(LivingEntity entity, MutableComponent text, QuestType questType) {
		super(entity.getName());
		this.entity = entity;
		this.questType = questType;
		generateLines(text);
	}

	private void generateLines(MutableComponent text) {
		if (text != null) {
			String str = text.getString();
			int position = 0;
			List<Component> lines = Lists.newArrayList();
			if (str.length() == 0) lines.add(new TranslatableComponent("text.afptweaks.quest.no_text"));
			while (position < str.length()) {
				int size = Math.min(TEXT_WIDTH, str.length()-position-1);
				int newPos = position + size;
				if (str.substring(position, newPos).contains("¶")) {
					int i = str.substring(position, newPos).indexOf("¶");
					MutableComponent component = new TextComponent(str.substring(position, position + i));
					component.setStyle(text.getStyle());
					lines.add(component);
					screens.add(lines);
					lines = Lists.newArrayList();
					position = position + i + 1;
					continue;
				}
				if (size < TEXT_WIDTH) {
					MutableComponent component = new TextComponent(str.substring(position, str.length()-1));
					component.setStyle(text.getStyle());
					lines.add(component);
					break;
				}
				for (int i = 0; i <= size; i++) {
					if (i == size) {
						MutableComponent component = new TextComponent(str.substring(position, newPos+1));
						component.setStyle(text.getStyle());
						lines.add(component);
						position = newPos;
						if (lines.size() >= 10) {
							screens.add(lines);
							lines = Lists.newArrayList();
						}
						break;
					}
					else if (str.charAt(newPos-i) == ' ') {
						AFPTweaks.logInfo("position=" + position + ", newPos=" + newPos + ", i=" + i);
						MutableComponent component = new TextComponent(str.substring(position, newPos-i+1));
						component.setStyle(text.getStyle());
						lines.add(component);
						position = newPos-i+1;
						if (lines.size() >= 10) {
							screens.add(lines);
							lines = Lists.newArrayList();
						}
						break;
					}
				}

			}
			screens.add(lines);
		} else {
			screens.add(Lists.newArrayList(new TranslatableComponent("text.afptweaks.quest.no_text", "null")));
		}
	}

	@Override
	protected void init() {
		addRenderableWidget(new BackgroundWidget(this, 50, 120));
		addRenderableWidget(new QuestButtonSmall(380, 120, true, entity.blockPosition(), new TextComponent(">>"), button -> {
			if (screen == screens.size()-1) {
				if (entity instanceof QuestEntity) completeQuest(true);
				return;
			}
			screen++;
			if (!hasNextButton() && questType != QuestType.AUTO_CLOSE) {
				removeWidget(button);
				if (questType == QuestType.ACCEPT_QUEST) {
					addRenderableWidget(new QuestButton(380, 190, true, entity.blockPosition(), new TextComponent(getRandomAcceptMessage()), button1 -> {
						if (entity instanceof QuestEntity) completeQuest(true);
						onClose();
						return;
					}));

					addRenderableWidget(new QuestButton(380, 218, false, entity.blockPosition(), new TextComponent(getRandomDeclineMessage()), button1 -> {
						if (entity instanceof QuestEntity) completeQuest(false);
						onClose();
						return;
					}));
				}
				else if (questType == QuestType.ACKNOWLEDGE) {
					String[] messages = getRandomAcknowledgmentMessages();
					addRenderableWidget(new QuestButton(380, 190, true, entity.blockPosition(), new TextComponent(messages[0]), button1 -> {
						if (entity instanceof QuestEntity) completeQuest(true);
						onClose();
						return;
					}));

					addRenderableWidget(new QuestButton(380, 218, false, entity.blockPosition(), new TextComponent(messages[1]), button1 -> {
						if (entity instanceof QuestEntity) completeQuest(true);
						onClose();
						return;
					}));
				}
			}
		}));
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void completeQuest(boolean accepted) {
		if (entity instanceof QuestEntity) minecraft.player.connection.send(new TriggerQuestCompleteMessage(entity, accepted));
	}

	@Override
	public void render(PoseStack poseStack, int mouseX, int mouseY, float partialTicks) {
		int entityX = 37;
		int entityY = 240;
		int size = 65;
		EntityRenderDispatcher dispatcher = minecraft.getEntityRenderDispatcher();
		((EntityRenderDispatcherExtension)dispatcher).setRenderNameplate(false);
		InventoryScreen.renderEntityInInventory(entityX, entityY, size, entityX - mouseX, entityY + (entity.getEyeHeight()) - mouseY, entity);
		((EntityRenderDispatcherExtension)dispatcher).setRenderNameplate(true);
		drawString(poseStack, minecraft.font, title, (width / 2) - (minecraft.font.width(title)), 125, 0xFFFFFF);
		if (screens.size() > 0) {
			int i = 0;
			for (Component component : screens.get(screen)) {
				drawString(poseStack, minecraft.font, component, 72, 142 + (i*10), 0xFFFFFF);
				i++;
			}
		}
		super.render(poseStack, mouseX, mouseY, partialTicks);
	}

	private boolean hasNextButton() {
		return screen < screens.size() -1;
	}

	@Override
	public boolean isPauseScreen() {
		return false;
	}

	private String[] getRandomAcknowledgmentMessages() {
		String[] messages = new String[2];
		messages[0] = getRandomAcknowledgmentMessage();
		while (messages[1] == null) {
			String message = getRandomAcknowledgmentMessage();
			if (messages[0] != message) messages[1] = message;
		}
		return messages;
	}

	private String getRandomAcknowledgmentMessage() {
		return switch (random.nextInt(5)) {
		case 1 -> "Got it";
		case 2 -> "Sure";
		case 3 -> "Okay!";
		case 4 -> "Alright!";
		default -> "Thanks!";
		};
	}

	private String getRandomAcceptMessage() {
		return switch (random.nextInt(5)) {
		case 1 -> "I'll do it!";
		case 2 -> "Sure";
		case 3 -> "Okay!";
		case 4 -> "Alright!";
		default -> "I accept!";
		};
	}

	private String getRandomDeclineMessage() {
		return switch (random.nextInt(5)) {
		case 1 -> "Maybe later.";
		case 2 -> "I'm Busy";
		case 3 -> "No, Sorry.";
		case 4 -> "Not right now";
		default -> "I decline!";
		};
	}

}
