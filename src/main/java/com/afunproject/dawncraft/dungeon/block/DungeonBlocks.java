package com.afunproject.dawncraft.dungeon.block;

import com.afunproject.dawncraft.Constants;
import com.afunproject.dawncraft.CreativeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour.Properties;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public class DungeonBlocks {

	public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, Constants.MODID);
	public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Constants.MODID);

	private static Properties DUNGEON_PROPS = Properties.of(Material.STONE, MaterialColor.TERRACOTTA_ORANGE).strength(-1, 18000000);

	//decorational blocks
	public static final DungeonBlock FOREST_BRICK = new DungeonBlock("forest_brick", MaterialColor.PODZOL, ITEMS, BLOCKS);
	public static final DungeonBlock FIRE_BRICK = new DungeonBlock("fire_brick", MaterialColor.TERRACOTTA_YELLOW, ITEMS, BLOCKS);
	public static final DungeonBlock CORAL = new DungeonBlock("coral", MaterialColor.WARPED_WART_BLOCK, ITEMS, BLOCKS);
	public static final DungeonBlock MAGMA_SLATE = new DungeonBlock("magma_slate", MaterialColor.TERRACOTTA_ORANGE, ITEMS, BLOCKS);
	public static final DungeonBlock MOSS_BRICK = new DungeonBlock("moss_brick", MaterialColor.STONE, ITEMS, BLOCKS);
	public static final DungeonBlock MUD_BRICK = new DungeonBlock("mud_brick", MaterialColor.DIRT, ITEMS, BLOCKS);
	public static final DungeonBlock ROCK = new DungeonBlock("rock", MaterialColor.TERRACOTTA_ORANGE, ITEMS, BLOCKS);
	public static final DungeonBlock SAND_BRICK = new DungeonBlock("sand_brick", MaterialColor.COLOR_ORANGE, ITEMS, BLOCKS);
	public static final DungeonBlock SAND_SLATE = new DungeonBlock("sand_slate", MaterialColor.COLOR_ORANGE, ITEMS, BLOCKS);
	public static final DungeonBlock SHADOW_BRICK = new DungeonBlock("shadow_brick", MaterialColor.TERRACOTTA_LIGHT_BLUE, ITEMS, BLOCKS);
	public static final DungeonBlock WATER_BRICK = new DungeonBlock("water_brick", MaterialColor.COLOR_CYAN, ITEMS, BLOCKS);
	public static final DungeonBlock MOSS_SLATE = new DungeonBlock("moss_slate", MaterialColor.WOOD, ITEMS, BLOCKS);

	//dungeon doors
	public static final RegistryObject<Block> FIRE_DOOR = registerDoor("fire_door");
	public static final RegistryObject<Block> RUST_DOOR = registerDoor("rust_door");
	public static final RegistryObject<Block> SAND_DOOR = registerDoor("sand_door");
	public static final RegistryObject<Block> STONE_DOOR = registerDoor("stone_door");
	public static final RegistryObject<Block> WOOD_DOOR = registerDoor("wood_door");

	//functional blocks
	public static final RegistryObject<Block> CHEST_SPAWNER = register("chest_spawner", ChestSpawnerBlock :: new, true);
	public static final RegistryObject<Block> REDSTONE_ACTIVATOR = register("redstone_activator", RedstoneActivatorBlock :: new, true);

	//triggerable blocks
	public static final RegistryObject<Block> REDSTONE_TRIGGER = register("redstone_trigger", RedstoneTriggerBlock :: new, true);

	private static RegistryObject<Block> registerDoor(String name) {
		return register(name, () -> new DungeonDoorBlock(DUNGEON_PROPS), true);
	}

	private static RegistryObject<Block> register(String name, Supplier<Block> supplier, boolean isFunctional) {
		RegistryObject<Block> block = BLOCKS.register(name, supplier);
		ITEMS.register(block.getId().getPath(), () -> new BlockItemDungeon(block.get(), name, isFunctional ? CreativeTabs.DUNGEON_FUNCTIONAL_BLOCKS : CreativeTabs.DUNGEON_BLOCKS));
		return block;
	}

}
