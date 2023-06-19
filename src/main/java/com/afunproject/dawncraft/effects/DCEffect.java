package com.afunproject.dawncraft.effects;

import com.google.common.collect.Lists;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public class DCEffect extends MobEffect {

	protected DCEffect(int colour) {
		super(MobEffectCategory.HARMFUL, colour);
	}

	@Override
	public List<ItemStack> getCurativeItems() {
		return Lists.newArrayList();
	}

}
