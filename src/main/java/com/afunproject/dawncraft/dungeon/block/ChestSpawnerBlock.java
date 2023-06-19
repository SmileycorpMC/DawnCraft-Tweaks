package com.afunproject.dawncraft.dungeon.block;

import com.afunproject.dawncraft.dungeon.block.entity.ChestSpawnerBlockEntity;
import com.afunproject.dawncraft.dungeon.item.DungeonItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;
import net.minecraft.world.phys.BlockHitResult;

import javax.annotation.Nullable;

public class ChestSpawnerBlock extends HorizontalDirectionalBlock implements EntityBlock {

	public ChestSpawnerBlock() {
		super(Properties.of(Material.STONE, MaterialColor.TERRACOTTA_RED).strength(-1, 18000000));
		registerDefaultState(stateDefinition.any().setValue(FACING, Direction.NORTH));
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> state) {
		state.add(FACING);
	}

	@Override
	public BlockState getStateForPlacement(BlockPlaceContext ctx) {
		return this.defaultBlockState().setValue(FACING, ctx.getHorizontalDirection().getOpposite());
	}

	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new ChestSpawnerBlockEntity(pos, state);
	}

	@Override
	@Nullable
	public MenuProvider getMenuProvider(BlockState state, Level level, BlockPos pos) {
		BlockEntity blockentity = level.getBlockEntity(pos);
		return blockentity instanceof MenuProvider ? (MenuProvider)blockentity : null;
	}

	@Override
	public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult p_51536_) {
		if (!player.isCreative()) return InteractionResult.PASS;
		if (level.isClientSide) return InteractionResult.SUCCESS;
		ItemStack stack = player.getItemInHand(hand);
		if (stack.getItem() == DungeonItems.DUNGEON_CONFIGURATOR.get() || hand == InteractionHand.OFF_HAND) return InteractionResult.PASS;
		MenuProvider menuprovider = getMenuProvider(state, level, pos);
		if (menuprovider != null) {
			player.openMenu(menuprovider);
		}
		return InteractionResult.CONSUME;
	}

}
