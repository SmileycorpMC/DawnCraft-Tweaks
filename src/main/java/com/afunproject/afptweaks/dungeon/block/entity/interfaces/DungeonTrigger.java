package com.afunproject.afptweaks.dungeon.block.entity.interfaces;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

public interface DungeonTrigger extends RotatableBlockEntity {

	public void triggerLinkedBlocks();

	public void removeLinkedBlock(BlockPos pos);

	public void addLinkedBlock(Level level, BlockPos pos);

}
