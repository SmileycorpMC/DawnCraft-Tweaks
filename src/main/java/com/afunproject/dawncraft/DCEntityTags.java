package com.afunproject.dawncraft;

import net.minecraft.core.Registry;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;

public class DCEntityTags {

    public static final TagKey<EntityType<?>> BOSSES = TagKey.m_203882_(Registry.ENTITY_TYPE_REGISTRY, Constants.loc("bosses"));
    public static final TagKey<EntityType<?>> BYPASSES_MASK_OF_ATHORA = TagKey.m_203882_(Registry.ENTITY_TYPE_REGISTRY, Constants.loc("bypasses_cursed_mask"));
   
}
