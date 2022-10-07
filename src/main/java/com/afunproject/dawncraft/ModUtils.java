package com.afunproject.dawncraft;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
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

	public static CompoundTag savePosToNBT(Vec3i center) {
		CompoundTag nbt = new CompoundTag();
		nbt.putInt("x", center.getX());
		nbt.putInt("y", center.getY());
		nbt.putInt("z", center.getZ());
		return nbt;
	}

	public static Vec3i readPosFromNBT(CompoundTag nbt, boolean isBlockPos) {
		int x = nbt.getInt("x");
		int y = nbt.getInt("y");
		int z = nbt.getInt("z");
		return isBlockPos ? new BlockPos(x, y, z) : new Vec3i(x, y, z);
	}

	public static boolean isValidResourceLocation(String structure) {
		try {
			new ResourceLocation(structure.replace("#", ""));
			return true;
		} catch (Exception e) {
			return false;
		}
	}

}
