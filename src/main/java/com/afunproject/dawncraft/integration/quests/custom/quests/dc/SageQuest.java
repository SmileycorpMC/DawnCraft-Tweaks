package com.afunproject.dawncraft.integration.quests.custom.quests.dc;

import com.afunproject.dawncraft.capability.DCCapabilities;
import com.afunproject.dawncraft.capability.SageQuestTracker;
import com.afunproject.dawncraft.integration.quests.custom.QuestEntity;
import com.afunproject.dawncraft.integration.quests.custom.QuestType;
import com.afunproject.dawncraft.integration.quests.custom.entity.QuestEntityBase;
import com.afunproject.dawncraft.integration.quests.custom.quests.Quest;
import net.mcreator.simplemobs.init.SimpleMobsModItems;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.util.LazyOptional;

public class SageQuest extends Quest {

	protected final int end_phase = 3;

	public SageQuest() {
		super((player, entity, phase, isTest)->{
			LazyOptional<SageQuestTracker> optional = player.getCapability(DCCapabilities.SAGE_QUEST_TRACKER);
			if (optional.isPresent()) return phase == 2 && optional.resolve().get().getCheckedCount() >= 10;
			return false;
		});
	}

	@Override
	public void completeQuest(Player quest_completer, Mob entity, int phase, boolean accepted) {
		if (phase == end_phase) {
			if (entity instanceof QuestEntityBase) {
				((QuestEntityBase) entity).setDespawnable(true);
			}
			LazyOptional<SageQuestTracker> optional = quest_completer.getCapability(DCCapabilities.SAGE_QUEST_TRACKER);
			if (optional.isPresent()) optional.resolve().get().setActive(false);
			this.giveItem(quest_completer, new ItemStack(SimpleMobsModItems.SHINY_KEY.get()));
			setPhase(entity, phase + 1);
		}
		else if (phase == 1 && accepted) {
			setPhase(entity, phase + 1);
			LazyOptional<SageQuestTracker> optional = quest_completer.getCapability(DCCapabilities.SAGE_QUEST_TRACKER);
			if (optional.isPresent()) optional.resolve().get().setActive(true);
		}
		if (entity instanceof QuestEntity) ((QuestEntity) entity).setQuestText(getText(((QuestEntity) entity).getQuestPhase(), accepted));
	}

	@Override
	public String getText(int phase, boolean accepted) {
		return "text.dawncraft.quest.sage" + phase;
	}

	@Override
	public QuestType getQuestType(int phase) {
		if (phase == end_phase) return QuestType.AUTO_CLOSE;
		if (phase % 2 == 1) return QuestType.ACCEPT_QUEST;
		return QuestType.DENY;
	}

	@Override
	public boolean isQuestActive(Mob entity, int phase) {
		return phase < end_phase;
	}

}
