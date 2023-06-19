package com.afunproject.dawncraft.client.entity;

import com.afunproject.dawncraft.client.DawnCraftRenderTypes;
import com.afunproject.dawncraft.integration.quests.custom.entity.Fallen;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

public class FallenRenderer extends PlayerEntityRenderer<Fallen> {

	public FallenRenderer(EntityRendererProvider.Context ctx) {
		super(ctx, new FallenModel(ctx.bakeLayer(DEFAULT)), new FallenModel(ctx.bakeLayer(SLIM)));
	}

	@Override
	protected RenderType getRenderType(Fallen entity, boolean p_230496_2_, boolean p_230496_3_, boolean p_230496_4_) {
		return DawnCraftRenderTypes.ghost(getTextureLocation(entity));
	}

}
