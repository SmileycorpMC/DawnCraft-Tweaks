package com.afunproject.dawncraft.dungeon.block.entity;

import com.afunproject.dawncraft.dungeon.block.entity.base.LockableBlockEntityBase;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class DungeonDoorBlockEntity extends LockableBlockEntityBase {

	public DungeonDoorBlockEntity(BlockPos p_155229_, BlockState p_155230_) {
		this(DungeonBlockEntities.DUNGEON_DOOR.get(), p_155229_, p_155230_);
	}

	public DungeonDoorBlockEntity(BlockEntityType<?> p_155228_, BlockPos p_155229_, BlockState p_155230_) {
		super(p_155228_, p_155229_, p_155230_);
	}

	public void open() {
		level.setBlock(worldPosition, Blocks.AIR.defaultBlockState(), 3);
	}

}
