package com.afunproject.dawncraft.dungeon.block;

import com.afunproject.dawncraft.dungeon.KeyColour;
import com.afunproject.dawncraft.dungeon.block.entity.DungeonDoorBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Direction.Axis;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class DungeonDoorBlock extends Block implements LockedBlock, EntityBlock {

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
		BlockEntity blockEntity = level.getBlockEntity(pos);
		if (blockEntity instanceof DungeonDoorBlockEntity) return ((DungeonDoorBlockEntity) blockEntity).getLockColour();
		return null;
	}

	@Override
	public boolean open(Level level, BlockPos pos, BlockStateBase state, boolean isKey) {
		if (level.getBlockEntity(pos) instanceof DungeonDoorBlockEntity) {
			((DungeonDoorBlockEntity) level.getBlockEntity(pos)).unlock();
			((DungeonDoorBlockEntity) level.getBlockEntity(pos)).open();
		}
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

	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new DungeonDoorBlockEntity(pos, state);
	}

	@Override
	public BlockState rotate(BlockState state, Rotation rotation) {
		switch(rotation) {
		case COUNTERCLOCKWISE_90:
		case CLOCKWISE_90:
			switch((Direction.Axis)state.getValue(AXIS)) {
			case X:
				return state.setValue(AXIS, Direction.Axis.Z);
			case Z:
				return state.setValue(AXIS, Direction.Axis.X);
			default:
				return state;
			}
		default:
			return state;
		}
	}
}
