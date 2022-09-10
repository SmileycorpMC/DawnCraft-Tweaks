package com.afunproject.dawncraft.dungeon.block.entity.interfaces;

import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;

public interface RotatableBlockEntity {

	public Direction getDirection();

	public void setDirection(Direction direction);

	public void rotate(Rotation rrotation);

	public void mirror(Mirror direction);

}
