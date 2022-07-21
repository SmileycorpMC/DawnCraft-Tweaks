package com.afunproject.afptweaks.dungeon.item;

import java.util.Map;

import com.afunproject.afptweaks.ModDefinitions;
import com.afunproject.afptweaks.dungeon.KeyColour;
import com.google.common.collect.Maps;

import net.minecraft.world.item.Item;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class DungeonItems {

	public static DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, ModDefinitions.MODID);

	private static Map<KeyColour, RegistryObject<Item>> KEY_MAP = Maps.newHashMap();
	private static Map<KeyColour, RegistryObject<Item>> LOCK_MAP = Maps.newHashMap();

	public static final RegistryObject<Item> DUNGEON_CONFIGURATOR = ITEMS.register("dungeon_configurator", DungeonConfiguratorItem :: new);

	static {
		for (KeyColour colour : KeyColour.values()) {
			KEY_MAP.put(colour, ITEMS.register(colour.getName() + "_key", ()-> new KeyItem(colour)));
			LOCK_MAP.put(colour, ITEMS.register(colour.getName() + "_lock", ()-> new LockItem(colour)));
		}
	}

	public static KeyItem getKey(KeyColour colour) {
		return (KeyItem) KEY_MAP.get(colour).get();
	}

	public static LockItem getLock(KeyColour colour) {
		return (LockItem) LOCK_MAP.get(colour).get();
	}

}
