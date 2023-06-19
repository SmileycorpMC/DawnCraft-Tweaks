package com.afunproject.dawncraft.client.entity;

import com.afunproject.dawncraft.integration.quests.custom.entity.Fallen;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;

public class FallenModel extends HumanoidModel<Fallen> {

	private float alpha = 0.9f;

	public FallenModel(ModelPart modelPart) {
		super(modelPart);
	}

	@Override
	public void setupAnim(Fallen fallen, float p_102867_, float p_102868_, float p_102869_, float p_102870_, float p_102871_) {
		int fade = fallen.getFadeTimer();
		if (fade > -1) alpha  = fallen.getFadeTimer()/(float)Fallen.FADE_LENGTH * 0.9f;
	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer consumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		super.renderToBuffer(poseStack, consumer, packedLight, packedOverlay, 1, 1, 1, this.alpha);
	}

}
