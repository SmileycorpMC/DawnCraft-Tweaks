package com.afunproject.dawncraft.integration.epicfight.client;

import com.afunproject.dawncraft.client.ClientHandler;
import net.minecraft.client.gui.components.toasts.Toast;
import net.minecraft.client.gui.components.toasts.ToastComponent;
import net.minecraft.world.item.Items;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.ModList;
import yesman.epicfight.client.input.EpicFightKeyMappings;
import yesman.epicfight.world.item.EpicFightItems;

public class EpicFightClientEvents {

	public static void init() {
		registerKeybinds();
		MinecraftForge.EVENT_BUS.register(EpicFightClientEvents.class);
		if (ModList.get().isLoaded("paraglider")) MinecraftForge.EVENT_BUS.register(new EpicFightParagliderEvents());
	}

	private static void registerKeybinds() {
		ClientHandler.IMMOBILIZED_KEYS.add(EpicFightKeyMappings.DODGE);
		ClientHandler.IMMOBILIZED_KEYS.add(EpicFightKeyMappings.GUARD);
		ClientHandler.IMMOBILIZED_KEYS.add(EpicFightKeyMappings.LOCK_ON);
		ClientHandler.IMMOBILIZED_KEYS.add(EpicFightKeyMappings.MOVER_SKILL);
		ClientHandler.IMMOBILIZED_KEYS.add(EpicFightKeyMappings.WEAPON_INNATE_SKILL);
		ClientHandler.FROG_KEYS.add(EpicFightKeyMappings.DODGE);
		ClientHandler.FROG_KEYS.add(EpicFightKeyMappings.GUARD);
		ClientHandler.FROG_KEYS.add(EpicFightKeyMappings.LOCK_ON);
		ClientHandler.FROG_KEYS.add(EpicFightKeyMappings.MOVER_SKILL);
		ClientHandler.FROG_KEYS.add(EpicFightKeyMappings.WEAPON_INNATE_SKILL);
	}

	public static void displayToast(ToastComponent toasts, byte type) {
		Toast toast = null;
		switch (type) {
			case 1:
				toast = new KeyToast("toasts.dawncraft.combatmode", EpicFightKeyMappings.SWITCH_MODE, Items.IRON_SWORD);
				break;
			case 2:
				toast = new KeyToast("toasts.dawncraft.dodge", EpicFightKeyMappings.DODGE, Items.FEATHER);
				break;
			case 4:
				toast = new KeyToast("toasts.dawncraft.skills", EpicFightKeyMappings.SKILL_EDIT, EpicFightItems.SKILLBOOK.get());
				break;
		}
		if (toast != null) toasts.addToast(toast);
	}
}
