package com.afunproject.dawncraft;

import net.minecraft.resources.ResourceLocation;

public class Constants {
	
	public static final String MODID = "dawncraft";
	public static String VERSION = "N/A";
	public static final float DAMAGE_MULT = 0.5f;

	public static ResourceLocation loc(String path) {
		return new ResourceLocation(MODID, path);
	}
	public static String locStr(String path) {
		return loc(path).toString();
	}

}
