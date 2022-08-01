package com.afunproject.afptweaks.dungeon.block.entity.interfaces;

import com.afunproject.afptweaks.dungeon.block.entity.base.TriggerBlockEntityBase;

public interface Functional {

	public boolean canTrigger(TriggerBlockEntityBase source);

	public void trigger(TriggerBlockEntityBase source);


}
