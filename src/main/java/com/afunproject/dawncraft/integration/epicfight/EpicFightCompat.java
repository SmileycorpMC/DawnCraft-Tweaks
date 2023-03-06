package com.afunproject.dawncraft.integration.epicfight;

import com.afunproject.dawncraft.Constants;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLEnvironment;
import yesman.epicfight.api.animation.types.MainFrameAnimation;
import yesman.epicfight.api.animation.types.StaticAnimation;
import yesman.epicfight.api.client.model.ClientModels;
import yesman.epicfight.api.forgeevent.AnimationRegistryEvent;
import yesman.epicfight.api.model.Model;
import yesman.epicfight.api.utils.EpicFightDamageSource;
import yesman.epicfight.gameasset.Models;
import yesman.epicfight.world.capabilities.EpicFightCapabilities;
import yesman.epicfight.world.capabilities.entitypatch.EntityPatch;
import yesman.epicfight.world.capabilities.entitypatch.LivingEntityPatch;
import yesman.epicfight.world.capabilities.entitypatch.player.PlayerPatch;

public class EpicFightCompat {

	public static StaticAnimation EAT_LEFT;
	public static StaticAnimation EAT_RIGHT;
	public static StaticAnimation DRINK_LEFT;
	public static StaticAnimation DRINK_RIGHT;

	public static void init() {
		FMLJavaModLoadingContext.get().getModEventBus().addListener(EpicFightCompat::registerAnimations);
		MinecraftForge.EVENT_BUS.register(new EpicFightCompat());
	}

	public static void registerAnimations(AnimationRegistryEvent event) {
		event.getRegistryMap().put(Constants.MODID, EpicFightCompat::build);
	}

	private static void build() {
		Models<?> models = FMLEnvironment.dist == Dist.CLIENT ? ClientModels.LOGICAL_CLIENT : Models.LOGICAL_SERVER;
		Model biped = models.biped;
		EAT_LEFT = new MainFrameAnimation(0.15F, "biped/living/eat_left", biped);
		EAT_RIGHT = new MainFrameAnimation(0.15F, "biped/living/eat_right", biped);
		DRINK_LEFT = new MainFrameAnimation(0.15F, "biped/living/drink_left", biped);
		DRINK_RIGHT = new MainFrameAnimation(0.15F, "biped/living/drink_right", biped);
	}

	@SubscribeEvent
	@SuppressWarnings("rawtypes")
	public void itemTick(LivingEntityUseItemEvent.Tick event) {
		if (event.getEntityLiving() instanceof Player) {
			Player player = (Player) event.getEntityLiving();
			ItemStack stack = event.getItem();
			if (stack.getUseAnimation() == UseAnim.EAT) {
				LazyOptional<EntityPatch> optional = player.getCapability(EpicFightCapabilities.CAPABILITY_ENTITY);
				if (optional.isPresent()) {
					((LivingEntityPatch<?>)optional.resolve().get()).playAnimationSynchronized(
							(player.getMainArm() == HumanoidArm.LEFT && player.getUsedItemHand() == InteractionHand.MAIN_HAND)
							|| player.getUsedItemHand() == InteractionHand.OFF_HAND ? EpicFightCompat.EAT_LEFT : EpicFightCompat.EAT_RIGHT, 0.0F);
				}
			} else if (stack.getUseAnimation() == UseAnim.DRINK) {
				LazyOptional<EntityPatch> optional = player.getCapability(EpicFightCapabilities.CAPABILITY_ENTITY);
				if (optional.isPresent()) {
					((LivingEntityPatch<?>)optional.resolve().get()).playAnimationSynchronized(
							(player.getMainArm() == HumanoidArm.LEFT && player.getUsedItemHand() == InteractionHand.MAIN_HAND)
							|| player.getUsedItemHand() == InteractionHand.OFF_HAND ? EpicFightCompat.DRINK_LEFT : EpicFightCompat.DRINK_RIGHT, 0.0F);
				}
			}
		}
	}

	@SubscribeEvent
	public void livingHurtEvent(LivingDamageEvent event) {
		if (!event.getEntity().level.isClientSide) {
			DamageSource source = event.getSource();
			if (source.getMsgId().equals("player") &! (source instanceof EpicFightDamageSource)) {
				event.setAmount(event.getAmount()/2f);
			}
		}
	}

	@SuppressWarnings("rawtypes")
	public static boolean isCombatMode(Player player) {
		LazyOptional<EntityPatch> optional = player.getCapability(EpicFightCapabilities.CAPABILITY_ENTITY);
		if (optional.isPresent()) if (((PlayerPatch<?>)optional.resolve().get()).isBattleMode()) return true;
		return false;
	}

}
