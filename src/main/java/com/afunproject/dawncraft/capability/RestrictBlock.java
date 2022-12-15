package com.afunproject.dawncraft.capability;

import com.afunproject.dawncraft.ModUtils;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Mob;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;

public interface RestrictBlock {

	public boolean canRestrict(Mob entity);

	public void applyRestriction(Mob entity);

	public void readNBT(CompoundTag nbt);

	public CompoundTag writeNBT(CompoundTag nbt);

	public static class Implementation implements RestrictBlock {

		protected boolean shouldRestrict;
		protected int timesSpawned = 0;
		protected BlockPos center = null;
		protected int range = -1;

		@Override
		public boolean canRestrict(Mob entity) {
			if (!shouldRestrict) return false;
			if (timesSpawned == 2) return true;
			if(timesSpawned++ >= 1) {
				return true;
			}
			return false;
		}

		@Override
		public void applyRestriction(Mob entity) {
			if (timesSpawned == 1) {
				entity.restrictTo(entity.blockPosition(), 1);
			} else {
				if (center == null) center = entity.blockPosition();
				entity.restrictTo(center, range);
			}
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

	public static class Provider implements ICapabilitySerializable<CompoundTag> {

		private final RestrictBlock impl;

		public Provider() {
			impl = new Implementation();
		}

		@Override
		public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
			return cap == CapabilitiesRegister.RESTRICT_BLOCK ? LazyOptional.of(() -> impl).cast() : LazyOptional.empty();
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
