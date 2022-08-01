package com.afunproject.afptweaks.dungeon.block.entity.base;

import com.afunproject.afptweaks.dungeon.KeyColour;
import com.afunproject.afptweaks.dungeon.block.entity.interfaces.Lockable;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class LockableBlockEntityBase extends BlockEntity implements Lockable {

	public LockableBlockEntityBase(BlockEntityType<?> p_155228_, BlockPos p_155229_, BlockState p_155230_) {
		super(p_155228_, p_155229_, p_155230_);
	}

	protected KeyColour lock = null;

	@Override
	public KeyColour getLockColour() {
		return lock;
	}

	@Override
	public void setLockColour(KeyColour lock) {
		this.lock = lock;
	}

	@Override
	public void load(CompoundTag tag) {
		super.load(tag);
		if (tag.contains("lock_colour")) lock = KeyColour.valueOf(tag.getString("lock_colour").toUpperCase());
	}

	@Override
	protected void saveAdditional(CompoundTag tag) {
		super.saveAdditional(tag);
		if (lock != null) tag.putString("lock_colour", lock.toString().toLowerCase());
	}

	@Override
	public void unlock() {
		lock = null;
	}

	@Override
	public CompoundTag getUpdateTag() {
		CompoundTag tag = super.getUpdateTag();
		if (lock!=null) tag.putString("lock_colour", lock.toString().toLowerCase());
		return tag;
	}

}
