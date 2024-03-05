package com.afunproject.dawncraft.capability;

import com.afunproject.dawncraft.ModUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.MoveTowardsRestrictionGoal;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;

public interface RestrictBlock {

	boolean canRestrict(Mob entity);

	void applyRestriction(PathfinderMob entity);

	void readNBT(CompoundTag nbt);

	CompoundTag writeNBT(CompoundTag nbt);

	class Implementation implements RestrictBlock {

		protected boolean shouldRestrict;
		protected int timesSpawned = 0;
		protected BlockPos center = null;
		protected int range = -1;

		@Override
		public boolean canRestrict(Mob entity) {
			return shouldRestrict;
		}

		@Override
		public void applyRestriction(PathfinderMob entity) {
			if (center == null) center = entity.blockPosition();
			entity.restrictTo(center, range);
			entity.goalSelector.addGoal(5, new MoveTowardsRestrictionGoal(entity, 1.0D));
		}

		@Override
		public void readNBT(CompoundTag nbt) {
			if (nbt.contains("shouldRestrict")) shouldRestrict = nbt.getBoolean("shouldRestrict");
			if (nbt.contains("timesSpawned")) timesSpawned = nbt.getInt("timesSpawned");
			if (nbt.contains("center")) center = (BlockPos) ModUtils.readPosFromNBT((CompoundTag) nbt.get("center"), true);
			if (nbt.contains("range")) range = nbt.getInt("range");
		}

		@Override
		public CompoundTag writeNBT(CompoundTag nbt) {
			if (shouldRestrict) nbt.putBoolean("shouldRestrict", shouldRestrict);
			if (timesSpawned != 0) nbt.putInt("timesSpawned", timesSpawned);
			if (center != null) nbt.put("center", ModUtils.savePosToNBT(center));
			if (range >= 0) nbt.putInt("range", range);
			return nbt;
		}

	}

	class Provider implements ICapabilitySerializable<CompoundTag> {

		private final RestrictBlock impl;

		public Provider() {
			impl = new Implementation();
		}

		@Override
		public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
			return cap == DCCapabilities.RESTRICT_BLOCK ? LazyOptional.of(() -> impl).cast() : LazyOptional.empty();
		}

		@Override
		public CompoundTag serializeNBT() {
			return impl.writeNBT(new CompoundTag());
		}

		@Override
		public void deserializeNBT(CompoundTag nbt) {
			impl.readNBT(nbt);
		}

	}

}
