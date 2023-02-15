package com.afunproject.dawncraft.client;

public class EntityRenderProperties {

	private static boolean shouldRenderNameplate = true;

	public static void setRenderNameplate(boolean bool) {
		shouldRenderNameplate = bool;
	}

	public static boolean shouldRenderNameplate() {
		return shouldRenderNameplate;
	}

}
