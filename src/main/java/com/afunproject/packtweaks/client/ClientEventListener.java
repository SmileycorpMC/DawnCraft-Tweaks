package com.afunproject.packtweaks.client;

import com.afunproject.packtweaks.PackTweaks;

import net.minecraft.client.Minecraft;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderHandEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import yesman.epicfight.client.ClientEngine;
import yesman.epicfight.client.world.capabilites.entitypatch.player.LocalPlayerPatch;

@Mod.EventBusSubscriber(modid = PackTweaks.MODID, value = Dist.CLIENT)
public class ClientEventListener {

	private static boolean is_render_hand_active;

	//disable EpicFight battle mode if the player holds a map
	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public static void renderHandFirst(RenderHandEvent event) {
		LocalPlayerPatch patch = ClientEngine.instance.getPlayerPatch();
		if (patch.isBattleMode()) {
			if (event.getItemStack() != null) {
				Item item = event.getItemStack().getItem();
				if (item == Items.FILLED_MAP) {
					patch.toMiningMode(false);
					is_render_hand_active = true;
				}
			}
			if (event.getHand() == InteractionHand.OFF_HAND) {
				Minecraft mc = Minecraft.getInstance();
				if (mc.player != null) {
					ItemStack stack = mc.player.getItemInHand(InteractionHand.MAIN_HAND);
					if (stack != null) {
						if (stack.getItem() == Items.FILLED_MAP) {
							patch.toMiningMode(false);
							is_render_hand_active = true;
						}
					}
				}
			}
		}
	}

	//re-enable EpicFight battle mode after it's rendering has been bypassed
	@SubscribeEvent(priority = EventPriority.LOWEST)
	public static void renderHandLast(RenderHandEvent event) {
		LocalPlayerPatch patch = ClientEngine.instance.getPlayerPatch();
		if (event.getItemStack() != null && is_render_hand_active) {
			patch.toBattleMode(false);
			is_render_hand_active = false;
		}
	}

}
