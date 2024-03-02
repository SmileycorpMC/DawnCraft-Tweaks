package com.afunproject.dawncraft.capability;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;

public interface FollowQuest {

	public boolean hasStructure();

	public String getStructure();

	public void setStructure(String loc);

	public void readNBT(CompoundTag nbt);

	public CompoundTag writeNBT(CompoundTag nbt);

	public static class Implementation implements FollowQuest {

		private String structure = null;

		@Override
		public boolean hasStructure() {
			return structure != null;
		}

		@Override
		public String getStructure() {
			return structure;
		}

		@Override
		public void setStructure(String loc) {
			structure = loc;
		}

		@Override
		public void readNBT(CompoundTag nbt) {
			if (nbt.contains("structure")) structure = nbt.getString("structure");
		}

		@Override
		public CompoundTag writeNBT(CompoundTag nbt) {
			if (structure != null) nbt.putString("structure", structure);
			return nbt;
		}

	}

	public static class Provider implements ICapabilitySerializable<CompoundTag> {

		private final FollowQuest impl;

		public Provider() {
			impl = new Implementation();
		}

		@Override
		public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
			return cap == DCCapabilities.FOLLOW_QUEST ? LazyOptional.of(() -> impl).cast() : LazyOptional.empty();
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
