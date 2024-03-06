package com.afunproject.dawncraft.client;

import com.afunproject.dawncraft.Constants;
import com.afunproject.dawncraft.DCItemTags;
import com.afunproject.dawncraft.client.entity.FrogRenderer;
import com.afunproject.dawncraft.dungeon.item.DungeonItems;
import com.afunproject.dawncraft.effects.DawnCraftEffects;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityViewRenderEvent.CameraSetup;
import net.minecraftforge.client.event.InputEvent.MouseScrollEvent;
import net.minecraftforge.client.event.RenderHandEvent;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber(modid = Constants.MODID, value = Dist.CLIENT)
public class ClientEventListener {
	
	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public void renderLivingEventStart(RenderLivingEvent.Pre<?, ?> event) {
		LivingEntity entity = event.getEntity();
		if (event.getRenderer() instanceof FrogRenderer) {
			event.setCanceled(true);
		}
		else if (entity.hasEffect(DawnCraftEffects.FROGFORM.get())) {
			event.setCanceled(true);
			float pt = event.getPartialTick();
			FrogRenderer.INSTANCE.render(entity, Mth.lerp(pt, entity.yRotO, entity.getYRot()), pt, event.getPoseStack(), event.getMultiBufferSource(), event.getPackedLight());
		}
	}

	@SubscribeEvent(priority = EventPriority.LOWEST, receiveCanceled = true)
	public void renderLivingEventEnd(RenderLivingEvent.Pre<?, ?> event) {
		if (event.getRenderer() instanceof FrogRenderer) {
			event.setCanceled(false);
		}
	}

	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public void renderHandEventStart(RenderHandEvent event) {
		Minecraft mc = Minecraft.getInstance();
		if (mc.player.hasEffect(DawnCraftEffects.FROGFORM.get())) event.setCanceled(true);
	}

	@SubscribeEvent
	public void renderCamera(CameraSetup event){
		Minecraft mc = Minecraft.getInstance();
		LocalPlayer player = mc.player;
		if (player.hasEffect(DawnCraftEffects.TREMOR.get())) {
			float a = (player.getEffect(DawnCraftEffects.TREMOR.get()).getAmplifier() + 1) * 0.5f;
			float t = (mc.getFrameTime() + player.tickCount)*a*0.75f;
			event.setPitch((float) (event.getPitch() + a * Math.sin((2*t) + 3)));
			event.setYaw((float) (event.getYaw() + a * Math.cos(t)));
			event.setRoll((float) (event.getRoll() + a * Math.sin(5 - (t*3))));
		}
	}

	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public void mouseScrollInput(MouseScrollEvent event) {
		Minecraft mc = Minecraft.getInstance();
		LocalPlayer player = mc.player;
		if (player != null) {
			if (player.hasEffect(DawnCraftEffects.IMMOBILIZED.get())) event.setCanceled(true);
		}
	}

	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public static void addTooltips(ItemTooltipEvent event) {
		ItemStack stack = event.getItemStack();
		if (stack.m_204117_(DCItemTags.MASKS) && stack.getItem() != DungeonItems.MASK_OF_ATHORA.get())
			event.getToolTip().add(new TranslatableComponent("tooltip.dawncraft.mask").withStyle(Style.EMPTY.withItalic(true).applyFormat(ChatFormatting.DARK_PURPLE)));
	}

	@SubscribeEvent
	public static void clientTick(TickEvent.PlayerTickEvent event) {
		if (event.phase == TickEvent.Phase.START) {
			Player player = event.player;
			if (player == null) return;
			if (player.hasEffect(DawnCraftEffects.FRACTURED_SOUL.get())) {
				if (player.tickCount % Math.floorDiv(10, player.getEffect(DawnCraftEffects.FRACTURED_SOUL.get()).getAmplifier() + 1) == 0) {
					player.level.addParticle(ParticleTypes.SOUL_FIRE_FLAME, player.getRandomX(0.5D), player.getRandomY(), player.getRandomZ(0.5D), 0.0D, 0.0D, 0.0D);
				}
			}
		}
	}

	/*@SuppressWarnings("deprecation")
	@SubscribeEvent
	public static void renderWorld(RenderLevelStageEvent event){
		Minecraft mc = Minecraft.getInstance();
		LocalPlayer player = mc.player;
		ItemStack stack = player.getMainHandItem();
		if (stack.getItem() == DungeonItems.DUNGEON_CONFIGURATOR.get()) {
			BlockPos pos = DungeonConfiguratorItem.getSelectedPos(stack);
			if (pos != null) {
				BufferBuilder buffer = Tesselator.getInstance().getBuilder();
				buffer.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR);
				RenderingUtils.renderCubeQuad(buffer, 0, 0, 0, 1, new Color(1, 1, 1, 1));
				buffer.end();
			}
		}
	}*/
}

