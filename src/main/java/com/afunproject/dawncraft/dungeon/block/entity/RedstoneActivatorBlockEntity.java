package com.afunproject.dawncraft.dungeon.block.entity;

import com.afunproject.dawncraft.dungeon.block.DungeonBlocks;
import com.afunproject.dawncraft.dungeon.block.RedstoneActivatorBlock;
import com.afunproject.dawncraft.dungeon.block.entity.base.CamouflagedFunctionalBlockEntity;
import com.afunproject.dawncraft.dungeon.block.entity.base.TriggerBlockEntityBase;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class RedstoneActivatorBlockEntity extends CamouflagedFunctionalBlockEntity {

	protected boolean is_active = false;
	protected int signal_length = 1;

	public RedstoneActivatorBlockEntity(BlockPos pos, BlockState state) {
		this(DungeonBlockEntities.REDSTONE_ACTIVATOR.get(), pos, state);
	}

	public RedstoneActivatorBlockEntity(BlockEntityType<? extends RedstoneActivatorBlockEntity> type, BlockPos pos, BlockState state) {
		super(type, pos, state);
	}

	@SuppressWarnings("deprecation")
	@Override
	public void trigger(TriggerBlockEntityBase source) {
		if (is_active && level instanceof ServerLevel) {
			getBlockState().getBlock().tick(getBlockState(), (ServerLevel)level, worldPosition, level.random);
			is_active = false;
		} else {
			level.setBlock(worldPosition, getBlockState().setValue(RedstoneActivatorBlock.POWERED, true), 3);
			level.updateNeighborsAt(worldPosition, DungeonBlocks.REDSTONE_ACTIVATOR.get());
			if (signal_length >= 1) level.scheduleTick(worldPosition, DungeonBlocks.REDSTONE_ACTIVATOR.get(), signal_length-1);
			is_active = true;
		}
	}

	@Override
	public boolean canTrigger(TriggerBlockEntityBase source) {
		return !is_active || signal_length < 1;
	}

	public void setTriggered() {
		is_active = false;
	}

	@Override
	public void load(CompoundTag tag) {
		if (tag.contains("signal_length")) signal_length = tag.getInt("signal_length");
		super.load(tag);
	}

	@Override
	protected void saveAdditional(CompoundTag tag) {
		super.saveAdditional(tag);
		tag.putInt("signal_length", signal_length);
	}

	public void setSignalLength(int signal_length) {
		this.signal_length = signal_length;
	}

	public int getSignalLength() {
		return signal_length;
	}

}
