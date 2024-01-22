package com.afunproject.dawncraft.dungeon.item;

import com.afunproject.dawncraft.CreativeTabs;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.BaseComponent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.smileycorp.atlas.api.item.CustomTier;
import yesman.epicfight.world.item.GreatswordItem;

import javax.annotation.Nullable;
import java.util.List;

public class SlayersBladeItem extends GreatswordItem {

	public SlayersBladeItem() {
		super(new Properties().tab(CreativeTabs.DUNGEON_ITEMS).setNoRepair().stacksTo(1).fireResistant().rarity(Rarity.EPIC),
				new CustomTier(1000, -3.05f, 0, 0, 0, ()->Ingredient.EMPTY));
	}

	@Override
	public Component getName(ItemStack stack) {
		BaseComponent component = ((BaseComponent)super.getName(stack));
		return component.withStyle(component.getStyle().withColor(0xFF9300).withBold(true));
	}

	@Override
	public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> lines, TooltipFlag flag) {
		lines.add(new TranslatableComponent("tooltip.dawncraft.slayers_blade_0").withStyle(Style.EMPTY.withItalic(true)));
		lines.add(new TranslatableComponent("tooltip.dawncraft.slayers_blade_1").withStyle(Style.EMPTY.withColor(ChatFormatting.RED)));
	}

	@Override
	public Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlot slot, ItemStack stack) {
		if (slot == EquipmentSlot.MAINHAND) {
			ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
			builder.put(Attributes.ATTACK_SPEED, new AttributeModifier(BASE_ATTACK_SPEED_UUID, "Weapon modifier", -3.6f, AttributeModifier.Operation.ADDITION));
			builder.put(Attributes.MOVEMENT_SPEED, new AttributeModifier(MOVEMENT_SPEED_MODIFIER, "Weapon modifier", -0.05f, AttributeModifier.Operation.ADDITION));
			return builder.build();
		} else {
			return super.getAttributeModifiers(slot, stack);
		}
	}

}
