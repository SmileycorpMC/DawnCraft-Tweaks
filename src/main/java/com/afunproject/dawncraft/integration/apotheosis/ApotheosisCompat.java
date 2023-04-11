package com.afunproject.dawncraft.integration.apotheosis;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

public class ApotheosisCompat {

	public static void fixNBT(LivingEntity entity) {
		for (ItemStack stack : entity.getAllSlots()) {
			if (stack.hasTag() && stack.getTag().contains("apoth.equipment")) {
				stack.getTag().remove("apoth.equipment");
				if (stack.getTag().isEmpty()) stack.setTag(null);
			}
		}
	}

}
