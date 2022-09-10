package com.afunproject.dawncraft.dungeon.block.entity.interfaces;

import com.afunproject.dawncraft.dungeon.block.entity.base.TriggerBlockEntityBase;

public interface Functional {

	public boolean canTrigger(TriggerBlockEntityBase source);

	public void trigger(TriggerBlockEntityBase source);


}
