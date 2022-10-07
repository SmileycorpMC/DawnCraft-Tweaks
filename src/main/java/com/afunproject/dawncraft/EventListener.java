package com.afunproject.dawncraft;

import java.util.Random;

import com.afunproject.dawncraft.capability.CapabilitiesRegister;
import com.afunproject.dawncraft.capability.Invasions;
import com.afunproject.dawncraft.capability.RestrictBlock;
import com.afunproject.dawncraft.capability.SpawnTracker;
import com.afunproject.dawncraft.entities.DawnCraftEntities;
import com.afunproject.dawncraft.entities.KnightPlayer;
import com.afunproject.dawncraft.entities.QuestEntityBase;
import com.github.justinwon777.humancompanions.entity.Knight;

import net.mcreator.simplemobs.entity.Getsuga65Entity;
import net.mcreator.simplemobs.entity.Merlin1306Entity;
import net.mcreator.simplemobs.entity.ShadowMewYTEntity;
import net.mcreator.simplemobs.entity.SolarPixelEntity;
import net.mcreator.simplemobs.entity.UGoneEntity;
import net.mcreator.simplemobs.entity.WoodendayEntity;
import net.mcreator.simplemobs.init.SimpleMobsModItems;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.MoveTowardsRestrictionGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.TickEvent.Phase;
import net.minecraftforge.event.TickEvent.PlayerTickEvent;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.player.PlayerEvent.PlayerRespawnEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.registries.ForgeRegistries;
import yesman.epicfight.world.item.EpicFightItems;

@EventBusSubscriber(modid = ModDefinitions.MODID, bus = Bus.MOD)
public class EventListener {

	@SubscribeEvent
	public void playerTick(PlayerTickEvent event) {
		Player player = event.player;
		if (event.phase == Phase.END && player != null && !(player instanceof FakePlayer)) {
			if (!player.level.isClientSide) {
				LazyOptional<Invasions> optional = player.getCapability(CapabilitiesRegister.INVASIONS);
				if (optional.isPresent()) optional.resolve().get().tryToSpawnInvasion(player);
			}
		}
	}

	@SubscribeEvent
	public void entityJoinWorld(EntityJoinWorldEvent event) {
		Level level = event.getWorld();
		if (event.getEntity() instanceof PathfinderMob) {
			PathfinderMob entity = (PathfinderMob) event.getEntity();
			entity.goalSelector.addGoal(5, new MoveTowardsRestrictionGoal(entity, 1.0D));
			LazyOptional<RestrictBlock> optional = entity.getCapability(CapabilitiesRegister.RESTRICT_BLOCK);
			if (optional.isPresent()) {
				RestrictBlock cap = optional.resolve().get();
				if (cap.canRestrict(entity)) cap.applyRestriction(entity);
			}
		}
		if (event.getEntity() instanceof Knight &! level.isClientSide) {
			Knight entity = (Knight) event.getEntity();
			LazyOptional<SpawnTracker> optional = entity.getCapability(CapabilitiesRegister.SPAWN_TRACKER);
			if (optional.isPresent()) {
				SpawnTracker tracker = optional.resolve().get();
				if (!tracker.hasSpawned()) {
					if (new Random().nextInt(100) < 5) {
						KnightPlayer player = DawnCraftEntities.KNIGHT_PLAYER.get().create(level);
						player.setPos(entity.position());
						player.setPlayer("Braydon2570");
						level.addFreshEntity(player);
						player.setItemInHand(InteractionHand.MAIN_HAND, new ItemStack(EpicFightItems.IRON_GREATSWORD.get()));
						player.setItemSlot(EquipmentSlot.CHEST, new ItemStack(SimpleMobsModItems.DIABOLIUM_CHESTPLATE.get()));
						player.setItemSlot(EquipmentSlot.LEGS, new ItemStack(Items.CHAINMAIL_LEGGINGS));
						player.setItemSlot(EquipmentSlot.CHEST, new ItemStack(Items.LEATHER_BOOTS));
						player.getAttribute(Attributes.MAX_HEALTH).setBaseValue(40D);
						entity.discard();
					}
					tracker.setSpawned();
				}
			}
		}
		if (event.getEntity() instanceof Getsuga65Entity) {
			Getsuga65Entity entity = (Getsuga65Entity) event.getEntity();
			entity.setCustomName(new TextComponent("Getsuga65"));
			entity.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation("hexerei", "warhammer"))));
			entity.setItemSlot(EquipmentSlot.CHEST, new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation("scattered_weapons", "hunter_armor_chestplate"))));
		}
		if (event.getEntity() instanceof WoodendayEntity) {
			WoodendayEntity entity = (WoodendayEntity) event.getEntity();
			entity.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(EpicFightItems.IRON_TACHI.get()));
			entity.setItemSlot(EquipmentSlot.CHEST, new ItemStack(SimpleMobsModItems.DIABOLIUM_CHESTPLATE.get()));
			entity.setItemSlot(EquipmentSlot.LEGS, new ItemStack(SimpleMobsModItems.DIABOLIUM_LEGGINGS.get()));
			entity.setCustomName(new TextComponent("Wooden_Day"));
		}
		if (event.getEntity() instanceof ShadowMewYTEntity) {
			ShadowMewYTEntity entity = (ShadowMewYTEntity) event.getEntity();
			entity.setCustomName(new TextComponent("ShadowMewYT"));
		}
		if (event.getEntity() instanceof SolarPixelEntity) {
			SolarPixelEntity entity = (SolarPixelEntity) event.getEntity();
			entity.setItemSlot(EquipmentSlot.CHEST, new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation("cataclysm", "ignitium_chestplate"))));
			entity.setCustomName(new TextComponent("SolarPixel"));
		}
		if (event.getEntity() instanceof Merlin1306Entity) {
			Merlin1306Entity entity = (Merlin1306Entity) event.getEntity();
			entity.setItemSlot(EquipmentSlot.CHEST, new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation("ob_aquamirae", "abyssal_chestplate"))));
			entity.setCustomName(new TextComponent("merlin1306"));
		}
		if (event.getEntity() instanceof UGoneEntity) {
			UGoneEntity entity = (UGoneEntity) event.getEntity();
			entity.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(EpicFightItems.IRON_DAGGER.get()));
			entity.setItemSlot(EquipmentSlot.CHEST, new ItemStack(EpicFightItems.STRAY_ROBE.get()));
			entity.setCustomName(new TextComponent("uGone"));
		}
	}

	@SubscribeEvent
	public void playerRespawn(PlayerRespawnEvent event) {
		if (event.getPlayer() instanceof ServerPlayer) {
			ServerPlayer player = (ServerPlayer) event.getPlayer();
			if (!player.getAbilities().mayBuild) {
				player.setGameMode(GameType.SURVIVAL);
			}
		}
	}

	@SubscribeEvent
	public static void registerAttributes(EntityAttributeCreationEvent event) {
		event.put(DawnCraftEntities.FALLEN.get(), QuestEntityBase.createAttributes());
		event.put(DawnCraftEntities.QUEST_PLAYER.get(), QuestEntityBase.createAttributes());
		event.put(DawnCraftEntities.KNIGHT_PLAYER.get(), Knight.createAttributes().build());
	}

}
