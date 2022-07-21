package com.afunproject.afptweaks;

import net.minecraft.core.BlockPos;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.material.Material;

public class ModUtils {

	public static boolean isWater(BlockState oldState) {
		Block block = oldState.getBlock();
		if (oldState.getMaterial() == Material.WATER) return true;
		else if (block instanceof SimpleWaterloggedBlock) return (oldState.getValue(BlockStateProperties.WATERLOGGED));
		return oldState.getFluidState().m_205070_(FluidTags.WATER);
	}

	public static String getPosString(BlockPos pos) {
		return "("+pos.getX() + ", "+pos.getY()+", "+pos.getZ()+")";
	}

}
