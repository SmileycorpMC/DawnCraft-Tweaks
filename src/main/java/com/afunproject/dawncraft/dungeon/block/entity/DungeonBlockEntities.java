package com.afunproject.dawncraft.dungeon.block.entity;

import com.afunproject.dawncraft.Constants;
import com.afunproject.dawncraft.dungeon.block.DungeonBlocks;
import net.minecraft.Util;
import net.minecraft.util.datafix.fixes.References;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.BlockEntityType.BlockEntitySupplier;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.stream.Collectors;
import java.util.stream.Stream;

public class DungeonBlockEntities {

	public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITIES, Constants.MODID);

	public static final RegistryObject<BlockEntityType<DungeonDoorBlockEntity>> DUNGEON_DOOR = register("dungeon_door", DungeonDoorBlockEntity::new,
			DungeonBlocks.FIRE_DOOR, DungeonBlocks.RUST_DOOR, DungeonBlocks.SAND_DOOR, DungeonBlocks.STONE_DOOR, DungeonBlocks.WOOD_DOOR);
	public static final RegistryObject<BlockEntityType<ChestSpawnerBlockEntity>> CHEST_SPAWNER = register("chest_spawner",
			ChestSpawnerBlockEntity::new, DungeonBlocks.CHEST_SPAWNER);
	public static final RegistryObject<BlockEntityType<RedstoneTriggerBlockEntity>> REDSTONE_TRIGGER = register("redstone_trigger",
			RedstoneTriggerBlockEntity::new, DungeonBlocks.REDSTONE_TRIGGER);
	public static final RegistryObject<BlockEntityType<RedstoneActivatorBlockEntity>> REDSTONE_ACTIVATOR = register("redstone_activator",
			RedstoneActivatorBlockEntity::new, DungeonBlocks.REDSTONE_ACTIVATOR);

	@SafeVarargs
	private static <T extends BlockEntity> RegistryObject<BlockEntityType<T>> register(String name, BlockEntitySupplier<T> supplier, RegistryObject<Block>... blocks) {
		return BLOCK_ENTITIES.register(name, () ->
		BlockEntityType.Builder.of(supplier, Stream.of(blocks).map(RegistryObject::get).collect(Collectors.toList()).toArray(new Block[]{}))
		.build(Util.fetchChoiceType(References.BLOCK_ENTITY, Constants.locStr(name))));
	}

}
