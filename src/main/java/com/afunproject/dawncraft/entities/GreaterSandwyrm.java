package com.afunproject.dawncraft.entities;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;

public class GreaterSandwyrm extends Monster {

	/*public GreaterSandwyrm(Level level) {
		this(DawnCraftEntities.GREATER_SANDWYRM.get(), level);
	}*/

	public GreaterSandwyrm(EntityType<? extends GreaterSandwyrm> type, Level level) {
		super(type, level);
	}

}
