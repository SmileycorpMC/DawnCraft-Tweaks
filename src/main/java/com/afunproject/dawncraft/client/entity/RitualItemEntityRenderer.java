package com.afunproject.dawncraft.client.entity;

import com.afunproject.dawncraft.entities.RitualItemEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.Random;

public class RitualItemEntityRenderer extends EntityRenderer<RitualItemEntity> {

	private final ItemRenderer itemRenderer;
	private final Random random = new Random();

	public RitualItemEntityRenderer(EntityRendererProvider.Context p_174198_) {
		super(p_174198_);
		itemRenderer = p_174198_.getItemRenderer();
		shadowRadius = 0.15F;
		shadowStrength = 0.75F;
	}

	protected int getRenderAmount(ItemStack p_115043_) {
		int i = 1;
		if (p_115043_.getCount() > 48) {
			i = 5;
		} else if (p_115043_.getCount() > 32) {
			i = 4;
		} else if (p_115043_.getCount() > 16) {
			i = 3;
		} else if (p_115043_.getCount() > 1) {
			i = 2;
		}

		return i;
	}

	@Override
	public void render(RitualItemEntity p_115036_, float p_115037_, float p_115038_, PoseStack p_115039_, MultiBufferSource p_115040_, int p_115041_) {
		p_115039_.pushPose();
		ItemStack itemstack = p_115036_.getItem();
		int i = itemstack.isEmpty() ? 187 : Item.getId(itemstack.getItem()) + itemstack.getDamageValue();
		random.setSeed((long)i);
		BakedModel bakedmodel = itemRenderer.getModel(itemstack, p_115036_.level, (LivingEntity)null, p_115036_.getId());
		boolean flag = bakedmodel.isGui3d();
		int j = this.getRenderAmount(itemstack);
		p_115039_.scale(2f, 2f, 2f);
		p_115039_.mulPose(entityRenderDispatcher.cameraOrientation());
		if (!flag) {
			float f7 = -0.0F * (float)(j - 1) * 0.5F;
			float f8 = -0.0F * (float)(j - 1) * 0.5F;
			float f9 = -0.09375F * (float)(j - 1) * 0.5F;
			p_115039_.translate((double)f7, (double)f8, (double)f9);
		}

		for(int k = 0; k < j; ++k) {
			p_115039_.pushPose();
			if (k > 0) {
				if (flag) {
					float f11 = (random.nextFloat() * 2.0F - 1.0F) * 0.15F;
					float f13 = (random.nextFloat() * 2.0F - 1.0F) * 0.15F;
					float f10 = (random.nextFloat() * 2.0F - 1.0F) * 0.15F;
					p_115039_.translate(shouldSpreadItems() ? f11 : 0, shouldSpreadItems() ? f13 : 0, shouldSpreadItems() ? f10 : 0);
				} else {
					float f12 = (random.nextFloat() * 2.0F - 1.0F) * 0.15F * 0.5F;
					float f14 = (random.nextFloat() * 2.0F - 1.0F) * 0.15F * 0.5F;
					p_115039_.translate(shouldSpreadItems() ? f12 : 0, shouldSpreadItems() ? f14 : 0, 0.0D);
				}
			}

			itemRenderer.render(itemstack, ItemTransforms.TransformType.GROUND, false, p_115039_, p_115040_, p_115041_, OverlayTexture.NO_OVERLAY, bakedmodel);
			p_115039_.popPose();
			if (!flag) {
				p_115039_.translate(0.0, 0.0, 0.09375F);
			}
		}

		p_115039_.popPose();
		super.render(p_115036_, p_115037_, p_115038_, p_115039_, p_115040_, p_115041_);
	}

	@Override
	@SuppressWarnings("deprecation")
	public ResourceLocation getTextureLocation(RitualItemEntity p_115034_) {
		return TextureAtlas.LOCATION_BLOCKS;
	}

	/*==================================== FORGE START ===========================================*/

	/**
	 * @return If items should spread out when rendered in 3D
	 */
	public boolean shouldSpreadItems() {
		return true;
	}

	/**
	 * @return If items should have a bob effect
	 */
	public boolean shouldBob() {
		return true;
	}
}
