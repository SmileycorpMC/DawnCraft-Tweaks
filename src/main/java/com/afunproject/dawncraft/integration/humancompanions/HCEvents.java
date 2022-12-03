package com.afunproject.dawncraft.integration.humancompanions;

import java.util.Random;

import com.afunproject.dawncraft.capability.CapabilitiesRegister;
import com.afunproject.dawncraft.capability.SpawnTracker;
import com.afunproject.dawncraft.integration.humancompanions.entities.HCEntities;
import com.afunproject.dawncraft.integration.humancompanions.entities.KnightPlayer;
import com.github.justinwon777.humancompanions.entity.Knight;

import net.mcreator.simplemobs.init.SimpleMobsModItems;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import yesman.epicfight.world.item.EpicFightItems;

public class HCEvents {

	public static void init() {
		MinecraftForge.EVENT_BUS.register(new HCEvents());
		MinecraftForge.EVENT_BUS.register(HCEvents.class);
		HCEntities.ENTITIES.register(FMLJavaModLoadingContext.get().getModEventBus());
	}

	@SubscribeEvent
	public void entityJoinWorld(EntityJoinWorldEvent event) {
		Level level = event.getWorld();
		if (event.getEntity() instanceof Knight &! level.isClientSide) {
			Knight entity = (Knight) event.getEntity();
			LazyOptional<SpawnTracker> optional = entity.getCapability(CapabilitiesRegister.SPAWN_TRACKER);
			if (optional.isPresent()) {
				SpawnTracker tracker = optional.resolve().get();
				if (!tracker.hasSpawned()) {
					if (new Random().nextInt(100) < 5) {
						KnightPlayer player = HCEntities.KNIGHT_PLAYER.get().create(level);
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
	}

	@SubscribeEvent
	public static void registerAttributes(EntityAttributeCreationEvent event) {
		event.put(HCEntities.KNIGHT_PLAYER.get(), Knight.createAttributes().build());
	}

}
