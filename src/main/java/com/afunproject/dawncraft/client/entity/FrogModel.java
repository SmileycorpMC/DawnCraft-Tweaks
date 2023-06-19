package com.afunproject.dawncraft.client.entity;

import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;

public class FrogModel extends HierarchicalModel<LivingEntity> {
	private static final float WALK_ANIMATION_SPEED_FACTOR = 8000.0F;
	public static final float f_260651_ = 0.5F;
	public static final float MAX_WALK_ANIMATION_SPEED = 1.5F;
	private final ModelPart root;
	private final ModelPart body;
	private final ModelPart head;
	private final ModelPart eyes;
	private final ModelPart tongue;
	private final ModelPart leftArm;
	private final ModelPart rightArm;
	private final ModelPart leftLeg;
	private final ModelPart rightLeg;
	private final ModelPart croakingBody;

	public FrogModel(ModelPart p_233362_) {
		root = p_233362_.getChild("root");
		body = root.getChild("body");
		head = body.getChild("head");
		eyes = head.getChild("eyes");
		tongue = body.getChild("tongue");
		leftArm = body.getChild("left_arm");
		rightArm = body.getChild("right_arm");
		leftLeg = root.getChild("left_leg");
		rightLeg = root.getChild("right_leg");
		croakingBody = body.getChild("croaking_body");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();
		PartDefinition partdefinition1 = partdefinition.addOrReplaceChild("root", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));
		PartDefinition partdefinition2 = partdefinition1.addOrReplaceChild("body", CubeListBuilder.create().texOffs(3, 1).addBox(-3.5F, -2.0F, -8.0F, 7.0F, 3.0F, 9.0F).texOffs(23, 22).addBox(-3.5F, -1.0F, -8.0F, 7.0F, 0.0F, 9.0F), PartPose.offset(0.0F, -2.0F, 4.0F));
		PartDefinition partdefinition3 = partdefinition2.addOrReplaceChild("head", CubeListBuilder.create().texOffs(23, 13).addBox(-3.5F, -1.0F, -7.0F, 7.0F, 0.0F, 9.0F).texOffs(0, 13).addBox(-3.5F, -2.0F, -7.0F, 7.0F, 3.0F, 9.0F), PartPose.offset(0.0F, -2.0F, -1.0F));
		PartDefinition partdefinition4 = partdefinition3.addOrReplaceChild("eyes", CubeListBuilder.create(), PartPose.offset(-0.5F, 0.0F, 2.0F));
		partdefinition4.addOrReplaceChild("right_eye", CubeListBuilder.create().texOffs(0, 0).addBox(-1.5F, -1.0F, -1.5F, 3.0F, 2.0F, 3.0F), PartPose.offset(-1.5F, -3.0F, -6.5F));
		partdefinition4.addOrReplaceChild("left_eye", CubeListBuilder.create().texOffs(0, 5).addBox(-1.5F, -1.0F, -1.5F, 3.0F, 2.0F, 3.0F), PartPose.offset(2.5F, -3.0F, -6.5F));
		partdefinition2.addOrReplaceChild("croaking_body", CubeListBuilder.create().texOffs(26, 5).addBox(-3.5F, -0.1F, -2.9F, 7.0F, 2.0F, 3.0F, new CubeDeformation(-0.1F)), PartPose.offset(0.0F, -1.0F, -5.0F));
		PartDefinition partdefinition5 = partdefinition2.addOrReplaceChild("tongue", CubeListBuilder.create().texOffs(17, 13).addBox(-2.0F, 0.0F, -7.1F, 4.0F, 0.0F, 7.0F), PartPose.offset(0.0F, -1.01F, 1.0F));
		PartDefinition partdefinition6 = partdefinition2.addOrReplaceChild("left_arm", CubeListBuilder.create().texOffs(0, 32).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 3.0F, 3.0F), PartPose.offset(4.0F, -1.0F, -6.5F));
		partdefinition6.addOrReplaceChild("left_hand", CubeListBuilder.create().texOffs(18, 40).addBox(-4.0F, 0.01F, -4.0F, 8.0F, 0.0F, 8.0F), PartPose.offset(0.0F, 3.0F, -1.0F));
		PartDefinition partdefinition7 = partdefinition2.addOrReplaceChild("right_arm", CubeListBuilder.create().texOffs(0, 38).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 3.0F, 3.0F), PartPose.offset(-4.0F, -1.0F, -6.5F));
		partdefinition7.addOrReplaceChild("right_hand", CubeListBuilder.create().texOffs(2, 40).addBox(-4.0F, 0.01F, -5.0F, 8.0F, 0.0F, 8.0F), PartPose.offset(0.0F, 3.0F, 0.0F));
		PartDefinition partdefinition8 = partdefinition1.addOrReplaceChild("left_leg", CubeListBuilder.create().texOffs(14, 25).addBox(-1.0F, 0.0F, -2.0F, 3.0F, 3.0F, 4.0F), PartPose.offset(3.5F, -3.0F, 4.0F));
		partdefinition8.addOrReplaceChild("left_foot", CubeListBuilder.create().texOffs(2, 32).addBox(-4.0F, 0.01F, -4.0F, 8.0F, 0.0F, 8.0F), PartPose.offset(2.0F, 3.0F, 0.0F));
		PartDefinition partdefinition9 = partdefinition1.addOrReplaceChild("right_leg", CubeListBuilder.create().texOffs(0, 25).addBox(-2.0F, 0.0F, -2.0F, 3.0F, 3.0F, 4.0F), PartPose.offset(-3.5F, -3.0F, 4.0F));
		partdefinition9.addOrReplaceChild("right_foot", CubeListBuilder.create().texOffs(18, 32).addBox(-4.0F, 0.01F, -4.0F, 8.0F, 0.0F, 8.0F), PartPose.offset(-2.0F, 3.0F, 0.0F));
		return LayerDefinition.create(meshdefinition, 48, 48);
	}

	@Override
	public void setupAnim(LivingEntity p_102618_, float p_102867_, float p_102868_, float p_102869_, float p_102870_, float p_102871_) {
		rightLeg.xRot = Mth.cos(p_102867_ * 0.6662F) * 1.2F * p_102868_;
		leftLeg.xRot = Mth.cos(p_102867_ * 0.6662F + (float)Math.PI) * 1.2F * p_102868_;

		leftArm.xRot = Mth.cos(p_102867_ * 0.6662F) * 1.2F * p_102868_;
		rightArm.xRot = Mth.cos(p_102867_ * 0.6662F + (float)Math.PI) * 1.2F * p_102868_;
		/*this.root().getAllParts().forEach(ModelPart::resetPose);
		float f = (float)p_233372_.getDeltaMovement().horizontalDistanceSqr();
		float f1 = Mth.clamp(f * 8000.0F, 0.5F, 1.5F);
		this.animate(p_233372_.jumpAnimationState, FrogAnimation.FROG_JUMP, p_233375_);
		this.animate(p_233372_.croakAnimationState, FrogAnimation.FROG_CROAK, p_233375_);
		this.animate(p_233372_.tongueAnimationState, FrogAnimation.FROG_TONGUE, p_233375_);
		this.animate(p_233372_.walkAnimationState, FrogAnimation.FROG_WALK, p_233375_, f1);
		this.animate(p_233372_.swimAnimationState, FrogAnimation.FROG_SWIM, p_233375_);
		this.animate(p_233372_.swimIdleAnimationState, FrogAnimation.FROG_IDLE_WATER, p_233375_);
		this.croakingBody.visible = p_233372_.croakAnimationState.isStarted();*/
	}

	@Override
	public ModelPart root() {
		return root;
	}
}