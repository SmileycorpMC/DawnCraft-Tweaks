package com.afunproject.dawncraft;

import net.minecraft.resources.ResourceLocation;

public class ModDefinitions {

	public static final String LEGACY_MODID = "afptweaks";
	public static final String MODID = "dawncraft";
	public static String VERSION = "N/A";

	public static ResourceLocation getResource(String path) {
		return new ResourceLocation(MODID, path);
	}
	public static String getResourceName(String path) {
		return getResource(path).toString();
	}

}
