package com.afunproject.dawncraft.dungeon.block;

import com.afunproject.dawncraft.dungeon.KeyColour;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockBehaviour.BlockStateBase;

public interface LockedBlock {

	KeyColour getColour(Level level, BlockPos pos, BlockStateBase state);

	boolean open(Level level, BlockPos pos, BlockStateBase state, boolean isKey);

}
