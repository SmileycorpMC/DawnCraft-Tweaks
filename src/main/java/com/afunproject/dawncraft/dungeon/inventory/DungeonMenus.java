package com.afunproject.dawncraft.dungeon.inventory;

import com.afunproject.dawncraft.Constants;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.network.IContainerFactory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class DungeonMenus {

	public static DeferredRegister<MenuType<?>> MENU_TYPES = DeferredRegister.create(ForgeRegistries.CONTAINERS, Constants.MODID);

	/*public static final RegistryObject<MenuType<RedstoneActivatorMenu>> REDSTONE_ACTIVATOR_MENU = register("redstone_activator",
			(id, inv, buf) -> new RedstoneActivatorMenu(id));*/

	@SuppressWarnings("unused")
	private static <T extends AbstractContainerMenu> RegistryObject<MenuType<T>> register(String name, IContainerFactory<T> supplier) {
		return MENU_TYPES.register(name, () -> IForgeMenuType.<T>create(supplier));
	}

}
