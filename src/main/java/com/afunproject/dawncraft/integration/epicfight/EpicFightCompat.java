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

	public static void init() {
		MinecraftForge.EVENT_BUS.register(new EpicFightCompat());
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
