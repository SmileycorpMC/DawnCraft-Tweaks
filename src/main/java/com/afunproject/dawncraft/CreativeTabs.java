package com.afunproject.dawncraft;

import com.afunproject.dawncraft.dungeon.KeyColour;
import com.afunproject.dawncraft.dungeon.block.DungeonBlocks;
import com.afunproject.dawncraft.dungeon.item.*;
import net.mcreator.simplemobs.init.SimpleMobsModItems;
import net.minecraft.core.NonNullList;
import net.minecraft.core.Registry;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.RegistryObject;

public class CreativeTabs {

	public static CreativeModeTab DUNGEON_BLOCKS = new CreativeModeTab(Constants.MODID + ".DungeonBlocks"){
		@Override
		public ItemStack makeIcon(){
			return new ItemStack(DungeonBlocks.FOREST_BRICK.getBase());
		}
	};

	public static CreativeModeTab DUNGEON_FUNCTIONAL_BLOCKS = new CreativeModeTab(Constants.MODID + ".DungeonFunctionalBlocks"){
		@Override
		public ItemStack makeIcon(){
			return new ItemStack(DungeonBlocks.WOOD_DOOR.get());
		}
	};

	public static CreativeModeTab DUNGEON_ITEMS = new CreativeModeTab(Constants.MODID + ".DungeonItems"){
		@Override
		public ItemStack makeIcon(){
			return new ItemStack(DungeonItems.SKELETON_KEY.get());
		}

		@Override
		@SuppressWarnings("deprecation")
		public void fillItemList(NonNullList<ItemStack> items) {
			DungeonItems.DUNGEON_CONFIGURATOR.get().fillItemCategory(this, items);
			for(KeyColour colour : KeyColour.values()) items.add(new ItemStack(DungeonItems.getLock(colour)));
			items.add(new ItemStack(DungeonItems.REBIRTH_STAFF.get()));
			items.add(new ItemStack(DungeonItems.SKELETON_KEY.get()));
			for(KeyColour colour : KeyColour.values()) items.add(new ItemStack(DungeonItems.getKey(colour)));
			items.add(RebirthStaffItem.createPowered());
			for(Item item : Registry.ITEM) {
				if(!(item instanceof KeyItem || item instanceof SkeletonKeyItem || item instanceof DungeonConfiguratorItem
						|| item instanceof LockItem || item instanceof RebirthStaffItem)) item.fillItemCategory(this, items);
			}
			for (RegistryObject<Item> reg : SimpleMobsModItems.REGISTRY.getEntries()) {
				Item item = reg.get();
				if (!(item instanceof BlockItem) && item.getRegistryName().getNamespace().equals("simple_mobs") &!
						item.getRegistryName().getPath().contains("spawn_egg")) items.add(new ItemStack(item));
			}
		}
	};

}
