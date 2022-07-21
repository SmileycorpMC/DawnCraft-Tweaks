package com.afunproject.afptweaks.dungeon.block.entity.base;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;

public interface CamouflagedBlockEntity {

	public ResourceLocation getTexture();

	public default void setTexture(Block block) {
		setTexture(block.getRegistryName());
	}

	public void setTexture(ResourceLocation block);

}
