package com.afunproject.afptweaks.dungeon.block.entity.base;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public abstract class FunctionalBlockEntity extends BlockEntity {

	public FunctionalBlockEntity(BlockEntityType<?> p_155228_, BlockPos p_155229_, BlockState p_155230_) {
		super(p_155228_, p_155229_, p_155230_);
	}

	public abstract boolean canTrigger(TriggerableBlockEntity source);

	public abstract void trigger(TriggerableBlockEntity source);
}
