package com.afunproject.dawncraft.integration.suplementaries;

import com.afunproject.dawncraft.dungeon.item.RebirthStaffItem;
import com.afunproject.dawncraft.entities.RitualItemEntity;
import com.google.common.collect.Lists;
import net.mehvahdjukaar.supplementaries.common.block.tiles.PedestalBlockTile;
import net.mehvahdjukaar.supplementaries.setup.ModRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Direction.Axis;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

import java.util.List;
import java.util.Optional;

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

	public static void startRitual(ItemStack stack, Level level, BlockPos pos) {
		List<BlockPos> pedestals = Lists.newArrayList();
		for (Direction dir : Direction.values()) {
			if (dir.getAxis() != Axis.Y) {
				BlockPos pos0 = pos.relative(dir, 3);
				pedestals.add(pos0);
				BlockState state = level.getBlockState(pos0);
				if (state.is(ModRegistry.PEDESTAL.get())) {
					Optional<PedestalBlockTile> optional = level.getBlockEntity(pos0, ModRegistry.PEDESTAL_TILE.get());
					if (optional.isPresent()) {
						PedestalBlockTile pedestal = optional.get();
						pedestal.getDisplayedItem().shrink(1);
					}
				}
				level.sendBlockUpdated(pos0, state, state, 3);
				LightningBolt bolt = EntityType.LIGHTNING_BOLT.create(level);
				bolt.setPos(new Vec3(pos0.getX(), pos0.getY()+1, pos0.getZ()));
				bolt.setVisualOnly(true);
				level.addFreshEntity(bolt);
			}
		}
		RitualItemEntity entity = new RitualItemEntity(level, stack.copy(), RebirthStaffItem.createPowered(stack.copy()), pos, pedestals);
		stack.shrink(1);
		level.addFreshEntity(entity);
		level.playSound(null, pos.getX(), pos.getY(), pos.getZ(), SoundEvents.END_PORTAL_FRAME_FILL, entity.getSoundSource(), 0.75f, 0.75f);
		level.playSound(null, pos.getX(), pos.getY(), pos.getZ(), SoundEvents.ZOMBIE_VILLAGER_CURE, entity.getSoundSource(), 0.5f, 0.75f);
	}

}
