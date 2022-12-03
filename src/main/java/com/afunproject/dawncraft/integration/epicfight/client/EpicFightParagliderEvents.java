package com.afunproject.dawncraft.integration.epicfight.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.EntityModel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.event.RenderHandEvent;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import tictim.paraglider.item.ParagliderItem;

public class EpicFightParagliderEvents {

	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public void renderLivingEventStart(RenderLivingEvent.Pre<? extends LivingEntity, ? extends EntityModel<? extends LivingEntity>> event) {
		LivingEntity entity = event.getEntity();
		if (isGliding(entity.getMainHandItem())) event.setCanceled(true);
	}

	@SubscribeEvent(priority = EventPriority.LOWEST, receiveCanceled = true)
	public void renderLivingEventEnd(RenderLivingEvent.Pre<? extends LivingEntity, ? extends EntityModel<? extends LivingEntity>> event) {
		LivingEntity entity = event.getEntity();
		if (isGliding(entity.getMainHandItem())) event.setCanceled(false);
	}

	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public void renderHandEventStart(RenderHandEvent event) {
		Minecraft mc = Minecraft.getInstance();
		if (isGliding(mc.player.getMainHandItem())) event.setCanceled(true);
	}

	@SubscribeEvent(priority = EventPriority.LOWEST, receiveCanceled = true)
	public void renderHandEventEnd(RenderHandEvent event) {
		Minecraft mc = Minecraft.getInstance();
		if (isGliding(mc.player.getMainHandItem())) event.setCanceled(false);
	}

	private static boolean isGliding(ItemStack stack) {
		if (!(stack.getItem() instanceof ParagliderItem)) return false;
		return ParagliderItem.isItemParagliding(stack);
	}

}
