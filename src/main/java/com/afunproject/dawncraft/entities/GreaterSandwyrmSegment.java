package com.afunproject.dawncraft.entities;

import com.afunproject.dawncraft.integration.humancompanions.entities.KnightPlayer;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraftforge.entity.PartEntity;

public class GreaterSandwyrmSegment extends PartEntity<GreaterSandwyrm> {

	protected static final EntityDataAccessor<Integer> PARENT = SynchedEntityData.defineId(KnightPlayer.class, EntityDataSerializers.INT);

	public GreaterSandwyrmSegment(GreaterSandwyrm parent) {
		super(parent);
	}

	@Override
	protected void defineSynchedData() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void readAdditionalSaveData(CompoundTag p_20052_) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void addAdditionalSaveData(CompoundTag p_20139_) {
		// TODO Auto-generated method stub

	}

}
