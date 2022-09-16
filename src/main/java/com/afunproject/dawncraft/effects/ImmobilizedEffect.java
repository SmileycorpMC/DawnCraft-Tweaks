package com.afunproject.dawncraft.effects;

import java.util.List;

import com.google.common.collect.Lists;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.item.ItemStack;

public class ImmobilizedEffect extends MobEffect {

	protected ImmobilizedEffect() {
		super(MobEffectCategory.HARMFUL, 0xFFB8DBDB);
	}

	@Override
	public List<ItemStack> getCurativeItems() {
		return Lists.newArrayList();
	}

}
