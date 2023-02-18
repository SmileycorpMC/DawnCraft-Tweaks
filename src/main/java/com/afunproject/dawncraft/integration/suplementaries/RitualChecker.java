package com.afunproject.dawncraft.integration.suplementaries;

import java.util.List;
import java.util.Optional;

import com.google.common.collect.Lists;

import net.mehvahdjukaar.supplementaries.common.block.tiles.PedestalBlockTile;
import net.mehvahdjukaar.supplementaries.setup.ModRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Direction.Axis;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;

public class RitualChecker {

	public static boolean isValid(Level level, BlockPos pos) {
		for (Direction dir : Direction.values()) {
			if (dir.getAxis() != Axis.Y) {
				BlockPos pos0 = pos.relative(dir, 3);
				if (level.getBlockState(pos0).is(ModRegistry.PEDESTAL.get())) {
					Optional<PedestalBlockTile> optional = level.getBlockEntity(pos0, ModRegistry.PEDESTAL_TILE.get());
					if (optional.isPresent()) {
						PedestalBlockTile pedestal = optional.get();
						if (pedestal.getDisplayedItem().is(Items.BLAZE_POWDER)) continue;
					}
				}
				return false;
			}
		}
		return true;
	}

	public static void startRitual(Level level, BlockPos pos) {
		List<BlockPos> pedestals = Lists.newArrayList();
		for (Direction dir : Direction.values()) {
			if (dir.getAxis() != Axis.Y) {
				BlockPos pos0 = pos.relative(dir, 3);
				pedestals.add(pos0);
				if (level.getBlockState(pos0).is(ModRegistry.PEDESTAL.get())) {
					Optional<PedestalBlockTile> optional = level.getBlockEntity(pos0, ModRegistry.PEDESTAL_TILE.get());
					if (optional.isPresent()) {
						PedestalBlockTile pedestal = optional.get();
						pedestal.getDisplayedItem().shrink(1);
					}
				}
			}
		}
	}

}
