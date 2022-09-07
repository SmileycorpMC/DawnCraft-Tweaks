package com.afunproject.afptweaks.entities;

import com.afunproject.afptweaks.QuestType;

import net.mcreator.simplemobs.entity.PatronwerewolfEntity;
import net.mcreator.simplemobs.init.SimpleMobsModEntities;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;

public class WerewolfPlayer extends QuestPlayer {

	protected static final EntityDataAccessor<Integer> QUEST_PHASE = SynchedEntityData.defineId(WerewolfPlayer.class, EntityDataSerializers.INT);

	protected WerewolfPlayer(EntityType<? extends Mob> type, Level level) {
		super(type, level);
	}

	@Override
	protected void defineSynchedData(){
		super.defineSynchedData();
		entityData.define(QUEST_PHASE, 1);
		entityData.set(TEXT, "text.afptweaks.quest.werewolf1");
	}

	@Override
	public MobType getMobType() {
		return MobType.UNDEFINED;
	}

	@Override
	public InteractionResult mobInteract(Player player, InteractionHand hand) {
		if (entityData.get(QUEST_PHASE) == 2  && player.getInventory().contains(new ItemStack(Items.GOLDEN_APPLE))) {
			setPhase(3);
			for (ItemStack stack : player.getInventory().items) {
				if (stack.getItem() == Items.GOLDEN_APPLE) {
					stack.shrink(1);
					player.playSound(SoundEvents.PLAYER_LEVELUP, 0.5f,  0.5f);
					break;
				}
			}
		}
		return super.mobInteract(player, hand);
	}

	@Override
	public void readAdditionalSaveData(CompoundTag compound) {
		super.readAdditionalSaveData(compound);
		if (compound.contains("quest_phase")) {
			entityData.set(QUEST_PHASE, compound.getInt("quest_phase"));
		}
	}

	@Override
	public void addAdditionalSaveData(CompoundTag compound) {
		super.addAdditionalSaveData(compound);
		compound.putInt("quest_phase", entityData.get(QUEST_PHASE));
	}

	@Override
	public void completeQuest(boolean accepted) {
		if (entityData.get(QUEST_PHASE) == 1 && accepted) {
			setPhase(2);
		}
		else if (entityData.get(QUEST_PHASE) == 3) {
			PatronwerewolfEntity werewolf = new PatronwerewolfEntity(SimpleMobsModEntities.PATRONWEREWOLF.get(), level);
			werewolf.setPos(position());
			werewolf.setYBodyRot(getYRot());
			werewolf.setYHeadRot(getYRot());
			werewolf.setCustomName(getCustomName());
			werewolf.setPersistenceRequired();
			level.addFreshEntity(werewolf);
			werewolf.playAmbientSound();
			setRemoved(RemovalReason.DISCARDED);
		}
	}

	@Override
	public boolean canSeeQuest() {
		return true;
	}

	@Override
	public QuestType getQuestType() {
		int phase = entityData.get(QUEST_PHASE);
		if (phase == 1) return QuestType.ACCEPT_QUEST;
		if (phase == 3) return QuestType.AUTO_CLOSE;
		return QuestType.ACKNOWLEDGE;
	}

	private void setPhase(int phase) {
		entityData.set(QUEST_PHASE, phase);
		entityData.set(TEXT, "text.afptweaks.quest.werewolf"+phase);
	}

}
