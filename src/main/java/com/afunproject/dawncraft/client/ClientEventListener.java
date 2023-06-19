package com.afunproject.dawncraft.client;

import com.afunproject.dawncraft.Constants;
import com.afunproject.dawncraft.client.entity.FrogRenderer;
import com.afunproject.dawncraft.dungeon.item.DungeonItems;
import com.afunproject.dawncraft.effects.DawnCraftEffects;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.ChatFormatting;
import net.minecraft.client.CameraType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.Registry;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.tags.TagKey;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityViewRenderEvent.CameraSetup;
import net.minecraftforge.client.event.InputEvent.MouseScrollEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.client.event.RenderHandEvent;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber(modid = Constants.MODID, value = Dist.CLIENT)
public class ClientEventListener {

	public static final TagKey<Item> MASK_TAG = TagKey.m_203882_(Registry.ITEM_REGISTRY, Constants.loc("masks"));

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
	public void renderOverlay(RenderGameOverlayEvent.Pre event) {
		Minecraft mc = Minecraft.getInstance();
		//hide during quest screen
		//if (mc.screen != null) return;
		//render minimap
		if (event.getType() == ElementType.LAYER)  {
			PoseStack poseStack = event.getMatrixStack();
			poseStack.pushPose();
			poseStack.clear();
			poseStack.popPose();
			LocalPlayer player = mc.player;
			if (mc.options.renderDebug || player.isUsingItem()) return;
			ItemStack stack = null;
			if (player.getItemInHand(InteractionHand.MAIN_HAND).getItem() == Items.FILLED_MAP) {
				stack = player.getItemInHand(InteractionHand.MAIN_HAND);
			}
			else if (player.getItemInHand(InteractionHand.OFF_HAND).getItem() == Items.FILLED_MAP) {
				stack = player.getItemInHand(InteractionHand.OFF_HAND);
			}
			if (stack != null) {
				boolean fullscreen = player.getUseItem().isEmpty() && mc.options.keyUse.isDown();
				if (fullscreen) {
					event.setCanceled(true);
					if (!mc.options.getCameraType().isFirstPerson()) mc.options.setCameraType(CameraType.FIRST_PERSON);
				}
				MinimapRenderer.renderBasicMinimap(poseStack, stack, player.getUseItem().isEmpty() && mc.options.keyUse.isDown());
			}
		}
	}

	@SubscribeEvent
	public void renderCamera(CameraSetup event){
		Minecraft mc = Minecraft.getInstance();
		LocalPlayer player = mc.player;
		if (player.hasEffect(DawnCraftEffects.TREMOR.get())) {
			float a = (player.getEffect(DawnCraftEffects.TREMOR.get()).getAmplifier() + 1)*0.5f;
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
		if (stack.m_204117_(MASK_TAG) && stack.getItem() != DungeonItems.CURSED_MASK.get())
			event.getToolTip().add(new TranslatableComponent("tooltip.dawncraft.mask").withStyle(Style.EMPTY.withItalic(true).applyFormat(ChatFormatting.DARK_PURPLE)));
	}

	@SubscribeEvent
	public static void clientTick(TickEvent.ClientTickEvent event) {
		if (event.phase == TickEvent.Phase.START) {
			Minecraft mc = Minecraft.getInstance();
			LocalPlayer player = mc.player;
			if (player != null && player.hasEffect(DawnCraftEffects.FRACTURED_SOUL.get())) {
				if (player.tickCount % Math.floorDiv(10, player.getEffect(DawnCraftEffects.FRACTURED_SOUL.get()).getAmplifier() + 1) == 0) {
					mc.level.addParticle(ParticleTypes.SOUL_FIRE_FLAME, player.getRandomX(0.5D), player.getRandomY(), player.getRandomZ(0.5D), 0.0D, 0.0D, 0.0D);
				}
			}
		}
	}

	/*@SuppressWarnings("deprecation")
	@SubscribeEvent
	public static void renderWorld(RenderLevelStageEvent event){
		DawnCraft.logInfo("bing");
		Minecraft mc = Minecraft.getInstance();
		LocalPlayer player = mc.player;
		ItemStack stack = player.getMainHandItem();
		if (stack.getItem() == DungeonItems.DUNGEON_CONFIGURATOR.get()) {
			DawnCraft.logInfo("bong");
			BlockPos pos = DungeonConfiguratorItem.getSelectedPos(stack);
			if (pos != null) {
				BufferBuilder buffer = Tesselator.getInstance().getBuilder();
				buffer.begin(Mode.QUADS, DefaultVertexFormat.POSITION_COLOR_TEX_LIGHTMAP);
				RenderingUtils.renderCubeQuad(buffer, 1, 1, 1, 1, new Color(5636095),
						mc.getTextureAtlas(TextureAtlas.LOCATION_BLOCKS).apply(new ResourceLocation("white_concrete")), mc.level, 15, pos);
				buffer.end();
			}
		}
	}*/
}

