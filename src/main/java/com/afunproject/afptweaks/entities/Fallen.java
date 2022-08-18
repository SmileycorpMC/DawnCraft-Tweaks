package com.afunproject.afptweaks.entities;

import java.util.Optional;
import java.util.UUID;

import com.mojang.authlib.GameProfile;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier.Builder;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.Level;
import net.minecraftforge.server.ServerLifecycleHooks;

public class Fallen extends Mob {

	protected static final EntityDataAccessor<Optional<UUID>> PLAYER = SynchedEntityData.defineId(Fallen.class, EntityDataSerializers.OPTIONAL_UUID);

	protected Fallen(EntityType<? extends Mob> type, Level level) {
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

	public Optional<UUID> getPlayerUUID() {
		return entityData.get(PLAYER);
	}

	@Override
	public void tick() {
		setNoGravity(true);
		noPhysics = true;
		super.tick();
		noPhysics = false;
	}

	@Override
	public MobType getMobType() {
		return MobType.UNDEAD;
	}

	@Override
	public boolean isPushable() {
		return false;
	}

	@Override
	protected void doPush(Entity entity) {}

	@Override
	protected void pushEntities() {}

	@Override
	public boolean hurt(DamageSource source, float amount) {
		return source == DamageSource.OUT_OF_WORLD ? super.hurt(source, amount) : false;
	}

	@Override
	public boolean skipAttackInteraction(Entity entity) {
		return true;
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

	public static AttributeSupplier createAttributes() {
		Builder builder = Mob.createMobAttributes().add(Attributes.FOLLOW_RANGE, 35.0D).add(Attributes.MOVEMENT_SPEED, 0.23D).add(Attributes.ATTACK_DAMAGE, 3.0D);
		return builder.build();
	}

}
