package com.afunproject.dawncraft.client.render.blockentity;

import com.afunproject.dawncraft.dungeon.KeyColour;
import com.afunproject.dawncraft.dungeon.block.DungeonDoorBlock;
import com.afunproject.dawncraft.dungeon.block.entity.DungeonDoorBlockEntity;
import com.afunproject.dawncraft.dungeon.item.DungeonItems;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms.TransformType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider.Context;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.Direction.Axis;
import net.minecraft.world.item.ItemStack;

public class DungeonDoorBlockEntityRenderer implements BlockEntityRenderer<DungeonDoorBlockEntity> {

	public DungeonDoorBlockEntityRenderer(Context ctx) {}

	@Override
	public void render(DungeonDoorBlockEntity entity, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int light, int combinedOverlay) {
		KeyColour colour = entity.getLockColour();
		if (colour != null) {
			Axis axis = entity.getBlockState().getValue(DungeonDoorBlock.AXIS);
			ItemStack stack = new ItemStack(DungeonItems.getLock(colour));
			if (axis == Axis.X) {
				renderItem(stack, partialTicks, poseStack, buffer, light, 0.3d, 0.5d, 0.5d, axis);
				renderItem(stack, partialTicks, poseStack, buffer, light, 0.7d, 0.5d, 0.5d, axis);
			} else  {
				renderItem(stack, partialTicks, poseStack, buffer, light, 0.5d, 0.5d, 0.3d, axis);
				renderItem(stack, partialTicks, poseStack, buffer, light, 0.5d, 0.5d, 0.7d, axis);
			}
		}
	}

	private void renderItem(ItemStack stack, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int light, double x, double y, double z, Axis axis) {
		poseStack.pushPose();
		poseStack.translate(x, y, z);
		poseStack.scale(0.5f, 0.5f, 0.5f);
		if (axis == Axis.X) {
			poseStack.mulPose(Vector3f.YP.rotationDegrees(90));
		}
		Minecraft mc = Minecraft.getInstance();
		mc.getItemRenderer().renderStatic(stack, TransformType.FIXED, light, OverlayTexture.NO_OVERLAY, poseStack, buffer, 0);
		poseStack.popPose();
	}

}
