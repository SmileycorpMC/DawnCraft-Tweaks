package com.afunproject.dawncraft.integration.quests.custom.quests.dc;

import com.afunproject.dawncraft.DCItemTags;
import com.afunproject.dawncraft.dungeon.item.DungeonItems;
import com.afunproject.dawncraft.integration.quests.custom.QuestEntity;
import com.afunproject.dawncraft.integration.quests.custom.QuestType;
import com.afunproject.dawncraft.integration.quests.custom.conditions.AndCondition;
import com.afunproject.dawncraft.integration.quests.custom.conditions.OrCondition;
import com.afunproject.dawncraft.integration.quests.custom.conditions.QuestCondition;
import com.afunproject.dawncraft.integration.quests.custom.conditions.TagCondition;
import com.afunproject.dawncraft.integration.quests.custom.entity.QuestEntityBase;
import com.afunproject.dawncraft.integration.quests.custom.quests.Quest;
import com.afunproject.dawncraft.integration.quests.network.OpenQuestMessage;
import com.afunproject.dawncraft.network.DCNetworkHandler;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.network.NetworkDirection;

public class MonsterSlayerQuest extends Quest {

	public MonsterSlayerQuest() {
		super(new OrCondition(getCondition(DCItemTags.LESSER_MONSTER_DROP, 2),
				getCondition(DCItemTags.GREATER_MONSTER_DROP, 4), getCondition(DCItemTags.MINIBOSS_MONSTER_DROP, 6)));
	}

	private static QuestCondition getCondition(TagKey<Item> tag, int i) {
		return new AndCondition((player, entity, phase, isTest)->phase==i, new TagCondition(tag, 1, false));
	}

	@Override
	public void completeQuest(Player quest_completer, Mob entity, int phase, boolean accepted) {
		if (phase == -1) {
			QuestEntity.safeCast(entity).setQuestPhase(2);
		}
		else if (phase == 1 && accepted) {
			QuestEntity quest_entity = QuestEntity.safeCast(entity);
			quest_entity.setQuestPhase(-1);
			quest_entity.setQuestText("text.dawncraft.quest.monster_slayer" + "1b");
			DCNetworkHandler.NETWORK_INSTANCE.sendTo(new OpenQuestMessage(entity, this), ((ServerPlayer) quest_completer).connection.connection, NetworkDirection.PLAY_TO_CLIENT);
		} else if (accepted) {
			if (phase == 3) {
				giveItem(quest_completer, new ItemStack(Items.EMERALD, 3));
			}
			else if (phase == 5) {
				giveItem(quest_completer, new ItemStack(Items.EMERALD, 15));
			}
			else if (phase == 7) {
				giveItem(quest_completer, new ItemStack(Items.EMERALD, 54));
				giveItem(quest_completer, new ItemStack(DungeonItems.SLAYERS_BLADE.get()));
				if (entity instanceof QuestEntityBase) ((QuestEntityBase) entity).setDespawnable(true);
			}
			setPhase(entity, phase + 1);
		}
		QuestEntity.safeCast(entity).setQuestText(getText(QuestEntity.safeCast(entity).getQuestPhase(), accepted));
	}

	@Override
	public QuestType getQuestType(int phase) {
		if (phase == -1) return QuestType.AUTO_CLOSE;
		if (phase == 7) return QuestType.AUTO_CLOSE;
		if (phase % 2 == 1) return QuestType.ACCEPT_QUEST;
		return QuestType.DENY;
	}

	@Override
	public String getText(int phase, boolean accepted) {
		return "text.dawncraft.quest.monster_slayer" + phase;
	}

	@Override
	public boolean isQuestActive(Mob entity, int phase) {
		return phase <= 7;
	}

}
