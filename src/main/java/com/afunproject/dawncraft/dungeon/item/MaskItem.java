package com.afunproject.dawncraft.dungeon.item;

import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Wearable;
import net.minecraft.world.level.Level;

public class MaskItem extends Item implements Wearable {

	public MaskItem(Properties props) {
		super(props);
	}

	@Override
	public EquipmentSlot getEquipmentSlot(ItemStack stack) {
		return EquipmentSlot.HEAD;
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level p_40395_, Player p_40396_, InteractionHand p_40397_) {
		ItemStack itemstack = p_40396_.getItemInHand(p_40397_);
		EquipmentSlot equipmentslot = Mob.getEquipmentSlotForItem(itemstack);
		ItemStack itemstack1 = p_40396_.getItemBySlot(equipmentslot);
		if (itemstack1.isEmpty()) {
			p_40396_.setItemSlot(equipmentslot, itemstack.copy());
			if (!p_40395_.isClientSide()) {
				p_40396_.awardStat(Stats.ITEM_USED.get(this));
			}

			itemstack.setCount(0);
			return InteractionResultHolder.sidedSuccess(itemstack, p_40395_.isClientSide());
		} else {
			return InteractionResultHolder.fail(itemstack);
		}
	}

}
