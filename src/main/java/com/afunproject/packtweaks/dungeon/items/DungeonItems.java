package com.afunproject.packtweaks.dungeon.items;

import java.util.Map;

import com.afunproject.packtweaks.PackTweaks;
import com.afunproject.packtweaks.dungeon.KeyColour;
import com.google.common.collect.Maps;

import net.minecraft.world.item.Item;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class DungeonItems {

	public static DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, PackTweaks.MODID);

	private static Map<KeyColour, RegistryObject<Item>> KEY_MAP = Maps.newHashMap();

	static {
		for (KeyColour colour : KeyColour.values()) {
			KEY_MAP.put(colour, ITEMS.register(colour.getName()+"_key", ()->new KeyItem(colour)));
		}
	}

	public KeyItem getKey(KeyColour colour) {
		return (KeyItem) KEY_MAP.get(colour).get();
	}
}
