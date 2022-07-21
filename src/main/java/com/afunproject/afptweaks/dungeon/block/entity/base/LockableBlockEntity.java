package com.afunproject.afptweaks.dungeon.block.entity.base;

import com.afunproject.afptweaks.dungeon.KeyColour;

public interface LockableBlockEntity {

	public KeyColour getLockColour();

	public void setLockColour(KeyColour lock);

	public void unlock();

}
