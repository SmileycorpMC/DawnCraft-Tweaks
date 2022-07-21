package com.afunproject.afptweaks.dungeon.block.entity;

import java.util.stream.Stream;

import com.afunproject.afptweaks.ModDefinitions;
import com.afunproject.afptweaks.dungeon.block.DungeonBlocks;

import net.minecraft.Util;
import net.minecraft.util.datafix.fixes.References;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.BlockEntityType.BlockEntitySupplier;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class DungeonBlockEntities {

	public static DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITIES, ModDefinitions.MODID);

	public static final RegistryObject<BlockEntityType<DungeonDoorBlockEntity>> DUNGEON_DOOR = register("dungeon_door", DungeonDoorBlockEntity :: new,
			DungeonBlocks.FIRE_DOOR, DungeonBlocks.RUST_DOOR, DungeonBlocks.SAND_DOOR, DungeonBlocks.STONE_DOOR, DungeonBlocks.WOOD_DOOR);
	public static final RegistryObject<BlockEntityType<ChestSpawnerBlockEntity>> CHEST_SPAWNER = register("chest_spawner",
			ChestSpawnerBlockEntity :: new, DungeonBlocks.CHEST_SPAWNER);
	public static final RegistryObject<BlockEntityType<RedstoneTriggerBlockEntity>> REDSTONE_TRIGGER = register("redstone_trigger",
			RedstoneTriggerBlockEntity :: new, DungeonBlocks.REDSTONE_TRIGGER);

	@SafeVarargs
	private static <T extends BlockEntity> RegistryObject<BlockEntityType<T>> register(String name, BlockEntitySupplier<T> supplier, RegistryObject<Block>... blocks) {
		return BLOCK_ENTITIES.register(name, () ->
		BlockEntityType.Builder.of(supplier, Stream.of(blocks).map((block)->block.get()).toList().toArray(new Block[] {}))
		.build(Util.fetchChoiceType(References.BLOCK_ENTITY, ModDefinitions.getResourceName(name))));
	}

}
