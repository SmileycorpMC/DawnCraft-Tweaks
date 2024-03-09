package com.afunproject.dawncraft.dungeon.block.entity.interfaces;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

public interface DungeonTrigger extends RotatableBlockEntity {

	void triggerLinkedBlocks();

	void removeLinkedBlock(BlockPos pos);

	void addLinkedBlock(Level level, BlockPos pos);

}
