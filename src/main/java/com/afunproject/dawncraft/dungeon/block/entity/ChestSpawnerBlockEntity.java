package com.afunproject.dawncraft.dungeon.block.entity;

import com.afunproject.dawncraft.Constants;
import com.afunproject.dawncraft.ModUtils;
import com.afunproject.dawncraft.dungeon.block.ChestSpawnerBlock;
import com.afunproject.dawncraft.dungeon.block.entity.base.CamouflagedFunctionalBlockEntity;
import com.afunproject.dawncraft.dungeon.block.entity.base.TriggerBlockEntityBase;
import com.afunproject.dawncraft.dungeon.block.entity.interfaces.SingleUse;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ChestMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

import java.util.Optional;

public class ChestSpawnerBlockEntity extends CamouflagedFunctionalBlockEntity implements Container, MenuProvider, SingleUse {

	protected final NonNullList<ItemStack> inventory = NonNullList.withSize(27, ItemStack.EMPTY);

	protected boolean hasTriggered = false;

	public ChestSpawnerBlockEntity(BlockPos pos, BlockState state) {
		this(DungeonBlockEntities.CHEST_SPAWNER.get(), pos, state);
	}

	public ChestSpawnerBlockEntity(BlockEntityType<? extends ChestSpawnerBlockEntity> type, BlockPos pos, BlockState state) {
		super(type, pos, state);
	}

	@Override
	public boolean canTrigger(TriggerBlockEntityBase source) {
		return !hasTriggered;
	}

	@Override
	public void trigger(TriggerBlockEntityBase source) {
		BlockState state = Blocks.CHEST.defaultBlockState().setValue(ChestBlock.FACING, getBlockState().getValue(ChestSpawnerBlock.FACING));
		BlockPos pos = worldPosition.above();
		BlockState oldState = level.getBlockState(pos);
		if (ModUtils.isWater(oldState)) state.setValue(BlockStateProperties.WATERLOGGED, true);
		level.setBlock(pos, state, 3);
		Optional<ChestBlockEntity> optional = level.getBlockEntity(pos, BlockEntityType.CHEST);
		if (optional.isPresent()) {
			ChestBlockEntity entity = optional.get();
			for (int slot = 0; slot < inventory.size(); slot++) entity.setItem(slot, inventory.get(slot));
		}
		hasTriggered = true;
	}

	@Override
	public void load(CompoundTag tag) {
		if (tag.contains("Items")) ContainerHelper.loadAllItems(tag, inventory);
		if (tag.contains("hasTriggered")) hasTriggered = tag.getBoolean("hasTriggered");
		super.load(tag);
	}

	@Override
	protected void saveAdditional(CompoundTag tag) {
		super.saveAdditional(tag);
		ContainerHelper.saveAllItems(tag, inventory);
		tag.putBoolean("hasTriggered", hasTriggered);
	}

	@Override
	public AbstractContainerMenu createMenu(int id, Inventory inventory, Player player) {
		return ChestMenu.threeRows(id, inventory, this);
	}

	@Override
	public Component getDisplayName() {
		return new TranslatableComponent("tile." + Constants.MODID + ".chest_spawner");
	}

	@Override
	public void clearContent() {
		inventory.clear();
	}

	@Override
	public int getContainerSize() {
		return 27;
	}

	@Override
	public boolean isEmpty() {
		return inventory.isEmpty();
	}

	@Override
	public ItemStack getItem(int slot) {
		return inventory.get(slot);
	}

	@Override
	public ItemStack removeItem(int slot, int count) {
		ItemStack stack = inventory.get(slot);
		stack.shrink(count);
		return stack;
	}

	@Override
	public ItemStack removeItemNoUpdate(int slot) {
		return inventory.set(slot, ItemStack.EMPTY);
	}

	@Override
	public void setItem(int slot, ItemStack stack) {
		inventory.set(slot, stack);
	}

	@Override
	public boolean stillValid(Player player) {
		return player.isCreative();
	}

	@Override
	public void reset() {
		hasTriggered = false;
	}

	@Override
	public boolean hasBeenUsed() {
		return hasTriggered;
	}

}
