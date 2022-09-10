package com.afunproject.dawncraft;

import com.afunproject.dawncraft.client.ClientHandler;

import yesman.epicfight.client.input.EpicFightKeyMappings;

public class EpicFightCompat {

	public static void registerKeybinds() {
		ClientHandler.IMMOBILIZED_KEYS.add(EpicFightKeyMappings.DODGE);
		ClientHandler.IMMOBILIZED_KEYS.add(EpicFightKeyMappings.SPECIAL_SKILL);
		ClientHandler.IMMOBILIZED_KEYS.add(EpicFightKeyMappings.SPECIAL_SKILL_TOOLTIP);
	}

}
