package com.afunproject.afptweaks;

import java.util.function.Function;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat.Mode;

import net.minecraft.Util;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;

public abstract class AFPRenderTypes extends RenderType {

	public AFPRenderTypes(String p_173178_, VertexFormat p_173179_, Mode p_173180_, int p_173181_, boolean p_173182_,
			boolean p_173183_, Runnable p_173184_, Runnable p_173185_) {
		super(p_173178_, p_173179_, p_173180_, p_173181_, p_173182_, p_173183_, p_173184_, p_173185_);
		// TODO Auto-generated constructor stub
	}

	private static final Function<ResourceLocation, RenderType> GHOST_RENDERER = Util.memoize((textureLocation) -> {
		RenderType.CompositeState state = RenderType.CompositeState.builder()
				.setShaderState(RENDERTYPE_ENTITY_CUTOUT_NO_CULL_SHADER)
				.setTextureState(new RenderStateShard.TextureStateShard(textureLocation, false, false))
				.setTransparencyState(CRUMBLING_TRANSPARENCY)
				.setCullState(CULL)
				.setLightmapState(LIGHTMAP)
				.setOverlayState(NO_OVERLAY)
				.createCompositeState(true);
		return create(ModDefinitions.MODID + ":ghost", DefaultVertexFormat.NEW_ENTITY, VertexFormat.Mode.QUADS, 16, true, false, state);
	});

	/*protected static final RenderStateShard.TransparencyStateShard GHOST_TRANSPARENCY = new RenderStateShard.TransparencyStateShard("ghost_transparency", () -> {
		RenderSystem.enableBlend();
		RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.DST_COLOR, GlStateManager.DestFactor.SRC_COLOR, GlStateManager.SourceFactor., GlStateManager.DestFactor.ZERO);
	}, () -> {
		RenderSystem.disableBlend();
		RenderSystem.defaultBlendFunc();
	});*/

	public static RenderType ghost(ResourceLocation locationIn) {
		return GHOST_RENDERER.apply(locationIn);
	}

}
