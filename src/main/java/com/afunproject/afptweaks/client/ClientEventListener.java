package com.afunproject.afptweaks.client;

import com.afunproject.afptweaks.ModDefinitions;
import com.afunproject.afptweaks.client.screens.QuestScreen;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.platform.Window;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Matrix4f;
import com.mojang.math.Vector3f;

import net.minecraft.client.CameraType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.MapRenderer;
import net.minecraft.client.gui.MapRenderer.MapInstance;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.ItemInHandRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.MapItem;
import net.minecraft.world.level.saveddata.maps.MapDecoration;
import net.minecraft.world.level.saveddata.maps.MapItemSavedData;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = ModDefinitions.MODID, value = Dist.CLIENT)
public class ClientEventListener {

	@SubscribeEvent
	public void postRenderOverlay(RenderGameOverlayEvent.Pre event){
		Minecraft mc = Minecraft.getInstance();
		//hide during quest screen
		if (mc.screen instanceof QuestScreen) {
			event.setCanceled(true);
			return;
		}
		//render minimap
		LocalPlayer player = mc.player;
		ItemStack stack = null;
		if (player.getItemInHand(InteractionHand.MAIN_HAND).getItem() == Items.FILLED_MAP) {
			stack = player.getItemInHand(InteractionHand.MAIN_HAND);
		}
		else if (player.getItemInHand(InteractionHand.OFF_HAND).getItem() == Items.FILLED_MAP) {
			stack = player.getItemInHand(InteractionHand.OFF_HAND);
		}
		if (stack != null) {
			float scale = 0.5f;
			float offsetX = 12;
			float offsetY = 12;
			if (player.getUseItem().isEmpty() && mc.options.keyUse.isDown()) {
				scale = scale*3;
				Window window = mc.getWindow();
				offsetX = (window.getGuiScaledWidth()/2f) / scale;
				offsetY = (window.getGuiScaledHeight()/2f) /scale;
				event.setCanceled(true);
				if (!mc.options.getCameraType().isFirstPerson()) mc.options.setCameraType(CameraType.FIRST_PERSON);
			}
			int id = MapItem.getMapId(stack);
			PoseStack poseStack = event.getMatrixStack();
			poseStack.pushPose();
			poseStack.scale(scale, scale, 0.5f);
			GlStateManager._enableBlend();
			GlStateManager._blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA.value, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA.value,
					GlStateManager.SourceFactor.ONE.value, GlStateManager.DestFactor.ZERO.value);
			MultiBufferSource buffers = mc.renderBuffers().bufferSource();
			MapItemSavedData mapdata = MapItem.getSavedData(id, mc.level);
			VertexConsumer bg_vertices = buffers.getBuffer(mapdata == null ?
					ItemInHandRenderer.MAP_BACKGROUND : ItemInHandRenderer.MAP_BACKGROUND_CHECKERBOARD);
			Matrix4f bg = poseStack.last().pose();
			bg_vertices.vertex(bg, offsetX - 7.0F, offsetY + 135.0F, 0.0F).color(255, 255, 255, 255).uv(0.0F, 1.0F).uv2(255).endVertex();
			bg_vertices.vertex(bg, offsetX + 135.0F, offsetY + 135.0F, 0.0F).color(255, 255, 255, 255).uv(1.0F, 1.0F).uv2(255).endVertex();
			bg_vertices.vertex(bg, offsetX + 135.0F, offsetY - 7.0F, 0.0F).color(255, 255, 255, 255).uv(1.0F, 0.0F).uv2(255).endVertex();
			bg_vertices.vertex(bg, offsetX - 7.0F, offsetY - 7.0F, 0.0F).color(255, 255, 255, 255).uv(0.0F, 0.0F).uv2(255).endVertex();
			poseStack.popPose();
			if (mapdata != null) {
				poseStack.pushPose();
				poseStack.scale(scale, scale, 0.5f);
				poseStack.translate(offsetX, offsetY, scale);
				MapInstance instance = mc.gameRenderer.getMapRenderer().getOrCreateMapInstance(id, mapdata);
				if (instance.requiresUpload) {
					instance.updateTexture();
					instance.requiresUpload = false;
				}

				Matrix4f matrix4f = poseStack.last().pose();
				VertexConsumer vertexconsumer = buffers.getBuffer(instance.renderType);
				vertexconsumer.vertex(matrix4f, 0.0F, 128.0F, -0.01F).color(255, 255, 255, 255).uv(0.0F, 1.0F).uv2(255).endVertex();
				vertexconsumer.vertex(matrix4f, 128.0F, 128.0F, -0.01F).color(255, 255, 255, 255).uv(1.0F, 1.0F).uv2(255).endVertex();
				vertexconsumer.vertex(matrix4f, 128.0F, 0.0F, -0.01F).color(255, 255, 255, 255).uv(1.0F, 0.0F).uv2(255).endVertex();
				vertexconsumer.vertex(matrix4f, 0.0F, 0.0F, -0.01F).color(255, 255, 255, 255).uv(0.0F, 0.0F).uv2(255).endVertex();
				poseStack.popPose();
				int k = 0;
				for(MapDecoration mapdecoration : mapdata.getDecorations()) {
					poseStack.pushPose();
					poseStack.scale(scale, scale, 0.5f);
					poseStack.translate(offsetX, offsetY, (k+2)*scale*2);
					if (mapdecoration.render(k)) {
						k++;
						poseStack.popPose();
						continue;
					}
					poseStack.translate((double)(0.0F + (float)mapdecoration.getX() / 2.0F + 64.0F), (double)(0.0F + (float)mapdecoration.getY() / 2.0F + 64.0F), 0);
					poseStack.mulPose(Vector3f.ZP.rotationDegrees((float)(mapdecoration.getRot() * 360) / 16.0F));
					poseStack.scale(8.0F, 8.0F, 3.0F);
					poseStack.translate(-0.125D, 0.125D, 0.0D);
					byte b0 = mapdecoration.getImage();
					float f1 = (float)(b0 % 16 + 0) / 16.0F;
					float f2 = (float)(b0 / 16 + 0) / 16.0F;
					float f3 = (float)(b0 % 16 + 1) / 16.0F;
					float f4 = (float)(b0 / 16 + 1) / 16.0F;
					Matrix4f matrix4f1 = poseStack.last().pose();
					VertexConsumer vertexconsumer1 = buffers.getBuffer(MapRenderer.MAP_ICONS);
					vertexconsumer1.vertex(matrix4f1, -1.0F, 1.0F, 0).color(255, 255, 255, 255).uv(f1, f2).uv2(255).endVertex();
					vertexconsumer1.vertex(matrix4f1, 1.0F, 1.0F, 0).color(255, 255, 255, 255).uv(f3, f2).uv2(255).endVertex();
					vertexconsumer1.vertex(matrix4f1, 1.0F, -1.0F, 0).color(255, 255, 255, 255).uv(f3, f4).uv2(255).endVertex();
					vertexconsumer1.vertex(matrix4f1, -1.0F, -1.0F, 0).color(255, 255, 255, 255).uv(f1, f4).uv2(255).endVertex();
					poseStack.popPose();
					if (mapdecoration.getName() != null) {
						Font font = mc.font;
						Component component = mapdecoration.getName();
						float f6 = (float)font.width(component);
						float f7 = Mth.clamp(25.0F / f6, 0.0F, 6.0F / 9.0F);
						poseStack.pushPose();
						poseStack.translate((double)(0.0F + (float)mapdecoration.getX() / 2.0F + 64.0F - f6 * f7 / 2.0F), (double)(0.0F + (float)mapdecoration.getY() / 2.0F + 64.0F + 4.0F), (k+2)*scale*2);
						poseStack.scale(f7, f7, 1.0F);
						font.drawInBatch(component, 0.0F, 0.0F, -1, false, poseStack.last().pose(), buffers, false, Integer.MIN_VALUE, 255);
						poseStack.popPose();
					}
					++k;
				}
				GlStateManager._disableBlend();
			}
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

