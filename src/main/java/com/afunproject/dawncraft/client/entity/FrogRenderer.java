package com.afunproject.dawncraft.client.entity;

import com.afunproject.dawncraft.Constants;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;

public class FrogRenderer extends LivingEntityRenderer<LivingEntity, FrogModel> {

	public static FrogRenderer INSTANCE;

	public static ModelLayerLocation DEFAULT = new ModelLayerLocation(new ResourceLocation(Constants.MODID, "frog"), "default");

	public FrogRenderer(EntityRendererProvider.Context ctx) {
		super(ctx, new FrogModel(ctx.bakeLayer(DEFAULT)), 0.3f);
	}

	@Override
	public ResourceLocation getTextureLocation(LivingEntity entity) {
		return Constants.loc("textures/entity/frog.png");
	}

	public static void init() {
		Minecraft mc = Minecraft.getInstance();
		Context ctx = new Context(mc.getEntityRenderDispatcher(), mc.getItemRenderer(), mc.getResourceManager(), mc.getEntityModels(), mc.font);
		INSTANCE = new FrogRenderer(ctx);
	}

}
