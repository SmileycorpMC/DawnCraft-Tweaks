package com.afunproject.dawncraft.capability;

import com.afunproject.dawncraft.network.DCNetworkHandler;
import com.afunproject.dawncraft.network.ToastMessage;
import net.minecraft.core.Direction;
import net.minecraft.nbt.ByteTag;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.network.NetworkDirection;

public interface Toasts {

	void sendToast(ServerPlayer player, byte value);

	void readNBT(CompoundTag nbt);
	void readNBT(ByteTag nbt);

	ByteTag writeNBT();

	class Implementation implements Toasts {

		private byte flags;

		@Override
		public void sendToast(ServerPlayer player, byte b) {
			if ((flags & b) == b) return;
			flags += b;
			DCNetworkHandler.NETWORK_INSTANCE.sendTo(new ToastMessage(b), player.connection.connection, NetworkDirection.PLAY_TO_CLIENT);
		}

		@Override
		public void readNBT(CompoundTag nbt) {
			if (nbt.contains("0") && nbt.getBoolean("0")) flags += 1;
			if (nbt.contains("1") && nbt.getBoolean("1")) flags += 2;
		}

		@Override
		public void readNBT(ByteTag nbt) {
			flags = nbt.getAsByte();
		}

		@Override
		public ByteTag writeNBT() {
			return ByteTag.valueOf(flags);
		}
	}

	class Provider implements ICapabilitySerializable<Tag> {

		private final Toasts impl;

		public Provider() {
			impl = new Implementation();
		}

		@Override
		public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
			return cap == DCCapabilities.TOASTS ? LazyOptional.of(() -> impl).cast() : LazyOptional.empty();
		}

		@Override
		public Tag serializeNBT() {
			return impl.writeNBT();
		}

		@Override
		public void deserializeNBT(Tag nbt) {
			if (nbt instanceof CompoundTag) impl.readNBT((CompoundTag) nbt);
			if (nbt instanceof ByteTag) impl.readNBT((ByteTag) nbt);
		}

	}

}
