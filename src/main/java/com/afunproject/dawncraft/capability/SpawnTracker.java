package com.afunproject.dawncraft.capability;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;

public interface SpawnTracker {

	public boolean hasSpawned();

	public void setSpawned();

	public void readNBT(CompoundTag nbt);

	public CompoundTag writeNBT(CompoundTag nbt);

	public static class Implementation implements SpawnTracker {

		private boolean has_spawned;

		@Override
		public boolean hasSpawned() {
			return has_spawned;
		}

		@Override
		public void setSpawned() {
			has_spawned = true;
		}

		@Override
		public void readNBT(CompoundTag nbt) {
			if (nbt.contains("has_spawned")) has_spawned = nbt.getBoolean("has_spawned");
		}

		@Override
		public CompoundTag writeNBT(CompoundTag nbt) {
			if (has_spawned) nbt.putBoolean("has_spawned", has_spawned);
			return nbt;
		}

	}

	public static class Provider implements ICapabilitySerializable<CompoundTag> {

		private final SpawnTracker impl;

		public Provider() {
			impl = new Implementation();
		}

		@Override
		public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
			return cap == DCCapabilities.SPAWN_TRACKER ? LazyOptional.of(() -> impl).cast() : LazyOptional.empty();
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
