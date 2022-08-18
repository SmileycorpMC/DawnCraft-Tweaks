package com.afunproject.afptweaks.client;

import com.afunproject.afptweaks.ModDefinitions;
import com.afunproject.afptweaks.entities.Fallen;
import com.mojang.authlib.minecraft.MinecraftProfileTexture.Type;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.client.renderer.entity.HumanoidMobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.smileycorp.atlas.api.client.RenderingUtils;

public class FallenRenderer extends HumanoidMobRenderer<Fallen, HumanoidModel<Fallen>> {

	public static ModelLayerLocation MAIN_LAYER = new ModelLayerLocation(new ResourceLocation(ModDefinitions.MODID, "fallen"), "main");

	protected FallenRenderer(Context ctx) {
		super(ctx, new FallenModel(ctx.bakeLayer(MAIN_LAYER)), 0.5f);
	}

	@Override
	protected RenderType getRenderType(Fallen entity, boolean p_230496_2_, boolean p_230496_3_, boolean p_230496_4_) {
		return RenderType.entityTranslucent(getTextureLocation(entity));
	}

	@Override
	public ResourceLocation getTextureLocation(Fallen entity) {
		return RenderingUtils.getPlayerTexture(entity.getPlayerUUID(), Type.SKIN);
	}

	public static LayerDefinition createMainLayer() {
		return LayerDefinition.create(HumanoidModel.createMesh(CubeDeformation.NONE, 0), 64, 64);
	}

}
