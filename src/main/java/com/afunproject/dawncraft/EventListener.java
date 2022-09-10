package com.afunproject.dawncraft;

import com.afunproject.dawncraft.capability.CapabilitiesRegister;
import com.afunproject.dawncraft.capability.Invasions;
import com.afunproject.dawncraft.capability.RestrictBlock;
import com.afunproject.dawncraft.entities.AFPTweaksEntities;
import com.afunproject.dawncraft.entities.QuestEntityBase;

import net.mcreator.simplemobs.entity.Getsuga65Entity;
import net.mcreator.simplemobs.entity.WoodendayEntity;
import net.mcreator.simplemobs.init.SimpleMobsModItems;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.MoveTowardsRestrictionGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.GameType;
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
		if (event.getEntity() instanceof PathfinderMob) {
			PathfinderMob entity = (PathfinderMob) event.getEntity();
			entity.goalSelector.addGoal(5, new MoveTowardsRestrictionGoal(entity, 1.0D));
			LazyOptional<RestrictBlock> optional = entity.getCapability(CapabilitiesRegister.RESTRICT_BLOCK_CAPABILITY);
			if (optional.isPresent()) {
				RestrictBlock cap = optional.resolve().get();
				if (cap.canRestrict(entity)) cap.applyRestriction(entity);
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
		event.put(AFPTweaksEntities.FALLEN.get(), QuestEntityBase.createAttributes());
		event.put(AFPTweaksEntities.QUEST_PLAYER.get(), QuestEntityBase.createAttributes());
	}

}
