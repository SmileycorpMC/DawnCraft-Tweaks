package com.afunproject.dawncraft;

import net.minecraft.resources.ResourceLocation;

public class Constants {

	public static final String LEGACY_MODID = "afptweaks";
	public static final String MODID = "dawncraft";
	public static String VERSION = "N/A";

	public static ResourceLocation loc(String path) {
		return new ResourceLocation(MODID, path);
	}
	public static String locStr(String path) {
		return loc(path).toString();
	}

}
