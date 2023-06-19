package com.afunproject.dawncraft.integration.quests.custom.quests;

import com.afunproject.dawncraft.Constants;
import com.afunproject.dawncraft.integration.quests.custom.quests.dc.*;
import com.google.common.collect.Maps;
import net.minecraft.resources.ResourceLocation;

import java.util.Map;

public class QuestsRegistry {

	private final static Map<ResourceLocation, Quest> QUESTS = Maps.newHashMap();

	static {
		registerDCQuests();
	}

	public static Quest registerQuest(ResourceLocation registry, Quest quest) {
		quest.setRegistryName(registry);
		QUESTS.put(registry, quest);
		return quest;
	}

	public static Quest getQuest(ResourceLocation registry) {
		return QUESTS.get(registry);
	}

	public static void registerDCQuests() {
		//intro
		registerQuest(Constants.loc("intro"), new IntroQuest());
		//patreon quests
		registerQuest(Constants.loc("werewolf"), new WerewolfQuest());
		registerQuest(Constants.loc("ghost"), new BellGhostQuest());
		registerQuest(Constants.loc("cult"), new CultQuest());
		registerQuest(Constants.loc("barrel"), new BarrelQuest());
		registerQuest(Constants.loc("alchemist"), new AlchemistQuest());
		registerQuest(Constants.loc("mask_ghost"), new MaskGhostQuest());
		registerQuest(Constants.loc("witch"), new WitchQuest());
		registerQuest(Constants.loc("king"), new KingQuest());
		registerQuest(Constants.loc("monster_slayer"), new MonsterSlayerQuest());
		registerQuest(Constants.loc("sage"), new SageQuest());
		registerQuest(Constants.loc("sorcerer"), new SorcererQuest());
	}

}
