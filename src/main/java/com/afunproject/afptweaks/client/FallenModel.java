package com.afunproject.afptweaks.client;

import com.afunproject.afptweaks.entities.Fallen;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;

public class FallenModel extends HumanoidModel<Fallen> {

	public FallenModel(ModelPart modelPart) {
		super(modelPart);
	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer consumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		super.renderToBuffer(poseStack, consumer, packedLight, packedOverlay, 170f/255f, 170f/255f, 170f/255f, 0.75f);
	}

}
