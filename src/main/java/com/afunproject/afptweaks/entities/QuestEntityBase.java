package com.afunproject.afptweaks.entities;

import com.afunproject.afptweaks.client.ClientHandler;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier.Builder;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public abstract class QuestEntityBase extends Mob implements QuestEntity {

	protected static final EntityDataAccessor<String> TEXT = SynchedEntityData.defineId(QuestEntityBase.class, EntityDataSerializers.STRING);

	protected QuestEntityBase(EntityType<? extends Mob> type, Level level) {
		super(type, level);
	}

	@Override
	protected void defineSynchedData(){
		super.defineSynchedData();
		entityData.define(TEXT, "");
	}

	@Override
	public InteractionResult  mobInteract(Player player, InteractionHand hand) {
		if (player.isSecondaryUseActive()) return super.mobInteract(player, hand);
		if (player.level.isClientSide && canSeeQuest()) ClientHandler.openQuestGUI(this, new TranslatableComponent(entityData.get(TEXT), player.getName()), getQuestType());
		return InteractionResult.PASS;
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
	public boolean isPersistenceRequired() {
		return true;
	}

	@Override
	public boolean hurt(DamageSource source, float amount) {
		return source == DamageSource.OUT_OF_WORLD ? super.hurt(source, amount) : false;
	}

	@Override
	public boolean skipAttackInteraction(Entity entity) {
		return true;
	}

	@Override
	public void addAdditionalSaveData(CompoundTag compound) {
		super.addAdditionalSaveData(compound);
		compound.putString("text", entityData.get(TEXT));
	}

	@Override
	public void readAdditionalSaveData(CompoundTag compound) {
		super.readAdditionalSaveData(compound);
		if (compound.contains("username")) entityData.set(TEXT, compound.getString("text"));
	}

	public static AttributeSupplier createAttributes() {
		Builder builder = Mob.createMobAttributes().add(Attributes.FOLLOW_RANGE, 35.0D).add(Attributes.MOVEMENT_SPEED, 0.23D).add(Attributes.ATTACK_DAMAGE, 3.0D);
		return builder.build();
	}

}
