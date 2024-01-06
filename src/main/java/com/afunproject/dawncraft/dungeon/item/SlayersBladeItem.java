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
import net.minecraft.world.item.*;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.smileycorp.atlas.api.item.CustomTier;

import javax.annotation.Nullable;
import java.util.List;
import java.util.UUID;

public class SlayersBladeItem extends SwordItem {

	private static final UUID BASE_MOVE_SPEED_UUID = UUID.fromString("ee70a2df-5286-46eb-a852-28da3c795f1c");
	private final ImmutableMultimap<Attribute, AttributeModifier> modifiers;

	public SlayersBladeItem() {
		super(new CustomTier(1000, -3.05f, 0, 0, 0, ()->Ingredient.EMPTY), 0, -3.05f, new Properties().tab(CreativeTabs.DUNGEON_ITEMS)
				.setNoRepair().stacksTo(1).fireResistant().rarity(Rarity.EPIC));
		ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
		builder.put(Attributes.ATTACK_SPEED, new AttributeModifier(BASE_ATTACK_SPEED_UUID, "Weapon modifier", -4, AttributeModifier.Operation.ADDITION));
		builder.put(Attributes.ATTACK_SPEED, new AttributeModifier(BASE_MOVE_SPEED_UUID, "Weapon modifier", -2, AttributeModifier.Operation.ADDITION));
		modifiers = builder.build();
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
	public Multimap<Attribute, AttributeModifier> getDefaultAttributeModifiers(EquipmentSlot slot) {
		return slot == EquipmentSlot.MAINHAND ? modifiers : super.getDefaultAttributeModifiers(slot);
	}

}
