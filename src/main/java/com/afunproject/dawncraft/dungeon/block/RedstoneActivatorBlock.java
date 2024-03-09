package com.afunproject.dawncraft.dungeon.block;

import com.afunproject.dawncraft.dungeon.block.entity.DungeonBlockEntities;
import com.afunproject.dawncraft.dungeon.block.entity.RedstoneActivatorBlockEntity;
import com.afunproject.dawncraft.dungeon.item.DungeonItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
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
import net.minecraft.world.phys.BlockHitResult;

import javax.annotation.Nullable;
import java.util.Optional;
import java.util.Random;

public class RedstoneActivatorBlock extends Block implements EntityBlock {

	public static final BooleanProperty POWERED = BlockStateProperties.POWERED;

	public RedstoneActivatorBlock() {
		super(Properties.of(Material.STONE, MaterialColor.TERRACOTTA_RED).strength(-1, 18000000));
		registerDefaultState(stateDefinition.any().setValue(POWERED, Boolean.valueOf(false)));
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> state) {
		state.add(POWERED);
	}

	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new RedstoneActivatorBlockEntity(pos, state);
	}

	@Override
	public boolean isSignalSource(BlockState p_55138_) {
		return true;
	}

	@Override
	public int getDirectSignal(BlockState p_55127_, BlockGetter p_55128_, BlockPos p_55129_, Direction p_55130_) {
		return p_55127_.getSignal(p_55128_, p_55129_, p_55130_);
	}

	@Override
	public int getSignal(BlockState p_55101_, BlockGetter p_55102_, BlockPos p_55103_, Direction p_55104_) {
		return p_55101_.getValue(POWERED) ? 15 : 0;
	}

	@Override
	public void tick(BlockState state, ServerLevel level, BlockPos pos, Random rand) {
		if (state.getValue(POWERED)) {
			level.setBlock(pos, state.cycle(POWERED), 3);
			level.updateNeighborsAt(pos, this);
			Optional<RedstoneActivatorBlockEntity> optional = level.getBlockEntity(pos, DungeonBlockEntities.REDSTONE_ACTIVATOR.get());
			if (optional.isPresent()) optional.get().setTriggered();
		}
	}

	@Override
	public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult p_51536_) {
		if (!player.isCreative()) return InteractionResult.PASS;
		if (level.isClientSide) return InteractionResult.SUCCESS;
		ItemStack stack = player.getItemInHand(hand);
		if (stack.getItem() == DungeonItems.DUNGEON_CONFIGURATOR.get() || hand == InteractionHand.OFF_HAND) return InteractionResult.PASS;
		/*MenuProvider menuprovider = getMenuProvider(state, level, pos);
		if (menuprovider != null) {
			player.openMenu(menuprovider);
		}*/
		Optional<RedstoneActivatorBlockEntity> optional = level.getBlockEntity(pos, DungeonBlockEntities.REDSTONE_ACTIVATOR.get());
		if (optional.isPresent()) {
			RedstoneActivatorBlockEntity entity = optional.get();
			if (entity.getSignalLength() < 1) {
				entity.setSignalLength(1);
				player.sendMessage(new TranslatableComponent("message.dawncraft.singlepulse"), null);
			} else {
				entity.setSignalLength(0);
				player.sendMessage(new TranslatableComponent("message.dawncraft.toggle"), null);
			}
		}
		return InteractionResult.SUCCESS;
	}

	@Override
	@Nullable
	public MenuProvider getMenuProvider(BlockState state, Level level, BlockPos pos) {
		BlockEntity blockentity = level.getBlockEntity(pos);
		return blockentity instanceof MenuProvider ? (MenuProvider)blockentity : null;
	}

}
