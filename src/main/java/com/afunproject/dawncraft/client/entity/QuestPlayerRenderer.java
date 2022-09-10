package com.afunproject.dawncraft.client.entity;

import com.afunproject.dawncraft.ModDefinitions;
import com.afunproject.dawncraft.entities.QuestPlayer;
import com.mojang.authlib.minecraft.MinecraftProfileTexture.Type;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.client.renderer.entity.HumanoidMobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.smileycorp.atlas.api.client.RenderingUtils;

public class QuestPlayerRenderer<T extends QuestPlayer> extends HumanoidMobRenderer<T, HumanoidModel<T>> {

	public static ModelLayerLocation MAIN_LAYER = new ModelLayerLocation(new ResourceLocation(ModDefinitions.MODID, "quest_player"), "main");

	public QuestPlayerRenderer(EntityRendererProvider.Context ctx) {
		this(ctx, new HumanoidModel<T>(ctx.bakeLayer(MAIN_LAYER)));
	}

	public QuestPlayerRenderer(Context ctx, HumanoidModel<T> model) {
		super(ctx, model, 0.5f);
	}

	@Override
	public ResourceLocation getTextureLocation(QuestPlayer entity) {
		return RenderingUtils.getPlayerTexture(entity.getPlayerUUID(), Type.SKIN);
	}

	public static LayerDefinition createMainLayer() {
		return LayerDefinition.create(HumanoidModel.createMesh(CubeDeformation.NONE, 0), 64, 64);
	}

}
