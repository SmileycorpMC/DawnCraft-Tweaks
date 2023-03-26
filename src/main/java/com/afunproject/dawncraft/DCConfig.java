package com.afunproject.dawncraft;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.BooleanValue;

public class DCConfig {

	public static final ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();
	public static final ForgeConfigSpec config;

	public static BooleanValue harderKeepInventory;

	static {
		harderKeepInventory = builder.comment("Should items lose durability and random valuable items have a chance to be left in the corpse on death? (Default is true)")
				.define("harderKeepInventory", true);
		config = builder.build();
	}
}
