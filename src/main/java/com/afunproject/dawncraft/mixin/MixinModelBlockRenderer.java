package com.afunproject.dawncraft.mixin;

import javax.annotation.Nullable;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.afunproject.dawncraft.DawnCraft;
import com.afunproject.dawncraft.client.model.block.DisguisedBakedModel;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.ModelBlockRenderer;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.model.data.IModelData;

@Mixin(ModelBlockRenderer.class)
public abstract class MixinModelBlockRenderer {

	@Inject(at=@At("HEAD"), method = "renderModel(Lcom/mojang/blaze3d/vertex/PoseStack$Pose;Lcom/mojang/blaze3d/vertex/VertexConsumer;Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/client/resources/model/BakedModel;FFFIILnet/minecraftforge/client/model/data/IModelData;)V", remap = false)
	public void renderModel(PoseStack.Pose p_111068_, VertexConsumer p_111069_, @Nullable BlockState p_111070_, BakedModel model, float p_111072_, float p_111073_, float p_111074_, int p_111075_, int p_111076_, IModelData modelData, CallbackInfo callback) {
		DawnCraft.logInfo("sus");
		if (model instanceof DisguisedBakedModel) {
			Minecraft mc = Minecraft.getInstance();
			Player player = mc.player;
			DawnCraft.logInfo("sus reee");
			if (player != null) if (player.isCreative()) renderModel(p_111068_, p_111069_, p_111070_, ((DisguisedBakedModel) model).getOverlayModel(), p_111072_, p_111073_, p_111074_, p_111075_, p_111076_, modelData);
		}
	}

	@Shadow
	public abstract void renderModel(PoseStack.Pose p_111068_, VertexConsumer p_111069_, @Nullable BlockState p_111070_, BakedModel model, float p_111072_, float p_111073_, float p_111074_, int p_111075_, int p_111076_, IModelData modelData);

}