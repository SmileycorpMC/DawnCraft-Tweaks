package com.afunproject.dawncraft.quest.conditions;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class ItemCondition implements QuestCondition {

	protected final ItemStack stack;
	protected final boolean consume;

	public ItemCondition(ItemStack stack) {
		this(stack, true);
	}

	public ItemCondition(ItemStack stack, boolean consume) {
		this.stack = stack;
		this.consume = consume;
	}

	@Override
	public boolean apply(Player player, Mob entity, int phase) {
		if (player.getInventory().contains(stack) && player.getInventory().countItem(stack.getItem())>=stack.getCount()) {
			if (consume) {
				for (ItemStack stack : player.getInventory().items) {
					int count = this.stack.getCount();
					if (stack.getItem() == this.stack.getItem()) {
						int size = stack.getCount();
						stack.shrink(count);
						count = count - size;
						if (count <= 0) break;
					}
				}
			}
			player.playSound(SoundEvents.PLAYER_LEVELUP, 0.5f,  0.5f);
			return true;
		}
		return false;
	}

}
