package com.afunproject.afptweaks.dungeon.block.entity.base;

import java.util.Set;

import com.google.common.collect.Sets;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class TriggerableBlockEntity extends BlockEntity {

	protected final Set<BlockPos> linked_blocks = Sets.newHashSet();

	public TriggerableBlockEntity(BlockEntityType<?> p_155228_, BlockPos p_155229_, BlockState p_155230_) {
		super(p_155228_, p_155229_, p_155230_);
	}

	public void triggerLinkedBlocks() {
		for (BlockPos pos : linked_blocks ) {
			BlockEntity blockEntity = level.getBlockEntity(pos);
			if (blockEntity instanceof FunctionalBlockEntity) {
				if (((FunctionalBlockEntity) blockEntity).canTrigger(this)) {
					System.out.println("triggering " + pos);
					((FunctionalBlockEntity) blockEntity).trigger(this);
				}
			}
		}
	}

	public void removeLinkedBlock(BlockPos pos) {
		linked_blocks.remove(pos);
	}

	public void addLinkedBlock(Level level, BlockPos pos) {
		if (level == this.level) {
			linked_blocks.add(pos);
			System.out.println("linked " + pos);
		}
	}

	@Override
	public void load(CompoundTag tag) {
		super.load(tag);
		if (tag.contains("linked_blocks")) for (Tag subTag : (ListTag) tag.get("linked_blocks")) {
			CompoundTag posTag = (CompoundTag) subTag;
			linked_blocks.add(new BlockPos(posTag.getInt("x"), posTag.getInt("y"), posTag.getInt("z")));
		}
	}

	@Override
	protected void saveAdditional(CompoundTag tag) {
		super.saveAdditional(tag);
		ListTag list = new ListTag();
		for (BlockPos pos : linked_blocks) {
			CompoundTag posTag = new CompoundTag();
			posTag.putInt("x", pos.getX());
			posTag.putInt("y", pos.getX());
			posTag.putInt("z", pos.getX());
			list.add(posTag);
		}
		tag.put("linked_blocks", list);
	}


}
