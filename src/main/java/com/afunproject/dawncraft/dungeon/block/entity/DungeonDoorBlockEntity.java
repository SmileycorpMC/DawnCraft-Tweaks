package com.afunproject.dawncraft.dungeon.block.entity;

import com.afunproject.dawncraft.dungeon.block.entity.base.LockableBlockEntityBase;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class DungeonDoorBlockEntity extends LockableBlockEntityBase {

	public DungeonDoorBlockEntity(BlockPos pos, BlockState state) {
		this(DungeonBlockEntities.DUNGEON_DOOR.get(), pos, state);
	}

	public DungeonDoorBlockEntity(BlockEntityType<? extends DungeonDoorBlockEntity> type, BlockPos pos, BlockState state) {
		super(type, pos, state);
	}

	public void open() {
		level.setBlock(worldPosition, Blocks.AIR.defaultBlockState(), 3);
	}

}
