package com.afunproject.dawncraft.entities;

import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;

public abstract class TestEntity extends LivingEntity {

	public static final EntityDataAccessor<Float> atanyaw = SynchedEntityData.defineId(TestEntity.class, EntityDataSerializers.FLOAT);
	public static final EntityDataAccessor<Float> roll = SynchedEntityData.defineId(TestEntity.class, EntityDataSerializers.FLOAT);

	protected TestEntity(EntityType<? extends LivingEntity> p_20966_, Level p_20967_) {
		super(p_20966_, p_20967_);
	}

	@Override
	protected void defineSynchedData(){
		super.defineSynchedData();
		entityData.define(atanyaw, 0f);
		entityData.define(roll, 0f);
	}

	@Override
	public void baseTick() {
		this.getEntityData().set(TestEntity.atanyaw, (float) Math.toDegrees(Math.atan2(this.getX() - xOld, this.getZ() - zOld)));

		if (this.getEntityData().get(TestEntity.atanyaw) < 0) {
			this.getEntityData().set(TestEntity.roll, (0 - this.getEntityData().get(TestEntity.atanyaw)));
		}

		if (this.getEntityData().get(TestEntity.atanyaw) >= 0) {
			this.getEntityData().set(TestEntity.roll, (360 - this.getEntityData().get(TestEntity.atanyaw)));
		}

		super.baseTick();
	}

}
