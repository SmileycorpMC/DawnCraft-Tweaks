package com.afunproject.packtweaks.dungeon.blocks;

import com.afunproject.packtweaks.dungeon.KeyColour;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Direction.Axis;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class DungeonDoorBlock extends Block implements LockedBlock {

	public static final EnumProperty<Axis> AXIS = EnumProperty.create("axis", Direction.Axis.class, (axis) -> axis != Axis.Y);

	protected static final VoxelShape X = Block.box(5.0D, 0.0D, 0.0D, 11.0D, 16.0D, 16.0D);
	protected static final VoxelShape Z = Block.box(0.0D, 0.0D, 5.0D, 16.0D, 16.0D, 11.0D);

	public DungeonDoorBlock(Properties properties) {
		super(properties);
		registerDefaultState(stateDefinition.any().setValue(AXIS, Axis.X));
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> state) {
		state.add(AXIS);
	}

	@Override
	public VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext ctx) {
		return state.getValue(AXIS) == Axis.Z ? Z : X;
	}

	@Override
	public VoxelShape getBlockSupportShape(BlockState state, BlockGetter level, BlockPos pos) {
		return state.getValue(AXIS) == Axis.Z ? Z : X;
	}

	@Override
	public VoxelShape getVisualShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext ctx) {
		return state.getValue(AXIS) == Axis.Z ? Z : X;
	}
	@Override
	public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext ray) {
		return state.getValue(AXIS) == Axis.Z ? Z : X;
	}

	@Override
	public BlockState getStateForPlacement(BlockPlaceContext context) {
		for (Direction dir : context.getNearestLookingDirections()) {
			if (dir.getAxis() != Axis.Y) return defaultBlockState().setValue(AXIS, dir.getAxis());
		}
		return defaultBlockState();
	}

	@Override
	public KeyColour getColour(Level level, BlockPos pos, BlockStateBase state) {
		return null;
	}

	@Override
	public boolean open(Level level, BlockPos pos, BlockStateBase state, boolean isKey) {
		level.setBlock(pos, Blocks.AIR.defaultBlockState(), UPDATE_ALL);
		for (Direction dir : Direction.values()) {
			if (dir.getAxis() == state.getValue(AXIS)) continue;
			BlockPos new_pos = pos.relative(dir);
			BlockStateBase other_state = level.getBlockState(new_pos);
			if (other_state.getBlock() == state.getBlock()) {
				if (other_state.getValue(AXIS) == state.getValue(AXIS)) {
					((DungeonDoorBlock) other_state.getBlock()).open(level, new_pos, other_state, false);
				}
			}
		}
		return true;
	}

}
