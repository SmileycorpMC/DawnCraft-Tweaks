package com.afunproject.packtweaks;

import com.afunproject.packtweaks.dungeon.blocks.DungeonBlocks;

import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

public class CreativeTabs {

	public static CreativeModeTab DUNGEONS = new CreativeModeTab(PackTweaks.MODID + ".Dungeons"){
		@Override
		public ItemStack makeIcon(){
			return new ItemStack(DungeonBlocks.FOREST_BRICK.getBase());
		}
	};

}
