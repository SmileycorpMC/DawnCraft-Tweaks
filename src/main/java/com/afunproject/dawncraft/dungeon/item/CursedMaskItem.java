package com.afunproject.dawncraft.dungeon.item;

import java.util.List;

import javax.annotation.Nullable;

import com.afunproject.dawncraft.CreativeTabs;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.BaseComponent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

public class CursedMaskItem extends MaskItem {

	public CursedMaskItem() {
		super(new Properties().tab(CreativeTabs.DUNGEON_ITEMS).stacksTo(1));
	}

	@Override
	public Component getName(ItemStack stack) {
		BaseComponent component = ((BaseComponent)super.getName(stack));
		return component.withStyle(component.getStyle().withColor(0x664888));
	}

	@Override
	public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> lines, TooltipFlag flag) {
		lines.add(new TranslatableComponent("tooltip.dawncraft.cursed_mask_0").withStyle(Style.EMPTY.withItalic(true)));
		lines.add(new TranslatableComponent("tooltip.dawncraft.cursed_mask_1").withStyle(Style.EMPTY.withColor(ChatFormatting.RED)));
	}

}
