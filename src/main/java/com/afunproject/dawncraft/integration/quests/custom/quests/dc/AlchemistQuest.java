package com.afunproject.dawncraft.integration.quests.custom.quests.dc;

import com.afunproject.dawncraft.integration.quests.custom.QuestEntity;
import com.afunproject.dawncraft.integration.quests.custom.QuestType;
import com.afunproject.dawncraft.integration.quests.custom.conditions.AndCondition;
import com.afunproject.dawncraft.integration.quests.custom.conditions.ItemCondition;
import com.afunproject.dawncraft.integration.quests.custom.quests.Quest;
import com.afunproject.dawncraft.integration.quests.network.OpenQuestMessage;
import com.afunproject.dawncraft.network.DCNetworkHandler;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.network.NetworkDirection;

public class AlchemistQuest extends Quest {

	public AlchemistQuest() {
		super(new AndCondition((player, entity, phase, isTest)->phase!=-1, new ItemCondition(new ItemStack(Items.EMERALD))));
	}

	@Override
	public void completeQuest(Player quest_completer, Mob entity, int phase, boolean accepted) {
		if (phase !=-1 && accepted && isQuestComplete(quest_completer, entity, -2)) {
			QuestEntity.safeCast(entity).setQuestText(getText(-1, true));
			setPhase(entity, -1);
			if (quest_completer instanceof ServerPlayer) DCNetworkHandler.NETWORK_INSTANCE.sendTo(new OpenQuestMessage(entity, this), ((ServerPlayer) quest_completer).connection.connection, NetworkDirection.PLAY_TO_CLIENT);
		}
	}

	@Override
	public boolean isQuestComplete(Player player, Mob entity, int phase) {
		if (phase != -2) return false;
		return super.isQuestComplete(player, entity, phase);
	}

	@Override
	public String getText(int phase, boolean accepted) {
		return phase == -1 ? "text.dawncraft.quest.alchemist.bloodthirst¦text.dawncraft.quest.alchemist.withering¦text.dawncraft.quest.alchemist.endurance" : "text.dawncraft.quest.alchemist";
	}

	@Override
	public QuestType getQuestType(int phase) {
		return (phase == -1) ? QuestType.OPTIONS : QuestType.ACCEPT_QUEST;
	}

	@Override
	public boolean isQuestActive(Mob entity, int phase) {
		return true;
	}

}
