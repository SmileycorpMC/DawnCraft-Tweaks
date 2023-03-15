package com.afunproject.dawncraft.integration.sophisticatedbackpacks;

import net.minecraft.world.item.Item;
import net.p3pp3rf1y.sophisticatedbackpacks.backpack.BackpackItem;

public class SophisticatedBackpacksCompat {

	public static boolean isBackpack(Item item) {
		return item instanceof BackpackItem;
	}

}
