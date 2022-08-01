package com.afunproject.afptweaks.dungeon.block.entity.interfaces;

import com.afunproject.afptweaks.dungeon.KeyColour;

public interface Lockable {

	public KeyColour getLockColour();

	public void setLockColour(KeyColour lock);

	public void unlock();

}
