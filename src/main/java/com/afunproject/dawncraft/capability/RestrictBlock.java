package com.afunproject.dawncraft.capability;

import com.afunproject.dawncraft.ModUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;

public interface RestrictBlock {

	boolean shouldRestrict(Mob entity);
	
	BlockPos getCenter(Mob entity);
	
	int getRange();
	
	void readNBT(CompoundTag nbt);
	
	CompoundTag writeNBT(CompoundTag nbt);

	class Implementation implements RestrictBlock {

		protected boolean shouldRestrict;
		protected int timesSpawned = 0;
		protected BlockPos center = null;
		protected int range = -1;

		@Override
		public boolean shouldRestrict(Mob entity) {
			BlockPos pos = getCenter(entity);
			if (entity == null || pos == null || range < 0) return false;
			return shouldRestrict && entity.getTarget() == null && entity.distanceToSqr(Vec3.atCenterOf(pos)) >= (range * range);
		}
		
		@Override
		public BlockPos getCenter(Mob entity) {
			if (range >= 0 && center == null) center = entity.blockPosition();
			return center;
		}
		
		@Override
		public int getRange() {
			return range;
		}
		
		@Override
		public void readNBT(CompoundTag nbt) {
			if (nbt.contains("shouldRestrict")) shouldRestrict = nbt.getBoolean("shouldRestrict");
			if (nbt.contains("timesSpawned")) timesSpawned = nbt.getInt("timesSpawned");
			if (nbt.contains("center")) center = (BlockPos) ModUtils.readPosFromNBT(nbt.getCompound("center"), true);
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
