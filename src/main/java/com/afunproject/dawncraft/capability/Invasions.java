package com.afunproject.dawncraft.capability;

import java.util.List;
import java.util.Random;

import com.afunproject.dawncraft.invasion.InvasionEntry;
import com.afunproject.dawncraft.invasion.InvasionRegistry;
import com.google.common.collect.Lists;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;

public interface Invasions {

	public void tryToSpawnInvasion(Player player);

	public CompoundTag save();

	public void load(CompoundTag tag);

	public static class Implementation implements Invasions {

		private Random rand = new Random();
		private List<InvasionEntry> invasions = Lists.newArrayList();

		public Implementation() {
			invasions.addAll(InvasionRegistry.getInvasions());
		}

		@Override
		public void tryToSpawnInvasion(Player player) {
			if (!player.level.isClientSide && player.tickCount % 240000 == 0 && player.tickCount > 0 &! invasions.isEmpty()) {
				InvasionEntry invasion = invasions.get(rand.nextInt(invasions.size()));
				invasion.spawnEntities(player);
				invasions.remove(invasion);
			}
		}

		@Override
		public CompoundTag save() {
			CompoundTag tag = new CompoundTag();
			ListTag list = new ListTag();
			for (InvasionEntry invasion : invasions) list.add(invasion.save());
			tag.put("invasions", list);
			return tag;
		}

		@Override
		public void load(CompoundTag tag) {
			invasions.clear();
			for (Tag invasion : tag.getList("invasions", 10)) try {
				invasions.add(new InvasionEntry((CompoundTag)invasion));
			} catch (Exception e) {}
		}

	}

	public static class Provider implements ICapabilitySerializable<CompoundTag> {

		protected Invasions impl = new Implementation();

		@Override
		public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction facing) {
			return cap == CapabilitiesRegister.INVASIONS ? LazyOptional.of(() -> impl).cast() : LazyOptional.empty();
		}

		@Override
		public CompoundTag serializeNBT() {
			return impl.save();
		}

		@Override
		public void deserializeNBT(CompoundTag nbt) {
			impl.load(nbt);
		}

	}

}
