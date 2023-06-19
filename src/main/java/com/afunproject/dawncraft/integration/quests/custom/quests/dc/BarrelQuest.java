package com.afunproject.dawncraft.integration.quests.custom.quests.dc;

import com.afunproject.dawncraft.integration.quests.custom.QuestType;
import com.afunproject.dawncraft.integration.quests.custom.quests.Quest;
import net.mcreator.simplemobs.entity.JosslynMCEntity;
import net.mcreator.simplemobs.init.SimpleMobsModEntities;
import net.minecraft.world.entity.Entity.RemovalReason;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;

public class BarrelQuest extends Quest {

	@Override
	public void completeQuest(Player quest_completer, Mob entity, int phase, boolean accepted) {
		if (phase == 0) setPhase(entity, 1);
		if (accepted) {
			JosslynMCEntity newentity = SimpleMobsModEntities.JOSSLYN_MC.get().create(entity.level);
			newentity.setPos(entity.position());
			newentity.setYBodyRot(entity.getYRot());
			newentity.setYHeadRot(entity.getYRot());
			newentity.setCustomName(entity.getCustomName());
			newentity.setPersistenceRequired();
			entity.level.addFreshEntity(newentity);
			entity.setRemoved(RemovalReason.DISCARDED);
		}
	}

	@Override
	public String getText(int phase, boolean accepted) {
		return "text.dawncraft.quest.barrel" + (phase < 1 ? "1" : "2");
	}

	@Override
	public QuestType getQuestType(int phase) {
		return QuestType.ACCEPT_QUEST;
	}

	@Override
	public boolean isQuestActive(Mob entity, int phase) {
		return true;
	}

}
