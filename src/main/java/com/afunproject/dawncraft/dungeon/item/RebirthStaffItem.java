package com.afunproject.dawncraft.dungeon.item;

import java.util.List;

import javax.annotation.Nullable;

import com.afunproject.dawncraft.CreativeTabs;

import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.BaseComponent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

public class RebirthStaffItem extends Item {

	public RebirthStaffItem() {
		super(new Properties().stacksTo(1).tab(CreativeTabs.DUNGEON_ITEMS).rarity(Rarity.RARE));
	}

	@Override
	public Component getName(ItemStack stack) {
		return ((BaseComponent)super.getName(stack)).withStyle(ChatFormatting.LIGHT_PURPLE);
	}

	@Override
	public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> lines, TooltipFlag flag) {
		boolean isPowered = isPowered(stack);
		String text = "tooltip.dawncraft.rebirth_staff." + (isPowered ? "powered_" : "inert_");
		lines.add(new TranslatableComponent(text+"0").withStyle(isPowered ? Style.EMPTY.applyFormat(ChatFormatting.LIGHT_PURPLE) : Style.EMPTY));
		lines.add(new TranslatableComponent(text+"1").withStyle(Style.EMPTY.withItalic(true)));
	}

	@Override
	public boolean isFoil(ItemStack stack) {
		return isPowered(stack);
	}

	public static boolean isPowered(ItemStack stack) {
		CompoundTag tag = stack.getOrCreateTag();
		if (tag.contains("powered")) {
			if (tag.getBoolean("powered")) return true;
		}
		return false;
	}

}
