package com.afunproject.dawncraft.integration.quests.custom.entity;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class Fallen extends QuestPlayer {

	protected static final EntityDataAccessor<Integer> FADE_TIMER = SynchedEntityData.defineId(Fallen.class, EntityDataSerializers.INT);

	public static int FADE_LENGTH = 60;

	private ItemStack drop;

	protected Fallen(EntityType<? extends Mob> type, Level level) {
		super(type, level);
	}

	@Override
	protected void defineSynchedData(){
		super.defineSynchedData();
		entityData.define(FADE_TIMER, -1);
	}

	@Override
	public void tick() {
		setNoGravity(true);
		noPhysics = true;
		super.tick();
		noPhysics = false;
		if (entityData.get(FADE_TIMER) > -1) {
			entityData.set(FADE_TIMER, entityData.get(FADE_TIMER) -1);
			if (entityData.get(FADE_TIMER) == 0) {
				setRemoved(RemovalReason.DISCARDED);
				playSound(SoundEvents.ZOMBIE_VILLAGER_CONVERTED, 1f, 1f);
				if (drop != null) spawnAtLocation(drop, 0.5f);
			}
			if (level.isClientSide) {
				for (int i = 0; i < random.nextInt(5)+2; i++) {
					level.addParticle(ParticleTypes.SOUL_FIRE_FLAME, xo+random.nextFloat()-0.5f, yo+random.nextFloat(), zo+random.nextFloat()-0.5f, random.nextFloat()-0.5f, random.nextFloat()-0.5f, 1-random.nextFloat()-0.5f);
				}
			}
		}
	}

	@Override
	public MobType getMobType() {
		return MobType.UNDEAD;
	}

	public int getFadeTimer() {
		return entityData.get(FADE_TIMER);
	}

	@Override
	public boolean canSeeQuest() {
		return entityData.get(FADE_TIMER) == -1;
	}

	public void startFading(ItemStack drop) {
		entityData.set(FADE_TIMER, 60);
		this.drop = drop;
	}

}
