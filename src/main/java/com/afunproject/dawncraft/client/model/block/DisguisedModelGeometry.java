package com.afunproject.dawncraft.client.model.block;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.function.Function;

import com.afunproject.dawncraft.client.model.block.DisguisedModelLoader.DungeonModelContext;
import com.google.common.collect.Lists;
import com.mojang.datafixers.util.Pair;

import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.Material;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.client.resources.model.ModelState;
import net.minecraft.client.resources.model.UnbakedModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.model.IModelConfiguration;
import net.minecraftforge.client.model.geometry.IModelGeometry;

public class DisguisedModelGeometry implements IModelGeometry<DisguisedModelGeometry> {

	private final DungeonModelContext ctx;

	public DisguisedModelGeometry(DungeonModelContext ctx) {
		this.ctx = ctx;
	}

	@Override
	public BakedModel bake(IModelConfiguration owner, ModelBakery bakery, Function<Material, TextureAtlasSprite> spriteGetter, ModelState modelTransform, ItemOverrides overrides, ResourceLocation modelLocation) {
		return new DisguisedBakedModel(ctx);
	}

	@Override
	@SuppressWarnings("deprecation")
	public Collection<Material> getTextures(IModelConfiguration owner, Function<ResourceLocation, UnbakedModel> modelGetter, Set<Pair<String, String>> missingTextureErrors) {
		List<Material> materials = Lists.newArrayList();
		for (ResourceLocation loc : ctx.getTextures().values()) materials.add(new Material(TextureAtlas.LOCATION_BLOCKS, loc));
		for (ResourceLocation loc : ctx.getOverlayTextures().values()) materials.add(new Material(TextureAtlas.LOCATION_BLOCKS, loc));
		return materials;
	}

}
