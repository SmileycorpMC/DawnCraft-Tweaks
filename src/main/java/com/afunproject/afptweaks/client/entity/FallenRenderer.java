package com.afunproject.afptweaks.client.entity;

import com.afunproject.afptweaks.AFPRenderTypes;
import com.afunproject.afptweaks.ModDefinitions;
import com.afunproject.afptweaks.entities.Fallen;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

public class FallenRenderer extends QuestPlayerRenderer<Fallen> {

	public static ModelLayerLocation MAIN_LAYER = new ModelLayerLocation(new ResourceLocation(ModDefinitions.MODID, "fallen"), "main");

	public FallenRenderer(EntityRendererProvider.Context ctx) {
		super(ctx, new FallenModel(ctx.bakeLayer(MAIN_LAYER)));
	}

	@Override
	protected RenderType getRenderType(Fallen entity, boolean p_230496_2_, boolean p_230496_3_, boolean p_230496_4_) {
		return AFPRenderTypes.ghost(getTextureLocation(entity));
	}

	public static LayerDefinition createMainLayer() {
		return LayerDefinition.create(HumanoidModel.createMesh(CubeDeformation.NONE, 0), 64, 64);
	}

}
