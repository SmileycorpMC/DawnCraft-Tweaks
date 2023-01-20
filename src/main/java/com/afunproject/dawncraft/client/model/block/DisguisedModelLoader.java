package com.afunproject.dawncraft.client.model.block;

import java.util.Map;

import com.afunproject.dawncraft.DawnCraft;
import com.google.common.collect.Maps;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;

import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraftforge.client.model.IModelLoader;

public class DisguisedModelLoader implements IModelLoader<DisguisedModelGeometry> {

	@Override
	public void onResourceManagerReload(ResourceManager p_10758_) {}

	@Override
	public DisguisedModelGeometry read(JsonDeserializationContext deserializationContext, JsonObject json) {
		DungeonModelContext ctx = new DungeonModelContext();
		if (json.has("textures")) {
			JsonObject textures = json.get("textures").getAsJsonObject();
			if (textures.has("overlay")) {
				try {
					ResourceLocation loc = new ResourceLocation(textures.get("overlay").getAsString());
					for (Direction dir : Direction.values()) ctx.getOverlayTextures().put(dir, loc);
				} catch (Exception e) {
					DawnCraft.logError("Failed to load disguisable model, invalid property \"overlay\"", e);
				}
			}
			for (Direction dir : Direction.values()) {

				if (textures.has(dir.toString().toLowerCase())) {
					try {
						ctx.getTextures().put(dir, new ResourceLocation(textures.get(dir.toString().toLowerCase()).getAsString()));
					} catch (Exception e) {
						DawnCraft.logError("Failed to load disguisable model, invalid property \""+ dir.toString().toLowerCase() +"\"", e);
					}
				}
				if (textures.has("overlay_" + dir.toString().toLowerCase())) {
					try {
						ctx.getOverlayTextures().put(dir, new ResourceLocation(textures.get("overlay_" + dir.toString().toLowerCase()).getAsString()));
					} catch (Exception e) {
						DawnCraft.logError("Failed to load disguisable model, invalid property \"overlay_"+ dir.toString().toLowerCase() +"\"", e);
					}
				}
			}
		}
		return new DisguisedModelGeometry(ctx);
	}

	public static class DungeonModelContext {

		private Map<Direction, ResourceLocation> textures = Maps.newHashMap();
		private Map<Direction, ResourceLocation> overlay_textures = Maps.newHashMap();

		public Map<Direction, ResourceLocation> getTextures() {
			return textures;
		}

		public Map<Direction, ResourceLocation> getOverlayTextures() {
			return overlay_textures;
		}

	}

}
