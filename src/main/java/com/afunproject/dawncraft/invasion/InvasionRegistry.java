package com.afunproject.dawncraft.invasion;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;

import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

public class InvasionRegistry {

	private static final Map<InvasionKey, InvasionEntry> ENTRIES = Maps.newHashMap();
	private static final Map<Integer, ItemStack> REWARDS = Maps.newHashMap();

	public static void register(InvasionEntry entry) {
		ENTRIES.put(InvasionKey.create(entry.name.toUpperCase(Locale.US), entry.name), entry);
	}
	
	public static void addReward(int kills, ItemStack reward) {
		REWARDS.put(kills, reward);
	}

	public static InvasionEntry getInvasion(String name) {
		return getInvasion(InvasionKey.getKey(name));
	}

	public static List<InvasionEntry> getInvasions() {
		List<InvasionEntry> invasions = Lists.newArrayList();
		for (InvasionEntry invasion : ENTRIES.values()) invasions.add(invasion.copy());
		return invasions;
	}

	public static List<String> getKeys() {
		return ENTRIES.keySet().stream().map(InvasionKey::toString).collect(Collectors.toList());
	}

	public static InvasionEntry getInvasion(InvasionKey key) {
		return ENTRIES.get(key);
	}
	
	public static ItemStack getReward(int kills) {
		ItemStack stack = REWARDS.get(kills);
		return stack == null ? ItemStack.EMPTY : stack;
	}
	
	public static boolean contains(Entity entity) {
		for (InvasionEntry entry : ENTRIES.values()) if (entry.entities.contains(entity.getType())) return true;
		return false;
	}
	
}
