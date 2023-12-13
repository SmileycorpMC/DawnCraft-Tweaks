package com.afunproject.dawncraft.integration;

import com.afunproject.dawncraft.integration.dcmobs.SimpleMobsEvents;
import com.afunproject.dawncraft.integration.epicfight.EpicFightCompat;
import com.afunproject.dawncraft.integration.epicfight.client.EpicFightClientEvents;
import com.afunproject.dawncraft.integration.humancompanions.HCEvents;
import com.afunproject.dawncraft.integration.humancompanions.client.HCClientEvents;
import com.afunproject.dawncraft.integration.journeymap.JourneyMapEvents;
import com.afunproject.dawncraft.integration.quests.QuestEvents;
import com.afunproject.dawncraft.integration.quests.client.QuestClientEvents;
import net.minecraftforge.fml.ModList;

public class IntegrationHandler {

	public static void construct() {
		ModList modlist = ModList.get();
		if (modlist.isLoaded("humancompanions")) HCEvents.init();
		if (modlist.isLoaded("quest_giver")) QuestEvents.init();
		if (modlist.isLoaded("simple_mobs")) SimpleMobsEvents.init();
		if (modlist.isLoaded("epicfight")) EpicFightCompat.init();
		if (modlist.isLoaded("journeymap")) JourneyMapEvents.init();
	}

	public static void setup() {
		ModList modlist = ModList.get();
		if (modlist.isLoaded("simple_mobs")) SimpleMobsEvents.registerInvasions();
	}

	public static void clientSetup() {
		ModList modlist = ModList.get();
		if (modlist.isLoaded("epicfight")) EpicFightClientEvents.init();
		if (modlist.isLoaded("humancompanions")) HCClientEvents.init();
		if (modlist.isLoaded("quest_giver")) QuestClientEvents.init();
	}

}
