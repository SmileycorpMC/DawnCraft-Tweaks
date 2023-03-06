package com.afunproject.dawncraft.mixin;

import java.util.Map;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.afunproject.dawncraft.dungeon.item.MaskItem;
import com.google.common.collect.Maps;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.platform.Lighting;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

@Mixin(ItemRenderer.class)
public abstract class MixinItemRenderer {

	private static Map<ResourceLocation, BakedModel> MODEL_CACHE = Maps.newHashMap();

	@Shadow
	private TextureManager textureManager;

	@Shadow
	public float blitOffset;

	@SuppressWarnings("deprecation")
	@Inject(at=@At("HEAD"), method = "renderGuiItem(Lnet/minecraft/world/item/ItemStack;IILnet/minecraft/client/resources/model/BakedModel;)V", cancellable = true)
	protected void renderGuiItem(ItemStack stack, int x, int y, BakedModel base, CallbackInfo callback) {
		if (stack != null) {
			if (stack.getItem() instanceof MaskItem) {
				Minecraft mc = Minecraft.getInstance();
				ResourceLocation loc = Registry.ITEM.getKey(stack.getItem());
				/*if (!MODEL_CACHE.containsKey(loc)) {
					TextureAtlasSprite sprite = mc.getTextureAtlas(TextureAtlas.LOCATION_BLOCKS)
							.apply(new ResourceLocation(loc.getNamespace(), "item/"+loc.getPath()));
					BakedQuad quads = ItemTextureQuadConverter.genQuad(Transformation.identity(), 0, 0, 16, 16, 1, sprite, Direction.NORTH, 0xFFFFFFFF, 2);
					ImmutableMap<TransformType, Transformation> map = ImmutableMap.of(TransformType.GUI, Transformation.identity());
					MODEL_CACHE.put(loc, new BakedItemModel(ImmutableList.of(quads), sprite, map, base.getOverrides(), false, true));
				}*/
				BakedModel model = mc.getModelManager().getModel(new ResourceLocation(loc.toString()+"_gui"));
				//System.out.println(model + " " + mc.getModelManager().getMissingModel());
				textureManager.getTexture(TextureAtlas.LOCATION_BLOCKS).setFilter(false, false);
				RenderSystem.setShaderTexture(0, TextureAtlas.LOCATION_BLOCKS);
				RenderSystem.enableBlend();
				RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
				RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
				PoseStack posestack = RenderSystem.getModelViewStack();
				posestack.pushPose();
				posestack.translate((double)x, (double)y, (double)(100.0F + blitOffset));
				posestack.translate(8.0D, 8.0D, 0.0D);
				posestack.scale(1.0F, -1.0F, 1.0F);
				posestack.scale(16.0F, 16.0F, 16.0F);
				RenderSystem.applyModelViewMatrix();
				PoseStack posestack1 = new PoseStack();
				MultiBufferSource.BufferSource multibuffersource$buffersource = Minecraft.getInstance().renderBuffers().bufferSource();
				boolean flag = !model.usesBlockLight();
				if (flag) {
					Lighting.setupForFlatItems();
				}
				this.render(stack, ItemTransforms.TransformType.GUI, false, posestack1, multibuffersource$buffersource, 15728880, OverlayTexture.NO_OVERLAY, model);
				multibuffersource$buffersource.endBatch();
				RenderSystem.enableDepthTest();
				if (flag) {
					Lighting.setupFor3DItems();
				}

				posestack.popPose();
				RenderSystem.applyModelViewMatrix();
				callback.cancel();
			}
		}
	}

	@Shadow
	public abstract void render(ItemStack p_115144_, ItemTransforms.TransformType p_115145_, boolean p_115146_, PoseStack p_115147_, MultiBufferSource p_115148_, int p_115149_, int p_115150_, BakedModel p_115151_);


}