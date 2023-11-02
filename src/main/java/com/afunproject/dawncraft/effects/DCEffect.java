package com.afunproject.dawncraft.effects;

import com.google.common.collect.Lists;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TranslatableComponent;
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

	@Override
	public Component getDisplayName() {
		return new TranslatableComponent(getDescriptionId()).withStyle(Style.EMPTY.withColor(getColor()));
	}

}
