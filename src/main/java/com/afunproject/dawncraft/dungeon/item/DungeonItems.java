package com.afunproject.dawncraft.dungeon.item;

import com.afunproject.dawncraft.Constants;
import com.afunproject.dawncraft.dungeon.KeyColour;
import com.google.common.collect.Maps;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.Map;

public class DungeonItems {

	public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Constants.MODID);

	private static final Map<KeyColour, RegistryObject<Item>> KEY_MAP = Maps.newHashMap();
	private static final Map<KeyColour, RegistryObject<Item>> LOCK_MAP = Maps.newHashMap();

	public static final RegistryObject<Item> DUNGEON_CONFIGURATOR = ITEMS.register("dungeon_configurator", DungeonConfiguratorItem::new);
	public static final RegistryObject<Item> SKELETON_KEY = ITEMS.register("skeleton_key", SkeletonKeyItem::new);
	public static final RegistryObject<Item> MASK_OF_ATHORA = ITEMS.register("cursed_mask", MaskOfAthoraItem::new);
	public static final RegistryObject<Item> REBIRTH_STAFF = ITEMS.register("rebirth_staff", RebirthStaffItem::new);
	public static final RegistryObject<Item> SLAYERS_BLADE = ITEMS.register("slayers_blade", SlayersBladeItem::new);
	public static final RegistryObject<Item> EXECUTIONER = ITEMS.register("executioner", ExecutionerItem::new);
	public static final RegistryObject<Item> CRYSTALLIZED_XP = ITEMS.register("crystallized_xp", CrystallizedXPItem::new);

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
