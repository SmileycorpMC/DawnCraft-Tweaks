package com.afunproject.dawncraft.integration.create;

import com.simibubi.create.content.equipment.toolbox.ToolboxBlock;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;

public class CreateCompat {

	public static boolean isToolbox(Item item) {
		if (item instanceof BlockItem) return ((BlockItem) item).getBlock() instanceof ToolboxBlock;
		return false;
	}
	
}
