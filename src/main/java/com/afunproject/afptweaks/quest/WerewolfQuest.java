package com.afunproject.afptweaks.quest;

import net.mcreator.simplemobs.entity.PatronwerewolfEntity;
import net.mcreator.simplemobs.init.SimpleMobsModEntities;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity.RemovalReason;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class WerewolfQuest extends SimpleItemQuest {

	public WerewolfQuest() {
		super(new ItemStack(Items.GOLDEN_APPLE));
	}

	@Override
	protected void completeItemQuest(Mob entity, int phase, boolean accepted) {
		spawnWerewolf(entity);
	}

	@Override
	protected String getText() {
		return "text.dawncraft.quest.werewolf";
	}

	@Override
	public void onDeath(Mob entity, DamageSource source) {
		if (source != DamageSource.OUT_OF_WORLD) spawnWerewolf(entity);
	}

	private void spawnWerewolf(Mob entity) {
		PatronwerewolfEntity werewolf = SimpleMobsModEntities.PATRONWEREWOLF.get().create(entity.level);
		werewolf.setPos(entity.position());
		werewolf.setYBodyRot(entity.getYRot());
		werewolf.setYHeadRot(entity.getYRot());
		werewolf.setCustomName(entity.getCustomName());
		werewolf.setPersistenceRequired();
		entity.level.addFreshEntity(werewolf);
		werewolf.playAmbientSound();
		entity.setRemoved(RemovalReason.DISCARDED);
	}

}
