package com.afunproject.dawncraft.integration.quests.custom.entity;

import com.afunproject.dawncraft.integration.epicfight.EpicFightCompat;
import com.afunproject.dawncraft.integration.quests.custom.QuestEntity;
import com.afunproject.dawncraft.integration.quests.custom.quests.Quest;
import com.afunproject.dawncraft.integration.quests.custom.quests.QuestsRegistry;
import com.afunproject.dawncraft.integration.quests.network.OpenQuestMessage;
import com.afunproject.dawncraft.network.DCNetworkHandler;
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
import net.minecraftforge.fml.ModList;
import net.minecraftforge.network.NetworkDirection;

import java.util.List;

public abstract class QuestEntityBase extends Mob implements QuestEntity {

	protected static final EntityDataAccessor<String> TEXT = SynchedEntityData.defineId(QuestEntityBase.class, EntityDataSerializers.STRING);
	protected static final EntityDataAccessor<Integer> QUEST_PHASE = SynchedEntityData.defineId(QuestEntityBase.class, EntityDataSerializers.INT);
	protected static final EntityDataAccessor<String> QUEST = SynchedEntityData.defineId(QuestEntityBase.class, EntityDataSerializers.STRING);

	protected boolean damageable;
	private boolean shouldDespawn;

	protected QuestEntityBase(EntityType<? extends Mob> type, Level level) {
		super(type, level);
	}

	@Override
	protected void registerGoals() {
		goalSelector.addGoal(1, new LookAtPlayerGoal(this, Player.class, 6.0F, 0.02F, true));
	}

	@Override
	protected void defineSynchedData(){
		super.defineSynchedData();
		entityData.define(TEXT, "");
		entityData.define(QUEST_PHASE, 1);
		entityData.define(QUEST, "");
	}

	@Override
	public InteractionResult mobInteract(Player player, InteractionHand hand) {
		if (player.isSecondaryUseActive()) return super.mobInteract(player, hand);
		if (ModList.get().isLoaded("epicfight")) if (EpicFightCompat.isCombatMode(player)) return InteractionResult.PASS;
		if (player instanceof ServerPlayer && canSeeQuest()) {
			Quest quest = getCurrentQuest();
			if (quest != null) {
				if (!quest.isQuestActive(this, entityData.get(QUEST_PHASE))) return InteractionResult.PASS;
				if (quest.isQuestComplete(player, this, entityData.get(QUEST_PHASE))) {
					entityData.set(QUEST_PHASE, entityData.get(QUEST_PHASE)+1);
					setQuestText(quest.getText(entityData.get(QUEST_PHASE), true));
				}
				DCNetworkHandler.NETWORK_INSTANCE.sendTo(new OpenQuestMessage(this, quest), ((ServerPlayer) player).connection.connection, NetworkDirection.PLAY_TO_CLIENT);
			}
		}
		return InteractionResult.PASS;
	}

	@Override
	public boolean isPushable() {
		return false;
	}

	@Override
	protected void pushEntities() {
		List<Entity> entities = level.getEntities(this, this.getBoundingBox(), (entity)-> !(isPassengerOfSameVehicle(entity) || entity.isSpectator()) && entity.isPushable());
		for (Entity entity : entities) doPush(entity);
	}

	@Override
	public void push(double x, double y, double z) {}

	@Override
	public boolean isPersistenceRequired() {
		return !shouldDespawn;
	}

	@Override
	public boolean removeWhenFarAway(double range) {
		return range >= 50 && shouldDespawn;
	}

	@Override
	public boolean hurt(DamageSource source, float amount) {
		boolean result = (damageable || source == DamageSource.OUT_OF_WORLD) ? super.hurt(source, amount) : false;
		if (damageable && getCurrentQuest() != null) getCurrentQuest().onHurt(this, source);
		return result;
	}

	@Override
	public void die(DamageSource source) {
		if (getCurrentQuest() != null) getCurrentQuest().onDeath(this, source);
	}

	@Override
	public boolean skipAttackInteraction(Entity entity) {
		return !damageable;
	}

	@Override
	public void addAdditionalSaveData(CompoundTag compound) {
		super.addAdditionalSaveData(compound);
		compound.putBoolean("damageable", damageable);
		compound.putBoolean("shouldDespawn", shouldDespawn);
		saveQuestData(compound);
	}

	@Override
	public void readAdditionalSaveData(CompoundTag compound) {
		super.readAdditionalSaveData(compound);
		if (compound.contains("damageable")) damageable = compound.getBoolean("damageable");
		if (compound.contains("shouldDespawn")) shouldDespawn = compound.getBoolean("shouldDespawn");
		loadQuestData(compound);
	}

	public static AttributeSupplier createDefaultAttributes() {
		Builder builder = Mob.createMobAttributes().add(Attributes.FOLLOW_RANGE, 35.0D)
				.add(Attributes.MOVEMENT_SPEED, 0.23D)
				.add(Attributes.ATTACK_DAMAGE, 3.0D)
				.add(Attributes.KNOCKBACK_RESISTANCE, 100);
		return builder.build();
	}

	@Override
	public Quest getCurrentQuest() {
		try {
			return QuestsRegistry.getQuest(new ResourceLocation(entityData.get(QUEST)));
		}
		catch (Exception e) {
			return null;
		}
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
		if (quest == null) entityData.set(QUEST, "");
		else entityData.set(QUEST, quest.getRegistryName().toString());
	}

	@Override
	public void setQuestText(String text) {
		entityData.set(TEXT, text);
	}

	@Override
	public CompoundTag saveQuestData(CompoundTag tag) {
		tag.putString("text", entityData.get(TEXT));
		tag.putInt("quest_phase", entityData.get(QUEST_PHASE));
		if (getCurrentQuest() != null) tag.putString("quest", getCurrentQuest().getRegistryName().toString());
		return tag;
	}

	@Override
	public void loadQuestData(CompoundTag tag) {
		if (tag.contains("text")) entityData.set(TEXT, tag.getString("text"));
		if (tag.contains("quest_phase")) entityData.set(QUEST_PHASE, tag.getInt("quest_phase"));
		if (tag.contains("quest")) {
			setQuest(QuestsRegistry.getQuest(new ResourceLocation(tag.getString("quest"))));
			if (getCurrentQuest() != null) entityData.set(TEXT, getCurrentQuest().getText(entityData.get(QUEST_PHASE), true));
		}
	}

	public void setDespawnable(boolean shouldDespawn) {
		this.shouldDespawn = shouldDespawn;
	}

}
