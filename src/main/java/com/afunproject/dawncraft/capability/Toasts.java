package com.afunproject.dawncraft.capability;

import com.afunproject.dawncraft.network.DCNetworkHandler;
import com.afunproject.dawncraft.network.ToastMessage;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.network.NetworkDirection;

public interface Toasts {

	void sendCombat(ServerPlayer player);

	void sendDodge(ServerPlayer player);

	void readNBT(CompoundTag nbt);

	CompoundTag writeNBT();

	class Implementation implements Toasts {

		private boolean combat, dodge;

		@Override
		public void sendCombat(ServerPlayer player) {
			if (combat) return;
			combat = true;
			DCNetworkHandler.NETWORK_INSTANCE.sendTo(new ToastMessage((byte)0), player.connection.connection, NetworkDirection.PLAY_TO_CLIENT);
		}

		@Override
		public void sendDodge(ServerPlayer player) {
			if (dodge) return;
			dodge = true;
			DCNetworkHandler.NETWORK_INSTANCE.sendTo(new ToastMessage((byte)1), player.connection.connection, NetworkDirection.PLAY_TO_CLIENT);
		}

		@Override
		public void readNBT(CompoundTag nbt) {
			if (nbt.contains("0")) combat = nbt.getBoolean("0");
			if (nbt.contains("1")) dodge = nbt.getBoolean("1");
		}

		@Override
		public CompoundTag writeNBT() {
			CompoundTag tag = new CompoundTag();
			if (combat) tag.putBoolean("0", true);
			if (dodge) tag.putBoolean("1", true);
			return tag;
		}
	}

	class Provider implements ICapabilitySerializable<CompoundTag> {

		private final Toasts impl;

		public Provider() {
			impl = new Implementation();
		}

		@Override
		public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
			return cap == CapabilitiesRegister.TOASTS ? LazyOptional.of(() -> impl).cast() : LazyOptional.empty();
		}

		@Override
		public CompoundTag serializeNBT() {
			return impl.writeNBT();
		}

		@Override
		public void deserializeNBT(CompoundTag nbt) {
			impl.readNBT(nbt);
		}

	}

}
