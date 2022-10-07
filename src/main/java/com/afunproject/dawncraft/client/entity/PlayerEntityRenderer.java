package com.afunproject.dawncraft.client.entity;

import com.afunproject.dawncraft.ModDefinitions;
import com.afunproject.dawncraft.entities.PlayerEntity;
import com.mojang.authlib.minecraft.MinecraftProfileTexture.Type;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.client.renderer.entity.HumanoidMobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Mob;
import net.smileycorp.atlas.api.client.RenderingUtils;

public class PlayerEntityRenderer<T extends Mob & PlayerEntity> extends HumanoidMobRenderer<T, HumanoidModel<T>> {

	public static ModelLayerLocation MAIN_LAYER = new ModelLayerLocation(new ResourceLocation(ModDefinitions.MODID, "player_entity"), "main");

	public PlayerEntityRenderer(EntityRendererProvider.Context ctx) {
		this(ctx, new HumanoidModel<T>(ctx.bakeLayer(MAIN_LAYER)));
	}

	public PlayerEntityRenderer(Context ctx, HumanoidModel<T> model) {
		super(ctx, model, 0.5f);
	}

	@Override
	public ResourceLocation getTextureLocation(T entity) {
		return RenderingUtils.getPlayerTexture(entity.getPlayerUUID(), Type.SKIN);
	}

	public static LayerDefinition createMainLayer() {
		return LayerDefinition.create(HumanoidModel.createMesh(CubeDeformation.NONE, 0), 64, 64);
	}

}
