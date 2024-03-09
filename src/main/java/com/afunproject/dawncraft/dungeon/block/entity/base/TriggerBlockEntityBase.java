package com.afunproject.dawncraft.dungeon.block.entity.base;

import com.afunproject.dawncraft.dungeon.block.entity.interfaces.DungeonTrigger;
import com.afunproject.dawncraft.dungeon.block.entity.interfaces.Functional;
import com.google.common.collect.Sets;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Set;

public class TriggerBlockEntityBase extends BlockEntity implements DungeonTrigger {

	protected final Set<Vec3i> linked_blocks = Sets.newHashSet();
	protected Direction direction = Direction.NORTH;

	public TriggerBlockEntityBase(BlockEntityType<? extends TriggerBlockEntityBase> type, BlockPos pos, BlockState state) {
		super(type, pos, state);
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
		if (tag.contains("direction")) {
			Direction direction =  Direction.byName(tag.getString("direction"));
			if (direction != null) this.direction = direction;
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
		if (direction != null) tag.putString("direction", direction.toString());
	}

	@Override
	public void rotate(Rotation rotation) {
		if (rotation == null) return;
		direction = rotation.rotate(direction);
		Set<Vec3i> cache = Sets.newHashSet(linked_blocks);
		linked_blocks.clear();
		for (Vec3i pos : cache) {
			int x = pos.getX();
			int z = pos.getZ();
			switch (rotation) {
			case CLOCKWISE_180:
				linked_blocks.add(new Vec3i(-pos.getX(), pos.getY(), -pos.getZ()));
				break;
			case CLOCKWISE_90:
				linked_blocks.add(new Vec3i(-z, pos.getY(), x));
				break;
			case COUNTERCLOCKWISE_90:
				linked_blocks.add(new Vec3i(z, pos.getY(), -x));
				break;
			case NONE:
				linked_blocks.add(pos);
				break;
			}
		}
	}

	@Override
	public Direction getDirection() {
		return direction;
	}

	@Override
	public void mirror(Mirror arg0) {
		direction =  direction.getOpposite();
	}

	@Override
	public void setDirection(Direction direction) {
		Rotation rotation = null;
		if (direction == this.direction.getOpposite()) rotation = Rotation.CLOCKWISE_180;
		if ((this.direction == Direction.NORTH && direction == Direction.EAST) || (this.direction == Direction.SOUTH && direction == Direction.WEST)||
				(this.direction == Direction.EAST && direction == Direction.SOUTH) || (this.direction == Direction.WEST && direction == Direction.NORTH)) rotation = Rotation.CLOCKWISE_90;
		if ((this.direction == Direction.NORTH && direction == Direction.WEST) || (this.direction == Direction.SOUTH && direction == Direction.EAST)||
				(this.direction == Direction.EAST && direction == Direction.NORTH) || (this.direction == Direction.WEST && direction == Direction.SOUTH)) rotation = Rotation.COUNTERCLOCKWISE_90;
		rotate(rotation);
	}

}
