package com.afunproject.dawncraft;

import com.afunproject.dawncraft.capability.*;
import com.afunproject.dawncraft.dungeon.item.DungeonItems;
import com.afunproject.dawncraft.dungeon.item.RebirthStaffItem;
import com.afunproject.dawncraft.effects.DawnCraftEffects;
import com.afunproject.dawncraft.entities.ai.GoToRestrictionGoal;
import com.afunproject.dawncraft.integration.apotheosis.ApotheosisCompat;
import com.afunproject.dawncraft.integration.champions.ChampionsIntegration;
import com.afunproject.dawncraft.integration.epicfight.EpicFightCompat;
import com.afunproject.dawncraft.integration.ironspellbooks.IronsSpellbooksCompat;
import com.afunproject.dawncraft.integration.suplementaries.RitualChecker;
import com.google.common.collect.Lists;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.AttributeModifier.Operation;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.TickEvent.Phase;
import net.minecraftforge.event.TickEvent.PlayerTickEvent;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerEvent.PlayerRespawnEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;

@EventBusSubscriber(modid = Constants.MODID, bus = Bus.MOD)
public class EventListener {

	private static final UUID BOSS_MODIFIER = UUID.fromString("dd686c7a-e2c7-479c-96d5-3e193b35c7b8");

	public static final List<Consumer<EntityAttributeCreationEvent>> ATTRIBUTE_SUPPLIERS = Lists.newArrayList();

	@SubscribeEvent
	public void playerTick(PlayerTickEvent event) {
		Player player = event.player;
		if (event.phase == Phase.END && player != null && !(player instanceof FakePlayer)) {
			if (player.level.isClientSide) return;
			LazyOptional<Invasion> optional = player.getCapability(DCCapabilities.INVASIONS);
			if (optional.isPresent()) optional.resolve().get().tryToSpawnInvasion();
		}
	}

	@SubscribeEvent
	public void entityJoinWorld(EntityJoinWorldEvent event) {
		//scale boss hp and damage based on players nearby
		if (event.getEntity().getType().m_204039_(DCEntityTags.BOSSES) && event.getEntity() instanceof Mob) {
			Mob boss = (Mob) event.getEntity();
			int players = 0;
			for (Player player : boss.level.players()) {
				if (player.distanceTo(boss) <= 100) players++;
			}
			if (players > 1) {
				AttributeInstance damage = boss.getAttribute(Attributes.ATTACK_DAMAGE);
				if (damage != null) {
					double damage_multiplier = Math.max(1.5, Math.pow(1.05, players - 1));
					damage.removeModifier(BOSS_MODIFIER);
					damage.addPermanentModifier(new AttributeModifier(BOSS_MODIFIER, "dawncraft_multiplayer_scaling", damage_multiplier, Operation.MULTIPLY_TOTAL));
				}
				AttributeInstance health = boss.getAttribute(Attributes.MAX_HEALTH);
				if (health != null) {
					double health_multiplier = Math.max(2, Math.pow(1.25, players - 1));
					health.removeModifier(BOSS_MODIFIER);
					health.addPermanentModifier(new AttributeModifier(BOSS_MODIFIER, "dawncraft_multiplayer_scaling", health_multiplier, Operation.MULTIPLY_TOTAL));
					boss.setHealth((float) health.getValue());
				}
			}
		}
		//add block restrictions
		if (event.getEntity() instanceof Mob) {
			Mob entity = (Mob) event.getEntity();
			LazyOptional<RestrictBlock> optional = entity.getCapability(DCCapabilities.RESTRICT_BLOCK);
			if (optional.isPresent()) {
				RestrictBlock cap = optional.resolve().get();
				entity.goalSelector.addGoal(1, new GoToRestrictionGoal(entity, cap));
			}
		}
		//add effects to natural iron golems
		if (event.getEntity() instanceof IronGolem) {
			IronGolem golem = (IronGolem) event.getEntity();
			if (!golem.isPlayerCreated()) {
				golem.addEffect(new MobEffectInstance(MobEffects.DAMAGE_BOOST, 10000000, 0, false, false));
				golem.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 10000000, 0, false, false));
				golem.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 10000000, 0, false, false));
			}
		}
	}

	@SubscribeEvent
	public void playerRespawn(PlayerRespawnEvent event) {
		if (event.getPlayer() instanceof ServerPlayer) {
			ServerPlayer player = (ServerPlayer) event.getPlayer();
			if (!player.getAbilities().mayBuild) player.setGameMode(GameType.SURVIVAL);
		}
	}

	@SubscribeEvent
	public void playerClone(PlayerEvent.Clone event) {
		Player original = event.getOriginal();
		Player player = event.getPlayer();
		original.reviveCaps();
		//clone sage quest capabilities
		LazyOptional<SageQuestTracker> soptionalOld = original.getCapability(DCCapabilities.SAGE_QUEST_TRACKER);
		LazyOptional<SageQuestTracker> soptional = player.getCapability(DCCapabilities.SAGE_QUEST_TRACKER);
		if (soptionalOld.isPresent() && soptional.isPresent()) soptional.resolve().get().readNBT(soptionalOld.resolve().get().writeNBT());
		//clone toast capabilities
		LazyOptional<Toasts> toptionalOld = original.getCapability(DCCapabilities.TOASTS);
		LazyOptional<Toasts> toptional = player.getCapability(DCCapabilities.TOASTS);
		if (toptionalOld.isPresent() && toptional.isPresent()) toptional.resolve().get().readNBT(toptionalOld.resolve().get().writeNBT());
		if (original.hasEffect(DawnCraftEffects.FRACTURED_SOUL.get())) {
			player.addEffect(new MobEffectInstance(DawnCraftEffects.FRACTURED_SOUL.get(), 3600,
					Math.min(4, original.getEffect(DawnCraftEffects.FRACTURED_SOUL.get()).getAmplifier()+1), true, true));
		} else {
			player.addEffect(new MobEffectInstance(DawnCraftEffects.FRACTURED_SOUL.get(), 3600, 0, true, true));
		}
	}

	@SubscribeEvent
	public void rightClickBlock(PlayerInteractEvent.RightClickBlock event) {
		Level level = event.getWorld();
		if (level.isClientSide) return;
		ItemStack stack = event.getItemStack();
		BlockPos pos = event.getPos();
		BlockState state = level.getBlockState(pos);
		if (stack == null || state ==null) return;
		if (!(state.is(Blocks.ENCHANTING_TABLE) && stack.is(DungeonItems.REBIRTH_STAFF.get()))) return;
		if (RebirthStaffItem.isPowered(stack) |! ModList.get().isLoaded("supplementaries")) return;
		if (!RitualChecker.isValid(level, pos)) return;
		RitualChecker.startRitual(stack, level, pos);
		event.setCanceled(true);
	}

	@SubscribeEvent(priority = EventPriority.LOWEST)
	public void livingHurt(LivingDamageEvent event) {
		if (event.isCanceled()) return;
		LivingEntity entity = event.getEntityLiving();
		DamageSource source = event.getSource();
		if (entity.level.isClientSide |! (source.getEntity() instanceof LivingEntity)) return;
			LivingEntity attacker = (LivingEntity) source.getEntity();
		if (attacker.getItemInHand(InteractionHand.MAIN_HAND).is(DungeonItems.SLAYERS_BLADE.get())) {
			if (attacker instanceof Player && ModList.get().isLoaded("epicfight"))
				if (EpicFightCompat.isCombatMode((Player) attacker)) event.setAmount(entity.getMaxHealth() * 0.1f);
			else event.setAmount(entity.getMaxHealth() * 0.1f);
		}
		else if (attacker.getItemInHand(InteractionHand.MAIN_HAND).is(DungeonItems.EXECUTIONER.get()) && entity.getHealth() < (entity.getMaxHealth() * 0.15)) {
			attacker.level.playSound(null, attacker.getX(), attacker.getY(), attacker.getZ(), SoundEvents.END_PORTAL_SPAWN, SoundSource.PLAYERS, 1f, attacker.getRandom().nextFloat());
			event.setAmount(entity.getHealth());
		}
	}

	@SubscribeEvent
	public void livingDeath(LivingDeathEvent event) {
		LivingEntity entity = event.getEntityLiving();
		if (entity.level.isClientSide) return;
		if (entity instanceof Player) {
			DamageSource source = event.getSource();
			if (source.getEntity() != null) {
				if (source.getEntity().getType().m_204039_(DCEntityTags.BOSSES)) {
					LivingEntity boss = (LivingEntity) source.getEntity();
					boss.heal(boss.getMaxHealth() * 0.25f);
				}
			}
		}
		LazyOptional<Invader> optional = entity.getCapability(DCCapabilities.INVADER);
		if (!optional.isPresent()) return;
		Invasion invasion = optional.orElse(null).getInvasion();
		if (invasion == null) return;
		invasion.entityKilled(entity);
	}

	@SubscribeEvent(priority = EventPriority.LOWEST)
	public void livingDeathEnd(LivingDeathEvent event) {
		Level level = event.getEntity().level;
		if (!level.isClientSide) {
			if (level.getGameRules().getBoolean(GameRules.RULE_KEEPINVENTORY)) {
				if (ModList.get().isLoaded("apotheosis")) ApotheosisCompat.fixNBT(event.getEntityLiving());
			}
		}
	}
	
	@SubscribeEvent
	public void rightClick(PlayerInteractEvent.RightClickItem event) {
		if (ModList.get().isLoaded("irons_spellbooks")) if (IronsSpellbooksCompat.isSpellBook(event.getItemStack())) {
			event.getEntityLiving().addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 20, 3));
		}
	}
	
	@SubscribeEvent(priority = EventPriority.LOWEST)
	public void playerTick(LivingEvent.LivingVisibilityEvent event) {
		Entity entity = event.getLookingEntity();
		if (!event.getEntityLiving().getItemBySlot(EquipmentSlot.HEAD).is(DungeonItems.MASK_OF_ATHORA.get())) return;
		if (entity.getType().m_204039_(DCEntityTags.BYPASSES_MASK_OF_ATHORA)) return;
		if (ModList.get().isLoaded("champions")) if (ChampionsIntegration.isChampion(entity)) return;
		event.modifyVisibility(0);
	}

	//major jank because forge and eclipse both suck
	@SubscribeEvent
	public static void registerAttributes(EntityAttributeCreationEvent event) {
		for (Consumer<EntityAttributeCreationEvent> supplier : ATTRIBUTE_SUPPLIERS) supplier.accept(event);
	}

}
