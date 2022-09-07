package com.afunproject.afptweaks.entities;

import com.afunproject.afptweaks.QuestType;

import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.level.Level;

public class Fallen extends QuestPlayer {

	protected static final EntityDataAccessor<Integer> FADE_TIMER = SynchedEntityData.defineId(Fallen.class, EntityDataSerializers.INT);

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
			if (entityData.get(FADE_TIMER) == 0) setRemoved(RemovalReason.DISCARDED);
		}
	}

	@Override
	public MobType getMobType() {
		return MobType.UNDEAD;
	}

	@Override
	public void completeQuest(boolean isAccepted) {
		entityData.set(FADE_TIMER, 25);
	}

	public int getFadeTimer() {
		return entityData.get(FADE_TIMER);
	}

	@Override
	public boolean canSeeQuest() {
		return entityData.get(FADE_TIMER) == -1;
	}

	@Override
	public QuestType getQuestType() {
		return QuestType.ACKNOWLEDGE;
	}

}
