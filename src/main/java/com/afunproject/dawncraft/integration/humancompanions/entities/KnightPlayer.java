package com.afunproject.dawncraft.integration.humancompanions.entities;

import com.afunproject.dawncraft.entities.PlayerEntity;
import com.github.justinwon777.humancompanions.entity.Knight;
import com.mojang.authlib.GameProfile;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraftforge.server.ServerLifecycleHooks;

import java.util.Optional;
import java.util.UUID;

public class KnightPlayer extends Knight implements PlayerEntity {

	protected static final EntityDataAccessor<Optional<UUID>> PLAYER = SynchedEntityData.defineId(KnightPlayer.class, EntityDataSerializers.OPTIONAL_UUID);

	protected KnightPlayer(EntityType<? extends Knight> type, Level level) {
		super(type, level);
	}

	@Override
	protected void defineSynchedData(){
		super.defineSynchedData();
		entityData.define(PLAYER, Optional.empty());
	}

	public void setPlayer(String username) {
		Optional<GameProfile> optional = ServerLifecycleHooks.getCurrentServer().getProfileCache().get(username);
		if (optional.isPresent()) setPlayer(optional.get());
	}

	public void setPlayer(UUID uuid) {
		Optional<GameProfile> optional = ServerLifecycleHooks.getCurrentServer().getProfileCache().get(uuid);
		if (optional.isPresent()) setPlayer(optional.get());
	}

	public void setPlayer(GameProfile profile) {
		if (profile.getName() != null) setCustomName(new TextComponent(profile.getName()));
		entityData.set(PLAYER, Optional.of(profile.getId()));
	}

	@Override
	public Optional<UUID> getPlayerUUID() {
		return entityData.get(PLAYER);
	}

	@Override
	public void readAdditionalSaveData(CompoundTag compound) {
		super.readAdditionalSaveData(compound);
		if (compound.contains("username")) {
			setPlayer(compound.getString("username"));
		}
		if (compound.contains("player_uuid")) {
			setPlayer(UUID.fromString(compound.getString("player_uuid")));
		}
		if (compound.contains("player")) {
			entityData.set(PLAYER, Optional.of(compound.getUUID("player")));
		}
	}

	@Override
	public void addAdditionalSaveData(CompoundTag compound) {
		super.addAdditionalSaveData(compound);
		Optional<UUID> optional = entityData.get(PLAYER);
		if (optional.isPresent()) {
			compound.putUUID("player", optional.get());
		}
	}

}
