package com.afunproject.afptweaks.quest;

import com.afunproject.afptweaks.QuestType;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;

public abstract class Quest {

	protected ResourceLocation registry;
	protected final QuestCondition[] conditions;

	public Quest(QuestCondition... conditions) {
		this.conditions = conditions;
	}

	void setRegistryName(ResourceLocation registry) {
		this.registry = registry;
	}

	public ResourceLocation getRegistryName() {
		return registry;
	}

	public final boolean isQuestComplete(Player player, Mob entity, int phase) {
		for (QuestCondition condition : conditions) if (!condition.apply(player, entity, phase)) return false;
		return true;
	}

	protected void setPhase(Mob entity, int phase) {
		if (entity instanceof QuestEntity) {
			((QuestEntity) entity).setQuestPhase(phase);
		}
	}

	public void onDeath(Mob entity, DamageSource source) {}

	public abstract void completeQuest(Mob entity, int phase, boolean accepted);

	public abstract String getText(int phase, boolean accepted);

	public abstract QuestType getQuestType(int phase);

	public abstract boolean isQuestActive(Mob entity, int phase);

}
