package com.afunproject.dawncraft.integration.epicfight.client;

import com.afunproject.dawncraft.client.ClientHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.ModList;
import yesman.epicfight.client.input.EpicFightKeyMappings;

public class EpicFightClientEvents {

	public static void init() {
		registerKeybinds();
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

}
