package com.afunproject.dawncraft.dungeon.block.entity.interfaces;

import com.afunproject.dawncraft.dungeon.KeyColour;

public interface Lockable {

	public KeyColour getLockColour();

	public void setLockColour(KeyColour lock);

	public void unlock();

}
