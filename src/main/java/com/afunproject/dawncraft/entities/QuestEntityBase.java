package com.afunproject.dawncraft.entities;

import com.afunproject.dawncraft.network.AFPPacketHandler;
import com.afunproject.dawncraft.network.OpenQuestMessage;
import com.afunproject.dawncraft.quest.Quest;
import com.afunproject.dawncraft.quest.QuestEntity;
import com.afunproject.dawncraft.quest.QuestsRegistry;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier.Builder;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.network.NetworkDirection;
import yesman.epicfight.world.capabilities.EpicFightCapabilities;
import yesman.epicfight.world.capabilities.entitypatch.EntityPatch;
import yesman.epicfight.world.capabilities.entitypatch.player.PlayerPatch;

public abstract class QuestEntityBase extends Mob implements QuestEntity {

	protected static final EntityDataAccessor<String> TEXT = SynchedEntityData.defineId(QuestEntityBase.class, EntityDataSerializers.STRING);
	protected static final EntityDataAccessor<Integer> QUEST_PHASE = SynchedEntityData.defineId(QuestEntityBase.class, EntityDataSerializers.INT);

	protected Quest quest = null;
	protected boolean damageable;

	protected QuestEntityBase(EntityType<? extends Mob> type, Level level) {
		super(type, level);
	}

	@Override
	protected void registerGoals() {
		goalSelector.addGoal(1, new LookAtPlayerGoal(this, Player.class, 6.0F));
	}

	@Override
	protected void defineSynchedData(){
		super.defineSynchedData();
		entityData.define(TEXT, "");
		entityData.define(QUEST_PHASE, 1);
	}

	@Override
	@SuppressWarnings("rawtypes")
	public InteractionResult mobInteract(Player player, InteractionHand hand) {
		if (player.isSecondaryUseActive()) return super.mobInteract(player, hand);
		LazyOptional<EntityPatch> optional = player.getCapability(EpicFightCapabilities.CAPABILITY_ENTITY);
		if (optional.isPresent()) if (((PlayerPatch<?>)optional.resolve().get()).isBattleMode()) return InteractionResult.PASS;
		if (player instanceof ServerPlayer && canSeeQuest()) {
			if (quest != null) if (quest.isQuestComplete(player, this, entityData.get(QUEST_PHASE))) {
				entityData.set(QUEST_PHASE, entityData.get(QUEST_PHASE)+1);
				setQuestText(quest.getText(entityData.get(QUEST_PHASE), true));
			}
			AFPPacketHandler.NETWORK_INSTANCE.sendTo(new OpenQuestMessage(this, quest), ((ServerPlayer) player).connection.connection, NetworkDirection.PLAY_TO_CLIENT);
		}
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
		return (damageable || source == DamageSource.OUT_OF_WORLD) ? super.hurt(source, amount) : false;
	}

	@Override
	public void die(DamageSource source) {
		if (quest != null) quest.onDeath(this, source);
	}

	@Override
	public boolean skipAttackInteraction(Entity entity) {
		return !damageable;
	}

	@Override
	public void addAdditionalSaveData(CompoundTag compound) {
		super.addAdditionalSaveData(compound);
		compound.putBoolean("damageable", damageable);
		saveQuestData(compound);
	}

	@Override
	public void readAdditionalSaveData(CompoundTag compound) {
		super.readAdditionalSaveData(compound);
		if (compound.contains("damageable")) damageable = compound.getBoolean("damageable");
		loadQuestData(compound);
	}

	public static AttributeSupplier createAttributes() {
		Builder builder = Mob.createMobAttributes().add(Attributes.FOLLOW_RANGE, 35.0D).add(Attributes.MOVEMENT_SPEED, 0.23D).add(Attributes.ATTACK_DAMAGE, 3.0D);
		return builder.build();
	}

	@Override
	public Quest getCurrentQuest() {
		return quest;
	}

	@Override
	public int getQuestPhase() {
		return entityData.get(QUEST_PHASE);
	}

	@Override
	public String getQuestText() {
		return entityData.get(TEXT);
	}

	@Override
	public void setQuestPhase(int phase) {
		entityData.set(QUEST_PHASE, phase);
	}

	@Override
	public void setQuest(Quest quest) {
		this.quest = quest;
	}

	@Override
	public void setQuestText(String text) {
		entityData.set(TEXT, text);
	}

	@Override
	public CompoundTag saveQuestData(CompoundTag tag) {
		tag.putString("text", entityData.get(TEXT));
		tag.putInt("quest_phase", entityData.get(QUEST_PHASE));
		if (quest != null) tag.putString("quest", quest.getRegistryName().toString());
		return tag;
	}

	@Override
	public void loadQuestData(CompoundTag tag) {
		if (tag.contains("text")) entityData.set(TEXT, tag.getString("text"));
		if (tag.contains("quest_phase")) entityData.set(QUEST_PHASE, tag.getInt("quest_phase"));
		if (tag.contains("quest")) {
			setQuest(QuestsRegistry.getQuest(new ResourceLocation(tag.getString("quest"))));
			if (quest != null) entityData.set(TEXT, quest.getText(entityData.get(QUEST_PHASE), true));
		}
	}

}
