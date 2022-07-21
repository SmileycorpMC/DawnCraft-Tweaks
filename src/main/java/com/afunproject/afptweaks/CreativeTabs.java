package com.afunproject.afptweaks;

import com.afunproject.afptweaks.dungeon.KeyColour;
import com.afunproject.afptweaks.dungeon.block.DungeonBlocks;
import com.afunproject.afptweaks.dungeon.item.DungeonItems;

import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

public class CreativeTabs {

	public static CreativeModeTab DUNGEON_BLOCKS = new CreativeModeTab(ModDefinitions.MODID + ".DungeonBlocks"){
		@Override
		public ItemStack makeIcon(){
			return new ItemStack(DungeonBlocks.FOREST_BRICK.getBase());
		}
	};

	public static CreativeModeTab DUNGEON_FUNCTIONAL_BLOCKS = new CreativeModeTab(ModDefinitions.MODID + ".DungeonFunctionalBlocks"){
		@Override
		public ItemStack makeIcon(){
			return new ItemStack(DungeonBlocks.WOOD_DOOR.get());
		}
	};

	public static CreativeModeTab DUNGEON_ITEMS = new CreativeModeTab(ModDefinitions.MODID + ".DungeonItems"){
		@Override
		public ItemStack makeIcon(){
			return new ItemStack(DungeonItems.getKey(KeyColour.YELLOW));
		}
	};

}
