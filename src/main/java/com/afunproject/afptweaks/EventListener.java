package com.afunproject.afptweaks;

import com.afunproject.afptweaks.capability.CapabilitiesRegister;
import com.afunproject.afptweaks.capability.IRestrictBlock;

import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.MoveTowardsRestrictionGoal;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class EventListener {

	@SubscribeEvent
	public void entityJoinWorld(EntityJoinWorldEvent event) {
		if (event.getEntity() instanceof PathfinderMob) {
			PathfinderMob entity = (PathfinderMob) event.getEntity();
			entity.goalSelector.addGoal(5, new MoveTowardsRestrictionGoal(entity, 1.0D));
			LazyOptional<IRestrictBlock> optional = entity.getCapability(CapabilitiesRegister.RESTRICT_BLOCK_CAPABILITY);
			if (optional.isPresent()) {
				IRestrictBlock cap = optional.resolve().get();
				if (cap.canRestrict(entity)) cap.applyRestriction(entity);
			}
		}
	}

}
