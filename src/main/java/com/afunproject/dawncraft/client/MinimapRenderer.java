package com.afunproject.dawncraft.client;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Matrix4f;
import com.mojang.math.Vector3f;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.MapRenderer;
import net.minecraft.client.gui.MapRenderer.MapInstance;
import net.minecraft.client.renderer.ItemInHandRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.MapItem;
import net.minecraft.world.level.saveddata.maps.MapDecoration;
import net.minecraft.world.level.saveddata.maps.MapItemSavedData;

public class MinimapRenderer {

	public static void renderBasicMinimap(PoseStack poseStack, ItemStack map, boolean fullscreen) {
		Minecraft mc = Minecraft.getInstance();
		float scale = fullscreen ? 1.5f : 0.5f;
		float offsetX = fullscreen ? (mc.getWindow().getGuiScaledWidth()/2f) / scale - 64f : 12;
		float offsetY = fullscreen ? (mc.getWindow().getGuiScaledHeight()/2f) / scale - 64f : 12;
		int id = MapItem.getMapId(map);
		MapItemSavedData mapdata = MapItem.getSavedData(id, mc.level);
		GlStateManager._enableBlend();
		GlStateManager._blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA.value, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA.value,
				GlStateManager.SourceFactor.ONE.value, GlStateManager.DestFactor.ZERO.value);
		//map background
		renderMapBackground(poseStack, mapdata == null, scale, offsetX, offsetY);
		//map blocks
		if (mapdata != null) {
			renderVanillaMapBlocks(poseStack, id, mapdata, scale, offsetX, offsetY);
			//map decoration items
			renderMapDecorations(poseStack, mapdata, scale, offsetX, offsetY);
			GlStateManager._disableBlend();
		}
	}

	private static void renderMapBackground(PoseStack poseStack, boolean hasData, float scale, float offsetX, float offsetY) {
		Minecraft mc = Minecraft.getInstance();
		poseStack.pushPose();
		poseStack.scale(scale, scale, 0.5f);
		MultiBufferSource buffers = mc.renderBuffers().bufferSource();
		VertexConsumer bg_vertices = buffers.getBuffer(hasData ?
				ItemInHandRenderer.MAP_BACKGROUND : ItemInHandRenderer.MAP_BACKGROUND_CHECKERBOARD);
		Matrix4f bg = poseStack.last().pose();
		bg_vertices.vertex(bg, offsetX - 7.0F, offsetY + 135.0F, -1F).color(255, 255, 255, 255).uv(0.0F, 1.0F).uv2(255).endVertex();
		bg_vertices.vertex(bg, offsetX + 135.0F, offsetY + 135.0F, -1F).color(255, 255, 255, 255).uv(1.0F, 1.0F).uv2(255).endVertex();
		bg_vertices.vertex(bg, offsetX + 135.0F, offsetY - 7.0F, -1F).color(255, 255, 255, 255).uv(1.0F, 0.0F).uv2(255).endVertex();
		bg_vertices.vertex(bg, offsetX - 7.0F, offsetY - 7.0F, -1F).color(255, 255, 255, 255).uv(0.0F, 0.0F).uv2(255).endVertex();
		poseStack.popPose();
	}

	private static void renderVanillaMapBlocks(PoseStack poseStack, int mapId, MapItemSavedData mapData, float scale, float offsetX, float offsetY) {
		Minecraft mc = Minecraft.getInstance();
		poseStack.pushPose();
		poseStack.scale(scale, scale, 0.5f);
		poseStack.translate(offsetX, offsetY, 0);
		MapInstance instance = mc.gameRenderer.getMapRenderer().getOrCreateMapInstance(mapId, mapData);
		if (instance.requiresUpload) {
			instance.updateTexture();
			instance.requiresUpload = false;
		}
		MultiBufferSource buffers = mc.renderBuffers().bufferSource();
		Matrix4f matrix4f = poseStack.last().pose();
		VertexConsumer vertexconsumer = buffers.getBuffer(instance.renderType);
		vertexconsumer.vertex(matrix4f, 0.0F, 128.0F, -0.01F).color(255, 255, 255, 255).uv(0.0F, 1.0F).uv2(255).endVertex();
		vertexconsumer.vertex(matrix4f, 128.0F, 128.0F, -0.01F).color(255, 255, 255, 255).uv(1.0F, 1.0F).uv2(255).endVertex();
		vertexconsumer.vertex(matrix4f, 128.0F, 0.0F, -0.01F).color(255, 255, 255, 255).uv(1.0F, 0.0F).uv2(255).endVertex();
		vertexconsumer.vertex(matrix4f, 0.0F, 0.0F, -0.01F).color(255, 255, 255, 255).uv(0.0F, 0.0F).uv2(255).endVertex();
		poseStack.popPose();
	}

	private static void renderMapDecorations(PoseStack poseStack, MapItemSavedData mapData, float scale, float offsetX, float offsetY) {
		Minecraft mc = Minecraft.getInstance();
		MultiBufferSource buffers = mc.renderBuffers().bufferSource();
		int k = 0;
		for(MapDecoration mapdecoration : mapData.getDecorations()) {
			poseStack.pushPose();
			poseStack.scale(scale, scale, 0.5f);
			poseStack.translate(offsetX, offsetY, k+2);
			if (mapdecoration.render(k)) {
				k++;
				poseStack.popPose();
				continue;
			}
			poseStack.translate((double)(0.0F + (float)mapdecoration.getX() / 2.0F + 64.0F), (double)(0.0F + (float)mapdecoration.getY() / 2.0F + 64.0F), 0);
			poseStack.mulPose(Vector3f.ZP.rotationDegrees((float)(mapdecoration.getRot() * 360)/16f));
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
			vertexconsumer1.vertex(matrix4f1, -1.0F, -1.0F,0).color(255, 255, 255, 255).uv(f1, f4).uv2(255).endVertex();
			poseStack.popPose();
			if (mapdecoration.getName() != null) {
				Font font = mc.font;
				Component component = mapdecoration.getName();
				float f6 = (float)font.width(component);
				float f7 = Mth.clamp(25.0F / f6, 0.0F, 6.0F / 9.0F);
				poseStack.pushPose();
				poseStack.translate((double)(0.0F + (float)mapdecoration.getX() / 2.0F + 64.0F - f6 * f7 / 2.0F), (double)(0.0F + (float)mapdecoration.getY() / 2.0F + 64.0F + 4.0F), k+2);
				poseStack.scale(f7, f7, 1.0F);
				font.drawInBatch(component, 0.0F, 0.0F, -1, false, poseStack.last().pose(), buffers, false, Integer.MIN_VALUE, 255);
				poseStack.popPose();
			}
			++k;
		}
	}

}
