package com.afunproject.dawncraft.integration.quests.custom.quests;

import java.util.Map;

import com.afunproject.dawncraft.ModDefinitions;
import com.afunproject.dawncraft.integration.quests.custom.quests.dc.AlchemistQuest;
import com.afunproject.dawncraft.integration.quests.custom.quests.dc.BarrelQuest;
import com.afunproject.dawncraft.integration.quests.custom.quests.dc.BellGhostQuest;
import com.afunproject.dawncraft.integration.quests.custom.quests.dc.CultQuest;
import com.afunproject.dawncraft.integration.quests.custom.quests.dc.IntroQuest;
import com.afunproject.dawncraft.integration.quests.custom.quests.dc.KingQuest;
import com.afunproject.dawncraft.integration.quests.custom.quests.dc.MaskGhostQuest;
import com.afunproject.dawncraft.integration.quests.custom.quests.dc.MonsterSlayerQuest;
import com.afunproject.dawncraft.integration.quests.custom.quests.dc.WerewolfQuest;
import com.afunproject.dawncraft.integration.quests.custom.quests.dc.WitchQuest;
import com.google.common.collect.Maps;

import net.minecraft.resources.ResourceLocation;

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
		registerQuest(ModDefinitions.getResource("intro"), new IntroQuest());
		//patreon quests
		registerQuest(ModDefinitions.getResource("werewolf"), new WerewolfQuest());
		registerQuest(ModDefinitions.getResource("ghost"), new BellGhostQuest());
		registerQuest(ModDefinitions.getResource("cult"), new CultQuest());
		registerQuest(ModDefinitions.getResource("barrel"), new BarrelQuest());
		registerQuest(ModDefinitions.getResource("alchemist"), new AlchemistQuest());
		registerQuest(ModDefinitions.getResource("mask_ghost"), new MaskGhostQuest());
		registerQuest(ModDefinitions.getResource("witch"), new WitchQuest());
		registerQuest(ModDefinitions.getResource("king"), new KingQuest());


		registerQuest(ModDefinitions.getResource("monster_slayer"), new MonsterSlayerQuest());
	}

}
