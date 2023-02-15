package com.afunproject.dawncraft.integration.quests.custom.quests;

import com.afunproject.dawncraft.ModDefinitions;
import com.afunproject.dawncraft.effects.DawnCraftEffects;
import com.afunproject.dawncraft.integration.quests.custom.QuestEntity;
import com.afunproject.dawncraft.integration.quests.custom.QuestType;
import com.afunproject.dawncraft.integration.quests.custom.conditions.AndCondition;
import com.afunproject.dawncraft.integration.quests.custom.conditions.TagCondition;
import com.afunproject.dawncraft.integration.quests.custom.entity.QuestEntityBase;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity.RemovalReason;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import tictim.paraglider.contents.Contents;

public class WitchQuest extends Quest {

	protected final int end_phase;

	public WitchQuest() {
		super(new AndCondition((player, entity, phase, isTest)->phase==2), new TagCondition(ModDefinitions.getResource("bat_wing")), new TagCondition(ModDefinitions.getResource("bark")),
				new TagCondition(ModDefinitions.getResource("spider_eye")), new TagCondition(ModDefinitions.getResource("honeycomb")),
				new TagCondition(ModDefinitions.getResource("rabbit_foot")), new TagCondition(ModDefinitions.getResource("bear_pelt")),
				new TagCondition(ModDefinitions.getResource("emerald")));
		end_phase = 3;
	}

	@Override
	public void completeQuest(Player quest_completer, Mob entity, int phase, boolean accepted) {
		if (phase == end_phase) {
			if (entity instanceof QuestEntityBase) {
				((QuestEntityBase) entity).setDespawnable(true);
			}
			ItemStack stack = new ItemStack(Contents.SPIRIT_ORB.get(), 4);
			giveItem(quest_completer, stack);
			quest_completer.addEffect(new MobEffectInstance(MobEffects.BLINDNESS, 20));
			entity.setRemoved(RemovalReason.DISCARDED);
		}
		else if (phase == 1 && accepted) {
			setPhase(entity, phase + 1);
		}
		if (entity instanceof QuestEntity) ((QuestEntity) entity).setQuestText(getText(((QuestEntity) entity).getQuestPhase(), accepted));
	}

	@Override
	public void onHurt(Mob entity, DamageSource source) {
		entity.heal(entity.getMaxHealth());
		if (source.getEntity() instanceof LivingEntity && source.getEntity().isAlive()) {
			((LivingEntity) source.getEntity()).addEffect(new MobEffectInstance(DawnCraftEffects.FROGFORM.get(), 1200));
		}
	}

	@Override
	public void onDeath(Mob entity, DamageSource source) {
		entity.level.addFreshEntity(new ItemEntity(entity.level, entity.getX(), entity.getY(), entity.getZ(), new ItemStack(Contents.SPIRIT_ORB.get())));
		((LivingEntity) source.getEntity()).addEffect(new MobEffectInstance(DawnCraftEffects.FROGFORM.get(), 7200));
	}

	@Override
	public String getText(int phase, boolean accepted) {
		return "text.dawncraft.quest.witch" + phase;
	}

	@Override
	public QuestType getQuestType(int phase) {
		if (phase == end_phase) return QuestType.AUTO_CLOSE;
		if (phase % 2 == 1) return QuestType.ACCEPT_QUEST;
		return QuestType.DENY;
	}

	@Override
	public boolean isQuestActive(Mob entity, int phase) {
		return true;
	}

}
