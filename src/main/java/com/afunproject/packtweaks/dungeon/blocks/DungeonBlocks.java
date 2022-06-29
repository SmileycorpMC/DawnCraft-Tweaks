package com.afunproject.packtweaks.dungeon.blocks;

import com.afunproject.packtweaks.CreativeTabs;
import com.afunproject.packtweaks.PackTweaks;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour.Properties;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.smileycorp.atlas.api.block.ShapedBlock;

public class DungeonBlocks {

	public static DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, PackTweaks.MODID);
	public static DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, PackTweaks.MODID);

	private static Properties DUNGEON_PROPS = Properties.of(Material.STONE, MaterialColor.TERRACOTTA_ORANGE).strength(-1, 18000000);

	public static final ShapedBlock FOREST_BRICK = new ShapedBlock("forest_brick", CreativeTabs.DUNGEONS, DUNGEON_PROPS, ITEMS, BLOCKS, true);
	public static final ShapedBlock FIRE_BRICK = new ShapedBlock("fire_brick", CreativeTabs.DUNGEONS, DUNGEON_PROPS, ITEMS, BLOCKS, true);
	public static final ShapedBlock CORAL = new ShapedBlock("coral", CreativeTabs.DUNGEONS, DUNGEON_PROPS, ITEMS, BLOCKS, true);
	public static final ShapedBlock MAGMA_SLATE = new ShapedBlock("magma_slate", CreativeTabs.DUNGEONS, DUNGEON_PROPS, ITEMS, BLOCKS, true);
	public static final ShapedBlock MOSS_BRICK = new ShapedBlock("moss_brick", CreativeTabs.DUNGEONS, DUNGEON_PROPS, ITEMS, BLOCKS, true);
	public static final ShapedBlock MUD_BRICK = new ShapedBlock("mud_brick", CreativeTabs.DUNGEONS, DUNGEON_PROPS, ITEMS, BLOCKS, true);
	public static final ShapedBlock ROCK = new ShapedBlock("rock", CreativeTabs.DUNGEONS, DUNGEON_PROPS, ITEMS, BLOCKS, true);
	public static final ShapedBlock SAND_BRICK = new ShapedBlock("sand_brick", CreativeTabs.DUNGEONS, DUNGEON_PROPS, ITEMS, BLOCKS, true);
	public static final ShapedBlock SAND_SLATE = new ShapedBlock("sand_slate", CreativeTabs.DUNGEONS, DUNGEON_PROPS, ITEMS, BLOCKS, true);
	public static final ShapedBlock SHADOW_BRICK = new ShapedBlock("shadow_brick", CreativeTabs.DUNGEONS, DUNGEON_PROPS, ITEMS, BLOCKS, true);
	public static final ShapedBlock WATER_BRICK = new ShapedBlock("water_brick", CreativeTabs.DUNGEONS, DUNGEON_PROPS, ITEMS, BLOCKS, true);

	public static final RegistryObject<Block> FIRE_DOOR = registerDoor("fire_door");
	public static final RegistryObject<Block> RUST_DOOR = registerDoor("rust_door");
	public static final RegistryObject<Block> SAND_DOOR = registerDoor("sand_door");
	public static final RegistryObject<Block> STONE_DOOR = registerDoor("stone_door");
	public static final RegistryObject<Block> WOOD_DOOR = registerDoor("wood_door");

	private static RegistryObject<Block> registerDoor(String name) {
		RegistryObject<Block> block = BLOCKS.register(name, () -> new DungeonDoorBlock(DUNGEON_PROPS));
		ITEMS.register(block.getId().getPath(), () -> new BlockItem(block.get(), new Item.Properties().tab(CreativeTabs.DUNGEONS)));
		return block;
	}

}
