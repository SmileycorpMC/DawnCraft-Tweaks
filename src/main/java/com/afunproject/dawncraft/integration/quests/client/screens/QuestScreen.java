package com.afunproject.dawncraft.integration.quests.client.screens;

import java.util.List;
import java.util.Random;

import com.afunproject.dawncraft.client.EntityRenderProperties;
import com.afunproject.dawncraft.integration.quests.QuestData;
import com.afunproject.dawncraft.integration.quests.custom.QuestResponseType;
import com.afunproject.dawncraft.integration.quests.network.QuestNetworkHandler;
import com.afunproject.dawncraft.integration.quests.network.TriggerQuestCompleteMessage;
import com.feywild.quest_giver.screen.button.QuestButton;
import com.feywild.quest_giver.screen.button.QuestButtonSmall;
import com.google.common.collect.Lists;
import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.entity.Mob;

public class QuestScreen extends Screen {

	private static final int TEXT_WIDTH = 56;

	protected final Mob entity;
	protected final QuestResponseType questType;
	protected List<Page> pages = Lists.newArrayList();
	protected Random random = new Random();
	protected final Style style;

	protected int pageIndex = 0;

	protected final Button NEXT_PAGE;

	private boolean isProgressScreen = false;

	public QuestScreen(QuestData data) {
		this(data.getRenderEntity(), new TranslatableComponent(data.getText()), QuestResponseType.AUTO_CLOSE);
		isProgressScreen = true;
	}

	public QuestScreen(Mob entity, MutableComponent text, QuestResponseType questType) {
		super(entity.getName());
		this.entity = entity;
		this.questType = questType;
		style = text.getStyle();
		NEXT_PAGE = new QuestButtonSmall(380, 120, true, minecraft.player.blockPosition(), new TranslatableComponent("text.dawncraft.next"), button -> {
			if (pageIndex == pages.size()-1) {
				completeQuest(true);
				onClose();
				return;
			}
			pageIndex++;
		});
		if (questType == QuestResponseType.OPTIONS) {
			String[] pages = text.getString().split("¶");
			if (pages.length == 0) this.pages.addAll(generatePages(this, text));
			if (pages.length > 1) {
				for (int i = 0; i < pages.length-2; i++) this.pages.addAll(generatePages(this, new TranslatableComponent(pages[i])));
			}
			String[] options = pages[pages.length-1].split("¦");
			this.pages.add(new OptionsPage(this, Lists.newArrayList(options)));
		}
		else pages.addAll(generatePages(this, text));
	}

	public static List<Page> generatePages(QuestScreen screen, MutableComponent text) {
		List<Page> pages = Lists.newArrayList();
		if (text != null) {
			String str = text.getString();
			int position = 0;
			List<String> lines = Lists.newArrayList();
			if (str.length() == 0) lines.add(new TranslatableComponent("text.afptweaks.quest.no_text").getString());
			while (position < str.length()) {
				int size = Math.min(TEXT_WIDTH, str.length()-position);
				int newPos = position + size;
				if (str.substring(position, newPos).contains("¶")) {
					int i = str.substring(position, newPos).indexOf("¶");
					lines.add(str.substring(position, position + i));
					pages.add(new TextPage(screen, lines, screen.NEXT_PAGE));
					lines = Lists.newArrayList();
					position = position + i + 1;
					continue;
				}
				if (str.substring(position, newPos).contains("¬")) {
					int i = str.substring(position, newPos).indexOf("¬");
					lines.add(str.substring(position, position + i));
					position = position + i + 1;
					if (lines.size() >= 9) {
						pages.add(new TextPage(screen, lines, screen.NEXT_PAGE));
						lines = Lists.newArrayList();
					}
					continue;
				}
				if (size < TEXT_WIDTH) {
					lines.add(str.substring(position));
					break;
				}
				for (int i = 0; i <= size; i++) {
					if (i == size) {
						lines.add(str.substring(position, newPos+1));
						position = newPos;
						if (lines.size() >= 9) {
							pages.add(new TextPage(screen, lines, screen.NEXT_PAGE));
							lines = Lists.newArrayList();
						}
						break;
					}
					else if (str.charAt(newPos-i) == ' ') {
						lines.add(str.substring(position, newPos-i+1));
						position = newPos-i+1;
						if (lines.size() >= 9) {
							pages.add(new TextPage(screen, lines, screen.NEXT_PAGE));
							lines = Lists.newArrayList();
						}
						break;
					}
				}

			}
			if (screen.questType == QuestResponseType.AUTO_CLOSE) pages.add(new TextPage(screen, lines, screen.NEXT_PAGE));
			else pages.add(new TextPage(screen, lines, screen.getAcceptButtons()));
		} else {
			pages.add(new TextPage(screen, Lists.newArrayList(new TranslatableComponent("text.afptweaks.quest.no_text", "null").getString()), screen.NEXT_PAGE));
		}
		return pages;
	}

	private void completeQuest(boolean accepted) {
		if (!isProgressScreen) QuestNetworkHandler.NETWORK_INSTANCE.sendToServer(new TriggerQuestCompleteMessage(entity, accepted));
	}

	@Override
	public void render(PoseStack poseStack, int mouseX, int mouseY, float partialTicks) {
		int entityX = 37;
		int entityY = 240;
		int size = 65;
		EntityRenderProperties.setRenderNameplate(false);
		InventoryScreen.renderEntityInInventory(entityX, entityY, size, entityX - mouseX, entityY + (entity.getEyeHeight()) - mouseY, entity);
		EntityRenderProperties.setRenderNameplate(true);
		if (pages.size() > 0) {
			pages.get(pageIndex).render(poseStack, mouseX, mouseY, partialTicks);
		}
		super.render(poseStack, mouseX, mouseY, partialTicks);
	}

	@Override
	public boolean mouseClicked(double mouseX, double mouseY, int p_95587_) {
		if (pages.size() > 0) {
			for (AbstractWidget widget : pages.get(pageIndex).getWidgets()) {
				if (widget.isMouseOver(mouseX, mouseY)) widget.mouseClicked(mouseX, mouseY, p_95587_);
			}
		}
		return false;
	}

	@Override
	public boolean isPauseScreen() {
		return false;
	}

	@Override
	public boolean shouldCloseOnEsc() {
		return questType == QuestResponseType.OPTIONS || isProgressScreen;
	}

	private Button[] getAcceptButtons() {
		if (questType == QuestResponseType.ACCEPT_QUEST) {
			return new Button[] {new QuestButton(380, 190, true, entity.blockPosition(), getRandomAcceptMessage(), button1 -> {
				completeQuest(true);
				onClose();
				return;
			}), new QuestButton(380, 218, false, entity.blockPosition(), getRandomDeclineMessage(), button1 -> {
				completeQuest(false);
				onClose();
				return;
			})
			};
		}
		else if (questType == QuestResponseType.ACKNOWLEDGE) {
			TranslatableComponent msg1 = getRandomAcknowledgmentMessage();
			TranslatableComponent msg2 = getRandomAcknowledgmentMessage();
			while (msg1.equals(msg2)) msg2 = getRandomAcknowledgmentMessage();
			return new Button[] {new QuestButton(380, 190, true, entity.blockPosition(), msg1, button1 -> {
				completeQuest(true);
				onClose();
				return;
			}), new QuestButton(380, 218, false, entity.blockPosition(), msg2, button1 -> {
				completeQuest(true);
				onClose();
				return;
			})};
		}
		else if (questType == QuestResponseType.DENY) {
			TranslatableComponent msg1 = getRandomDenyMessage();
			TranslatableComponent msg2 = getRandomDenyMessage();
			while (msg1.equals(msg2)) msg2 = getRandomDenyMessage();
			return new Button[] {new QuestButton(380, 190, true, entity.blockPosition(), msg1, button1 -> {
				completeQuest(false);
				onClose();
				return;
			}), new QuestButton(380, 218, false, entity.blockPosition(), msg2, button1 -> {
				completeQuest(false);
				onClose();
				return;
			})};
		}
		return new Button[]{};
	}

	private TranslatableComponent getRandomAcknowledgmentMessage() {
		return new TranslatableComponent("text.dawncraft.acknowledge" + (random.nextInt(5)+1));
	}

	private TranslatableComponent getRandomDenyMessage() {
		return new TranslatableComponent("text.dawncraft.deny" + (random.nextInt(4)+1));
	}

	private TranslatableComponent getRandomAcceptMessage() {
		return new TranslatableComponent("text.dawncraft.accept" + (random.nextInt(5)+1));
	}

	private TranslatableComponent getRandomDeclineMessage() {
		return new TranslatableComponent("text.dawncraft.decline" + (random.nextInt(5)+1));
	}

}
