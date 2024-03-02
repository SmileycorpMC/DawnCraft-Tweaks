package com.afunproject.dawncraft.integration.humancompanions;

import com.afunproject.dawncraft.EventListener;
import com.afunproject.dawncraft.capability.DCCapabilities;
import com.afunproject.dawncraft.capability.SpawnTracker;
import com.afunproject.dawncraft.integration.humancompanions.entities.HCEntities;
import com.afunproject.dawncraft.integration.humancompanions.entities.KnightPlayer;
import com.github.justinwon777.humancompanions.entity.Knight;
import com.github.justinwon777.humancompanions.entity.ai.MoveBackToPatrolGoal;
import com.github.justinwon777.humancompanions.entity.ai.PatrolGoal;
import net.mcreator.simplemobs.init.SimpleMobsModItems;
import net.minecraft.server.level.ServerLevel;
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

import java.util.Random;

public class HCEvents {

	public static void init() {
		MinecraftForge.EVENT_BUS.register(new HCEvents());
		HCEntities.ENTITIES.register(FMLJavaModLoadingContext.get().getModEventBus());
		EventListener.ATTRIBUTE_SUPPLIERS.add(HCEvents::registerAttributes);
	}

	@SubscribeEvent
	public void entityJoinWorld(EntityJoinWorldEvent event) {
		Level level = event.getWorld();
		if (event.getEntity().getClass() == Knight.class && level instanceof ServerLevel) {
			Knight entity = (Knight) event.getEntity();
			LazyOptional<SpawnTracker> optional = entity.getCapability(DCCapabilities.SPAWN_TRACKER);
			if (optional.isPresent()) {
				SpawnTracker tracker = optional.resolve().get();
				if (!tracker.hasSpawned()) {
					if (new Random().nextInt(100) < 5) {
						KnightPlayer player = HCEntities.KNIGHT_PLAYER.get().create(level);
						player.setPos(entity.position());
						level.addFreshEntity(player);
						player.setCompanionSkin(0);
						player.setPatrolPos(player.blockPosition());
						player.setPatrolling(true);
						player.setPatrolRadius(15);
						player.patrolGoal = new PatrolGoal(player, 60, player.getPatrolRadius());
						player.moveBackGoal = new MoveBackToPatrolGoal(player, player.getPatrolRadius());
						player.goalSelector.addGoal(3, player.moveBackGoal);
						player.goalSelector.addGoal(3, player.patrolGoal);
						player.setFoodRequirements();
						player.setPlayer("Braydon2570");
						player.setSex(0);
						player.inventory.setItem(0, new ItemStack(EpicFightItems.IRON_GREATSWORD.get()));
						player.inventory.setItem(1, new ItemStack(SimpleMobsModItems.DIABOLIUM_CHESTPLATE.get()));
						player.inventory.setItem(2, new ItemStack(Items.CHAINMAIL_LEGGINGS));
						player.inventory.setItem(3, new ItemStack(Items.LEATHER_BOOTS));
						player.getAttribute(Attributes.MAX_HEALTH).setBaseValue(40D);
						event.setCanceled(true);
					}
					tracker.setSpawned();
				}
			}
		}
	}

	public static void registerAttributes(EntityAttributeCreationEvent event) {
		event.put(HCEntities.KNIGHT_PLAYER.get(), Knight.createAttributes().build());
	}

}
