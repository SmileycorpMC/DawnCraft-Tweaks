package com.afunproject.dawncraft.integration.quests.custom.conditions;

import com.afunproject.dawncraft.DawnCraft;
import com.google.common.collect.Lists;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public class TagCondition implements QuestCondition {

	protected TagKey<Item> tag;
	protected int count;
	protected boolean consume;

	public TagCondition(ResourceLocation loc) {
		this(loc, 1, true);
	}

	public TagCondition(ResourceLocation loc, int count) {
		this(loc, count, true);
	}

	public TagCondition(ResourceLocation loc, int count, boolean consume) {
		tag = TagKey.m_203882_(Registry.ITEM_REGISTRY, loc);
		this.count = count;
		this.consume = consume;
	}

	@Override
	public boolean apply(Player player, Mob entity, int phase, boolean isTest) {
		List<ItemStack> stacks = Lists.newArrayList();
		for (ItemStack stack : player.getInventory().items) {
			if (stack.m_204117_(tag) &! contains(stacks, stack)) {
				stacks.add(stack);
				if (stacks.size() >= count) break;
			}
		}
		if (stacks.size() >= count) {
			if (!isTest && consume) {
				for (ItemStack stack : stacks) stack.shrink(1);
				player.playSound(SoundEvents.PLAYER_LEVELUP, 0.5f,  0.5f);
			}
			return true;
		}
		DawnCraft.logInfo((stacks.size()) + " items found of required " + count);
		return false;
	}

	public boolean contains(List<ItemStack> stacks, ItemStack stack) {
		for (ItemStack stack1 : stacks) if (stack1.getItem() == stack.getItem()) return true;
		return false;
	}

}
