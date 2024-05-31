package com.afunproject.dawncraft.mixin;

import com.afunproject.dawncraft.dungeon.item.MaskItem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.ItemTransforms.TransformType;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemRenderer.class)
public abstract class MixinItemRenderer {

	@SuppressWarnings("deprecation")
	@Inject(at=@At("HEAD"), method = "render(Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/client/renderer/block/model/ItemTransforms$TransformType;ZLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;IILnet/minecraft/client/resources/model/BakedModel;)V", cancellable = true)
	public void dctweaks$render(ItemStack stack, TransformType transforms, boolean p_115146_, PoseStack p_115147_, MultiBufferSource p_115148_, int p_115149_, int p_115150_, BakedModel base, CallbackInfo callback) {
		if (stack != null && stack.getItem() instanceof MaskItem && transforms == TransformType.GUI) {
			Minecraft mc = Minecraft.getInstance();
			ResourceLocation loc = Registry.ITEM.getKey(stack.getItem());
			BakedModel model = mc.getModelManager().getModel(new ResourceLocation(loc.toString()+"_gui"));
			p_115147_.pushPose();
			p_115147_.translate(-0.5D, -0.5D, -0.5D);
			RenderType rendertype = ItemBlockRenderTypes.getRenderType(stack, true);
			VertexConsumer vertexconsumer;
			vertexconsumer = ItemRenderer.getFoilBufferDirect(p_115148_, rendertype, true, stack.hasFoil());
			renderModelLists(model, stack, p_115149_, p_115150_, p_115147_, vertexconsumer);
			p_115147_.popPose();
			callback.cancel();
		}
	}

	@Shadow
	public abstract void renderModelLists(BakedModel p_115190_, ItemStack p_115191_, int p_115192_, int p_115193_, PoseStack p_115194_, VertexConsumer p_115195_);

}