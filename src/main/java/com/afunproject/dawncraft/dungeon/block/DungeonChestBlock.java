package com.afunproject.dawncraft.dungeon.block;

import com.afunproject.dawncraft.dungeon.KeyColour;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.stats.Stats;
import net.minecraft.world.*;
import net.minecraft.world.entity.monster.piglin.PiglinAi;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.Random;

public class DungeonChestBlock extends BaseEntityBlock implements LockedBlock, SimpleWaterloggedBlock {

	public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
	public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;

	protected static final VoxelShape AABB = Block.box(1.0D, 0.0D, 1.0D, 15.0D, 14.0D, 15.0D);

	public DungeonChestBlock() {
		super(Properties.of(Material.STONE, MaterialColor.TERRACOTTA_BROWN).strength(-1, 18000000));
		registerDefaultState(stateDefinition.any().setValue(FACING, Direction.NORTH)
				.setValue(WATERLOGGED, Boolean.valueOf(false)));
	}

	@Override
	public RenderShape getRenderShape(BlockState p_51567_) {
		return RenderShape.ENTITYBLOCK_ANIMATED;
	}

	@Override
	@SuppressWarnings("deprecation")
	public BlockState updateShape(BlockState state, Direction facing, BlockState neighbour, LevelAccessor level, BlockPos neighbour_pos, BlockPos pos) {
		if (state.getValue(WATERLOGGED)) {
			level.scheduleTick(neighbour_pos, Fluids.WATER, Fluids.WATER.getTickDelay(level));
		}
		return super.updateShape(state, facing, neighbour, level, neighbour_pos, pos);
	}

	@Override
	public VoxelShape getShape(BlockState p_51569_, BlockGetter p_51570_, BlockPos p_51571_, CollisionContext p_51572_) {
		return AABB;
	}

	@Override
	public BlockState getStateForPlacement(BlockPlaceContext p_51493_) {
		Direction direction = p_51493_.getHorizontalDirection().getOpposite();
		FluidState fluidstate = p_51493_.getLevel().getFluidState(p_51493_.getClickedPos());
		return defaultBlockState().setValue(FACING, direction).setValue(WATERLOGGED, Boolean.valueOf(fluidstate.getType() == Fluids.WATER));
	}

	@Override
	@SuppressWarnings("deprecation")
	public FluidState getFluidState(BlockState state) {
		return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
	}

	@Override
	@SuppressWarnings("deprecation")
	public void onRemove(BlockState state, Level level, BlockPos pos, BlockState neighbour, boolean p_51542_) {
		if (!state.is(neighbour.getBlock())) {
			BlockEntity blockentity = level.getBlockEntity(pos);
			if (blockentity instanceof Container) {
				Containers.dropContents(level, pos, (Container)blockentity);
				level.updateNeighbourForOutputSignal(pos, this);
			}
			super.onRemove(state, level, pos, neighbour, p_51542_);
		}
	}

	@Override
	public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit_result) {
		if (level.isClientSide) {
			return InteractionResult.SUCCESS;
		} else {
			MenuProvider menuprovider = getMenuProvider(state, level, pos);
			if (menuprovider != null) {
				player.openMenu(menuprovider);
				player.awardStat(Stats.CUSTOM.get(Stats.OPEN_CHEST));
				PiglinAi.angerNearbyPiglins(player, true);
			}
			return InteractionResult.CONSUME;
		}
	}

	@Override
	public boolean hasAnalogOutputSignal(BlockState p_51520_) {
		return true;
	}

	@Override
	public int getAnalogOutputSignal(BlockState p_51527_, Level p_51528_, BlockPos p_51529_) {
		//Optional<DungeonChest>
		//return AbstractContainerMenu.getRedstoneSignalFromContainer(getContainer(this, p_51527_, p_51528_, p_51529_, false));
		return 0;
	}

	@Override
	public boolean isPathfindable(BlockState p_51522_, BlockGetter p_51523_, BlockPos p_51524_, PathComputationType p_51525_) {
		return false;
	}

	@Override
	public void tick(BlockState p_153059_, ServerLevel p_153060_, BlockPos p_153061_, Random p_153062_) {
		BlockEntity blockentity = p_153060_.getBlockEntity(p_153061_);
		if (blockentity instanceof ChestBlockEntity) {
			((ChestBlockEntity)blockentity).recheckOpen();
		}

	}

	@Override
	public BlockEntity newBlockEntity(BlockPos p_153215_, BlockState p_153216_) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public KeyColour getColour(Level level, BlockPos pos, BlockStateBase state) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean open(Level level, BlockPos pos, BlockStateBase state, boolean isKey) {
		// TODO Auto-generated method stub
		return false;
	}


}
