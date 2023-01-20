package com.afunproject.dawncraft.dungeon.block.entity.interfaces;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.model.data.ModelProperty;

public interface Disguisable {

	public static ModelProperty<BlockState> TEXTURE = new ModelProperty<BlockState>();

	public BlockState getTexture();

	public void setTexture(BlockState block);

}
