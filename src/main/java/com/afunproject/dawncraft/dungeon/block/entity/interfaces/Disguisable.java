package com.afunproject.dawncraft.dungeon.block.entity.interfaces;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;

public interface Disguisable {

	public ResourceLocation getTexture();

	public default void setTexture(Block block) {
		setTexture(block.getRegistryName());
	}

	public void setTexture(ResourceLocation block);

}
