package com.afunproject.dawncraft.capability;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;

import java.util.UUID;

public interface Invader {
	
	Invasion getInvasion();
	
	void setInvasion(Player player);

	void load(CompoundTag tag);
	
	CompoundTag save();

	class Implementation implements Invader {
		
		private final LivingEntity entity;
		private UUID player;
		private Invasion invasion;
		
		public Implementation(LivingEntity entity) {
			this.entity = entity;
		}
		
		@Override
		public Invasion getInvasion() {
			if (player == null || entity == null) return null;
			Player player = entity.level.getPlayerByUUID(this.player);
			if (player == null) return null;
			if (invasion == null) invasion = player.getCapability(DCCapabilities.INVASIONS).orElse(null);
			return invasion;
		}
		
		@Override
		public void setInvasion(Player player) {
			this.player = player.getUUID();
		}
		
		@Override
		public void load(CompoundTag tag) {
			if (tag.hasUUID("player")) player = NbtUtils.loadUUID(tag.get("player"));
			if (entity != null && player != null) {
				Player player = entity.level.getPlayerByUUID(this.player);
				if (player == null) return;
				invasion = player.getCapability(DCCapabilities.INVASIONS).orElse(null);
			}
		}
		
		@Override
		public CompoundTag save() {
			CompoundTag tag = new CompoundTag();
			if (player != null) tag.put("player", NbtUtils.createUUID(player));
			return tag;
		}
	}
	public class Provider implements ICapabilitySerializable<CompoundTag> {

		protected final Invader impl;
		
		public Provider(LivingEntity entity) {
			impl = new Implementation(entity);
		}

		@Override
		public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction facing) {
			return cap == DCCapabilities.INVADER ? LazyOptional.of(() -> impl).cast() : LazyOptional.empty();
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
