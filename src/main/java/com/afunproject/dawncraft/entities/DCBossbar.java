package com.afunproject.dawncraft.entities;

import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.world.entity.Mob;

public class DCBossbar extends ServerBossEvent {

    private final Mob entity;

    public DCBossbar(Mob entity, BossBarColor colour) {
        super(entity.getDisplayName(), colour, BossBarOverlay.PROGRESS);
        this.entity = entity;
        setVisible(false);
    }

    public void update() {
        setProgress(entity.getHealth() / entity.getMaxHealth());
        if (!isVisible() && entity.getPersistentData().getBoolean("Wake")) setVisible(true);
    }

}
