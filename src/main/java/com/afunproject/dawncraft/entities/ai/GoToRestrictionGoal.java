package com.afunproject.dawncraft.entities.ai;

import com.afunproject.dawncraft.capability.RestrictBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.phys.Vec3;

import java.util.EnumSet;

public class GoToRestrictionGoal extends Goal {
    
    private final Mob entity;
    private final RestrictBlock restriction;
    private int ticks;
    
    public GoToRestrictionGoal(Mob entity, RestrictBlock restriction) {
        this.entity = entity;
        this.restriction = restriction;
        setFlags(EnumSet.of(Flag.MOVE));
    }
    
    @Override
    public boolean canUse() {
        return restriction.shouldRestrict(entity);
    }
    
    @Override
    public boolean canContinueToUse() {
        return canUse();
    }
    
    @Override
    public void tick() {
        if (ticks++ % 20 == 0) {
            BlockPos center = restriction.getCenter(entity);
            double distance = Math.sqrt(entity.distanceToSqr(Vec3.atCenterOf(center))) - ((float)restriction.getRange() * 0.75f);
            PathNavigation navigation = entity.getNavigation();
            navigation.moveTo(navigation.createPath(center, (int) distance), 1f);
        }
    }
    
}
