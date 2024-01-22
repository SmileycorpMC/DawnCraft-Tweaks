package com.afunproject.dawncraft.dungeon.item;

import com.afunproject.dawncraft.CreativeTabs;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.BaseComponent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.smileycorp.atlas.api.item.CustomTier;
import yesman.epicfight.world.item.GreatswordItem;

import javax.annotation.Nullable;
import java.util.List;

public class ExecutionerItem extends GreatswordItem {

	public ExecutionerItem() {
		super(new Properties().tab(CreativeTabs.DUNGEON_ITEMS).setNoRepair().stacksTo(1).rarity(Rarity.EPIC),
				new CustomTier(1000, -3.25f, 5, 0, 0, () -> Ingredient.EMPTY));
	}

	@Override
	public Component getName(ItemStack stack) {
		BaseComponent component = ((BaseComponent)super.getName(stack));
		return component.withStyle(component.getStyle().withColor(0xFF9300).withBold(true));
	}

	@Override
	public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> lines, TooltipFlag flag) {
		lines.add(new TranslatableComponent("tooltip.dawncraft.executioner_0").withStyle(Style.EMPTY.withItalic(true)));
		lines.add(new TranslatableComponent("tooltip.dawncraft.executioner_1").withStyle(Style.EMPTY.withColor(ChatFormatting.RED)));
	}

}
