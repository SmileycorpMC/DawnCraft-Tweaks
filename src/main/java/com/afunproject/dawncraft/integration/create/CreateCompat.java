package com.afunproject.dawncraft.integration.create;

import com.google.common.collect.Lists;
import com.simibubi.create.AllItems;
import com.simibubi.create.content.equipment.toolbox.ToolboxBlock;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public class CreateCompat {

	public static boolean isToolbox(Item item) {
		if (item instanceof BlockItem) return ((BlockItem) item).getBlock() instanceof ToolboxBlock;
		return false;
	}
    
    public static List<ItemStack> getXPNuggets(int loss) {
		List<ItemStack> drops = Lists.newArrayList();
		int count = loss / 3;
		int numStacks = count / 64;
		int remainder = count % 64;
		for (int i = 0; i <= numStacks; i++) drops.add(new ItemStack(AllItems.EXP_NUGGET.get(), i == numStacks ? remainder : 64));
		return drops;
	}
	
}
