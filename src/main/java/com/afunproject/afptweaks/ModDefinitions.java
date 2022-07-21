package com.afunproject.afptweaks;

import net.minecraft.resources.ResourceLocation;

public class ModDefinitions {

	public static final String MODID = "afptweaks";
	public static String VERSION = "N/A";

	public static ResourceLocation getResource(String path) {
		return new ResourceLocation(MODID, path);
	}
	public static String getResourceName(String path) {
		return getResource(path).toString();
	}

}
