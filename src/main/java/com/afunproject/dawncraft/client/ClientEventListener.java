package com.afunproject.dawncraft.client;

import com.afunproject.dawncraft.ModDefinitions;
import com.afunproject.dawncraft.client.screens.QuestScreen;
import com.afunproject.dawncraft.effects.DawnCraftEffects;
import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.CameraType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityViewRenderEvent.CameraSetup;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = ModDefinitions.MODID, value = Dist.CLIENT)
public class ClientEventListener {

	@SubscribeEvent
	public void renderOverlay(RenderGameOverlayEvent.Pre event){
		Minecraft mc = Minecraft.getInstance();
		//hide during quest screen
		if (mc.screen instanceof QuestScreen) {
			event.setCanceled(true);
			return;
		}
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
	public void renderWorld(CameraSetup event){
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

	/*@SubscribeEvent
	public void renderWorld(RenderLevelStageEvent event){
		if (event.getStage() == Stage.AFTER_SOLID_BLOCKS) {
			Minecraft mc = Minecraft.getInstance();
			LocalPlayer player = mc.player;
			ItemStack stack = player.getMainHandItem();
			if (stack.getItem() == DungeonItems.DUNGEON_CONFIGURATOR.get()) {
				BlockPos pos = DungeonConfiguratorItem.getSelectedPos(stack);
				if (pos != null) {
					RenderingUtils.renderCubeQuad(Tesselator.getInstance().getBuilder(), 1, 1, 1, 1, new Color(5636095),
							mc.getTextureAtlas(null).apply(new ResourceLocation("white_concrete")), mc.level, 15, pos);
				}
			}
		}
	}*/
}

