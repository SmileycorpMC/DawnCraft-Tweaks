package com.afunproject.afptweaks.dungeon.block.entity.base;

import java.util.Set;

import com.afunproject.afptweaks.dungeon.block.entity.interfaces.DungeonTrigger;
import com.afunproject.afptweaks.dungeon.block.entity.interfaces.Functional;
import com.google.common.collect.Sets;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class TriggerBlockEntityBase extends BlockEntity implements DungeonTrigger {

	protected final Set<Vec3i> linked_blocks = Sets.newHashSet();

	public TriggerBlockEntityBase(BlockEntityType<?> p_155228_, BlockPos p_155229_, BlockState p_155230_) {
		super(p_155228_, p_155229_, p_155230_);
	}

	@Override
	public void triggerLinkedBlocks() {
		for (Vec3i rpos : linked_blocks ) {
			BlockPos pos = worldPosition.offset(rpos);
			BlockEntity blockEntity = level.getBlockEntity(pos);
			if (blockEntity instanceof Functional) {
				if (((Functional) blockEntity).canTrigger(this)) {
					((Functional) blockEntity).trigger(this);
				}
			}
		}
	}

	@Override
	public void removeLinkedBlock(BlockPos pos) {
		Vec3i rpos = new Vec3i(pos.getX() - worldPosition.getX(), pos.getY() - worldPosition.getY(), pos.getZ() - worldPosition.getZ());
		linked_blocks.remove(rpos);
	}

	@Override
	public void addLinkedBlock(Level level, BlockPos pos) {
		if (level == this.level) {
			Vec3i rpos = new Vec3i(pos.getX() - worldPosition.getX(), pos.getY() - worldPosition.getY(), pos.getZ() - worldPosition.getZ());
			linked_blocks.add(rpos);
		}
	}

	@Override
	public void load(CompoundTag tag) {
		super.load(tag);
		if (tag.contains("linked_blocks")) for (Tag subTag : (ListTag) tag.get("linked_blocks")) {
			CompoundTag posTag = (CompoundTag) subTag;
			linked_blocks.add(new Vec3i(posTag.getInt("x"), posTag.getInt("y"), posTag.getInt("z")));
		}
	}

	@Override
	protected void saveAdditional(CompoundTag tag) {
		super.saveAdditional(tag);
		ListTag list = new ListTag();
		for (Vec3i pos : linked_blocks) {
			CompoundTag posTag = new CompoundTag();
			posTag.putInt("x", pos.getX());
			posTag.putInt("y", pos.getY());
			posTag.putInt("z", pos.getZ());
			list.add(posTag);
		}
		tag.put("linked_blocks", list);
	}


}
