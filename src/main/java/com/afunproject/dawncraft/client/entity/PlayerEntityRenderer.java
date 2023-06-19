package com.afunproject.dawncraft.client.entity;

import com.afunproject.dawncraft.Constants;
import com.afunproject.dawncraft.entities.PlayerEntity;
import com.mojang.authlib.minecraft.MinecraftProfileTexture.Type;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.client.renderer.entity.HumanoidMobRenderer;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Mob;
import net.smileycorp.atlas.api.client.PlayerTextureRenderer;

import java.util.Optional;
import java.util.UUID;

public class PlayerEntityRenderer<T extends Mob & PlayerEntity> extends HumanoidMobRenderer<T, HumanoidModel<T>> {

	public static ModelLayerLocation DEFAULT = new ModelLayerLocation(new ResourceLocation(Constants.MODID, "player_entity"), "default");
	public static ModelLayerLocation SLIM = new ModelLayerLocation(new ResourceLocation(Constants.MODID, "player_entity"), "slim");

	protected final HumanoidModel<T> defaultModel;
	protected final HumanoidModel<T> slimModel;

	public PlayerEntityRenderer(EntityRendererProvider.Context ctx) {
		this(ctx, new HumanoidModel<T>(ctx.bakeLayer(DEFAULT)), new HumanoidModel<T>(ctx.bakeLayer(SLIM)));
	}

	public PlayerEntityRenderer(Context ctx, HumanoidModel<T> defaultModel, HumanoidModel<T> slimModel) {
		super(ctx, defaultModel, 0.5f);
		this.defaultModel = defaultModel;
		this.slimModel = slimModel;
		this.addLayer(new HumanoidArmorLayer<>(this, new HumanoidModel<>(ctx.bakeLayer(ModelLayers.PLAYER_INNER_ARMOR)),
				new HumanoidModel<>(ctx.bakeLayer(ModelLayers.PLAYER_OUTER_ARMOR))));
	}

	@Override
	public ResourceLocation getTextureLocation(T entity) {
		Optional<UUID> optional = entity.getPlayerUUID();
		return PlayerTextureRenderer.getTexture(optional, Type.SKIN);
	}

	@Override
	public void render(T entity, float yaw, float partialTicks, PoseStack poseStack, MultiBufferSource bufferSource, int packedLightIn) {
		Optional<UUID> optional = entity.getPlayerUUID();
		String skinType = PlayerTextureRenderer.getSkinType(optional);
		boolean isSlim = skinType == null ? false : skinType.equals("slim");
		if (isSlim && model != slimModel) model = slimModel;
		else if (!isSlim && model != defaultModel) model = defaultModel;
		super.render(entity, yaw, partialTicks, poseStack, bufferSource, packedLightIn);
	}


	public static LayerDefinition createLayer(boolean slim) {
		return LayerDefinition.create(createMesh(CubeDeformation.NONE, slim), 64, 64);
	}

	public static MeshDefinition createMesh(CubeDeformation p_170826_, boolean p_170827_) {
		MeshDefinition meshdefinition = HumanoidModel.createMesh(p_170826_, 0.0F);
		PartDefinition partdefinition = meshdefinition.getRoot();
		partdefinition.addOrReplaceChild("ear", CubeListBuilder.create().texOffs(24, 0).addBox(-3.0F, -6.0F, -1.0F, 6.0F, 6.0F, 1.0F, p_170826_), PartPose.ZERO);
		partdefinition.addOrReplaceChild("cloak", CubeListBuilder.create().texOffs(0, 0).addBox(-5.0F, 0.0F, -1.0F, 10.0F, 16.0F, 1.0F, p_170826_, 1.0F, 0.5F), PartPose.offset(0.0F, 0.0F, 0.0F));
		if (p_170827_) {
			partdefinition.addOrReplaceChild("left_arm", CubeListBuilder.create().texOffs(32, 48).addBox(-1.0F, -2.0F, -2.0F, 3.0F, 12.0F, 4.0F, p_170826_), PartPose.offset(5.0F, 2.5F, 0.0F));
			partdefinition.addOrReplaceChild("right_arm", CubeListBuilder.create().texOffs(40, 16).addBox(-2.0F, -2.0F, -2.0F, 3.0F, 12.0F, 4.0F, p_170826_), PartPose.offset(-5.0F, 2.5F, 0.0F));
			partdefinition.addOrReplaceChild("left_sleeve", CubeListBuilder.create().texOffs(48, 48).addBox(-1.0F, -2.0F, -2.0F, 3.0F, 12.0F, 4.0F, p_170826_.extend(0.25F)), PartPose.offset(5.0F, 2.5F, 0.0F));
			partdefinition.addOrReplaceChild("right_sleeve", CubeListBuilder.create().texOffs(40, 32).addBox(-2.0F, -2.0F, -2.0F, 3.0F, 12.0F, 4.0F, p_170826_.extend(0.25F)), PartPose.offset(-5.0F, 2.5F, 0.0F));
		} else {
			partdefinition.addOrReplaceChild("left_arm", CubeListBuilder.create().texOffs(32, 48).addBox(-1.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, p_170826_), PartPose.offset(5.0F, 2.0F, 0.0F));
			partdefinition.addOrReplaceChild("left_sleeve", CubeListBuilder.create().texOffs(48, 48).addBox(-1.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, p_170826_.extend(0.25F)), PartPose.offset(5.0F, 2.0F, 0.0F));
			partdefinition.addOrReplaceChild("right_sleeve", CubeListBuilder.create().texOffs(40, 32).addBox(-3.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, p_170826_.extend(0.25F)), PartPose.offset(-5.0F, 2.0F, 0.0F));
		}

		partdefinition.addOrReplaceChild("left_leg", CubeListBuilder.create().texOffs(16, 48).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, p_170826_), PartPose.offset(1.9F, 12.0F, 0.0F));
		partdefinition.addOrReplaceChild("left_pants", CubeListBuilder.create().texOffs(0, 48).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, p_170826_.extend(0.25F)), PartPose.offset(1.9F, 12.0F, 0.0F));
		partdefinition.addOrReplaceChild("right_pants", CubeListBuilder.create().texOffs(0, 32).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, p_170826_.extend(0.25F)), PartPose.offset(-1.9F, 12.0F, 0.0F));
		partdefinition.addOrReplaceChild("jacket", CubeListBuilder.create().texOffs(16, 32).addBox(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F, p_170826_.extend(0.25F)), PartPose.ZERO);
		return meshdefinition;
	}


}
