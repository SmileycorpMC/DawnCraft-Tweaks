package com.afunproject.dawncraft.dungeon.block;

import com.afunproject.dawncraft.CreativeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour.Properties;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;
import net.minecraftforge.registries.DeferredRegister;
import net.smileycorp.atlas.api.block.ShapedBlock;

import java.util.function.Function;

public class DungeonBlock extends ShapedBlock {

	public DungeonBlock(String name, MaterialColor colour, DeferredRegister<Item> items, DeferredRegister<Block> blocks) {
		this(name, colour, items, blocks, props->props);
	}

	public DungeonBlock(String name, MaterialColor colour, DeferredRegister<Item> items, DeferredRegister<Block> blocks, Function<Properties, Properties> modifier) {
		super(name, CreativeTabs.DUNGEON_BLOCKS, modifier.apply(Properties.of(Material.STONE, colour).strength(-1, 18000000)), items, blocks, true);
	}

}
