package com.afunproject.dawncraft.dungeon.block;

import com.afunproject.dawncraft.dungeon.block.entity.DungeonBlockEntities;
import com.afunproject.dawncraft.dungeon.block.entity.RedstoneTriggerBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;

import java.util.Optional;
import java.util.Random;

public class RedstoneTriggerBlock extends Block implements EntityBlock {

	public static final BooleanProperty POWERED = BlockStateProperties.POWERED;

	public RedstoneTriggerBlock() {
		super(Properties.of(Material.STONE, MaterialColor.TERRACOTTA_RED).strength(-1, 18000000));
		registerDefaultState(stateDefinition.any().setValue(POWERED, Boolean.valueOf(false)));
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> state) {
		state.add(POWERED);
	}

	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new RedstoneTriggerBlockEntity(pos, state);
	}

	@Override
	public void neighborChanged(BlockState state, Level level, BlockPos pos, Block neighbour, BlockPos neighbour_pos, boolean p_55671_) {
		if (!level.isClientSide) {
			boolean is_powered = state.getValue(POWERED);
			if (!is_powered && level.hasNeighborSignal(pos)) {
				Optional<RedstoneTriggerBlockEntity> optional = level.getBlockEntity(pos, DungeonBlockEntities.REDSTONE_TRIGGER.get());
				if (optional.isPresent()) {
					optional.get().triggerLinkedBlocks();
					level.setBlock(pos, state.cycle(POWERED), 3);
				}
			}
			else if (is_powered &! level.hasNeighborSignal(pos)) {
				level.setBlock(pos, state.cycle(POWERED), 3);
			}
		}
	}

	@Override
	public void tick(BlockState p_55661_, ServerLevel p_55662_, BlockPos p_55663_, Random p_55664_) {
		if (p_55661_.getValue(POWERED) && !p_55662_.hasNeighborSignal(p_55663_)) {
			p_55662_.setBlock(p_55663_, p_55661_.cycle(POWERED), 2);
		}
	}

}
