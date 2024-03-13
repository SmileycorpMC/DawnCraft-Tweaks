package com.afunproject.dawncraft.integration.champions;

import net.minecraft.world.entity.Entity;
import net.minecraftforge.common.util.LazyOptional;
import top.theillusivec4.champions.api.IChampion;
import top.theillusivec4.champions.common.capability.ChampionCapability;

public class ChampionsIntegration {
    public static boolean isChampion(Entity entity) {
        LazyOptional<IChampion> optional = ChampionCapability.getCapability(entity);
        if (!optional.isPresent()) return false;
        IChampion champion = optional.orElse(null);
        return champion.getServer().getRank().isPresent() ? champion.getServer().getRank().get().getTier() > 0 : false;
    }
    
}
