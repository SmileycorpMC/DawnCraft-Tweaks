package com.afunproject.dawncraft.client.model.block;
import java.awt.Color;
import java.util.List;
import java.util.Map.Entry;
import java.util.Random;
import java.util.function.Function;

import com.afunproject.dawncraft.DawnCraft;
import com.afunproject.dawncraft.ModDefinitions;
import com.afunproject.dawncraft.client.model.block.DisguisedModelLoader.DungeonModelContext;
import com.afunproject.dawncraft.dungeon.block.entity.interfaces.Disguisable;
import com.google.common.collect.Lists;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.model.data.IDynamicBakedModel;
import net.minecraftforge.client.model.data.IModelData;
import net.smileycorp.atlas.api.client.RenderingUtils;

@SuppressWarnings("deprecation")
public class DisguisedBakedModel implements IDynamicBakedModel {

	private final DungeonModelContext ctx;
	private final DisguisedBakedModelOverlay overlay;

	public DisguisedBakedModel(DungeonModelContext ctx) {
		this.ctx = ctx;
		overlay = new DisguisedBakedModelOverlay(this);
	}

	@Override
	public TextureAtlasSprite getParticleIcon(IModelData data) {
		BlockState bg = data.getData(Disguisable.TEXTURE);
		Minecraft mc = Minecraft.getInstance();
		try {
			return mc.getBlockRenderer().getBlockModelShaper()
					.getBlockModel(bg).getParticleIcon(data);
		} catch (Exception e) {}
		return mc.getTextureAtlas(TextureAtlas.LOCATION_BLOCKS)
				.apply(ModDefinitions.getResource("block/forest_brick"));
	}

	@Override
	public List<BakedQuad> getQuads(BlockState state, Direction side, Random rand, IModelData data) {
		List<BakedQuad> quads = Lists.newArrayList();
		Minecraft mc = Minecraft.getInstance();
		Function<ResourceLocation, TextureAtlasSprite> blockSprites = mc.getTextureAtlas(TextureAtlas.LOCATION_BLOCKS);
		for (Direction dir : Direction.values()) {
			if (ctx.getTextures().containsKey(dir)) {
				try {
					TextureAtlasSprite sprite = blockSprites.apply(ctx.getTextures().get(dir));
					quads.addAll(RenderingUtils.getQuadsForPlane(dir, Color.WHITE, sprite));
				} catch (Exception e) {
					DawnCraft.logError("Failed to load custom quads for side "+dir, e);
				}
			} else {
				try {
					BlockState bg = data.getData(Disguisable.TEXTURE);
					if (bg == null) {
						TextureAtlasSprite sprite = blockSprites.apply(ModDefinitions.getResource("block/forest_brick"));
						quads.addAll(RenderingUtils.getQuadsForPlane(dir, Color.WHITE, sprite));
					} else {
						TextureAtlasSprite sprite = mc.getBlockRenderer().getBlockModel(bg).getParticleIcon();
						quads.addAll(RenderingUtils.getQuadsForPlane(dir, Color.WHITE, sprite));
					}
				} catch (Exception e) {
					DawnCraft.logError("Failed to load default quads for side "+dir, e);
				}
			}
		}
		return quads;
	}

	@Override
	public boolean useAmbientOcclusion() {
		return true;
	}

	@Override
	public boolean isGui3d() {
		return true;
	}

	@Override
	public boolean usesBlockLight() {
		return false;
	}

	@Override
	public boolean isCustomRenderer() {
		return false;
	}

	@Override
	public TextureAtlasSprite getParticleIcon() {
		return Minecraft.getInstance().getTextureAtlas(TextureAtlas.LOCATION_BLOCKS)
				.apply(ModDefinitions.getResource("block/forest_brick"));
	}

	@Override
	public ItemOverrides getOverrides() {
		return ItemOverrides.EMPTY;
	}

	@Override
	public ItemTransforms getTransforms() {
		return ItemTransforms.NO_TRANSFORMS;
	}

	public DisguisedBakedModelOverlay getOverlayModel() {
		return overlay;
	}

	public static class DisguisedBakedModelOverlay implements IDynamicBakedModel {

		protected final DisguisedBakedModel base;

		public DisguisedBakedModelOverlay(DisguisedBakedModel base) {
			this.base = base;
		}

		@Override
		public List<BakedQuad> getQuads(BlockState state, Direction side, Random rand, IModelData data) {
			List<BakedQuad> quads = Lists.newArrayList();
			Minecraft mc = Minecraft.getInstance();
			Function<ResourceLocation, TextureAtlasSprite> blockSprites = mc.getTextureAtlas(TextureAtlas.LOCATION_BLOCKS);
			for (Entry<Direction, ResourceLocation> entry : base.ctx.getOverlayTextures().entrySet()) {
				try {
					TextureAtlasSprite sprite = blockSprites.apply(entry.getValue());
					quads.addAll(RenderingUtils.getQuadsForPlane(entry.getKey(), Color.WHITE, sprite, 10));
				} catch (Exception e) {
					DawnCraft.logError("Failed to load overlay quads for side "+entry.getKey(), e);
				}
			}
			return quads;
		}

		@Override
		public boolean useAmbientOcclusion() {
			return base.useAmbientOcclusion();
		}

		@Override
		public boolean isGui3d() {
			return base.isGui3d();
		}

		@Override
		public boolean usesBlockLight() {
			return base.usesBlockLight();
		}

		@Override
		public boolean isCustomRenderer() {
			return base.isCustomRenderer();
		}

		@Override
		public TextureAtlasSprite getParticleIcon() {
			return base.getParticleIcon();
		}

		@Override
		public ItemOverrides getOverrides() {
			return base.getOverrides();
		}

		@Override
		public ItemTransforms getTransforms() {
			return base.getTransforms();
		}
	}

}
