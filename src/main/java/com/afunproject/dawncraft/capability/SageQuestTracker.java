package com.afunproject.dawncraft.capability;

import com.afunproject.dawncraft.network.DCNetworkHandler;
import com.google.common.collect.Lists;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.network.NetworkDirection;
import net.smileycorp.atlas.api.network.SimpleIntMessage;

import java.util.List;

public interface SageQuestTracker {

	public void checkAnimal(Player player, Animal animal);

	public int getCheckedCount();

	public boolean isActive();

	public void setActive(boolean active);

	public void readNBT(CompoundTag nbt);

	public CompoundTag writeNBT();

	public static class Implementation implements SageQuestTracker {

		protected List<String> animals = Lists.newArrayList();
		protected boolean isActive;

		@Override
		public void checkAnimal(Player player, Animal animal) {
			String name = animal.getType().getDescriptionId();
			if (!animals.contains(name)) {
				animals.add(name);
				if (animals.size() <= 10 && player instanceof ServerPlayer) DCNetworkHandler.NETWORK_INSTANCE.sendTo(new SimpleIntMessage(animals.size()),
						((ServerPlayer)player).connection.connection, NetworkDirection.PLAY_TO_CLIENT);
			}
		}

		@Override
		public int getCheckedCount() {
			return animals.size();
		}

		@Override
		public boolean isActive() {
			return isActive;
		}

		@Override
		public void setActive(boolean active) {
			isActive = active;
			if (!active) {
				animals.clear();
			}
		}

		@Override
		public void readNBT(CompoundTag nbt) {
			if (nbt.contains("animals")) {
				isActive = true;

			}
		}

		@Override
		public CompoundTag writeNBT() {
			CompoundTag nbt = new CompoundTag();
			if (isActive) {
				ListTag list = new ListTag();
				for (String animal : animals) list.add(StringTag.valueOf(animal));
				nbt.put("animals", list);
			}
			return nbt;
		}



	}

	public static class Provider implements ICapabilitySerializable<CompoundTag> {

		private final SageQuestTracker impl;

		public Provider() {
			impl = new Implementation();
		}

		@Override
		public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
			return cap == DCCapabilities.SAGE_QUEST_TRACKER ? LazyOptional.of(() -> impl).cast() : LazyOptional.empty();
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
