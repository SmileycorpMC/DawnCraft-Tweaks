package com.afunproject.dawncraft.integration.quests.custom.quests;

import java.util.Map;

import com.afunproject.dawncraft.ModDefinitions;
import com.google.common.collect.Maps;

import net.minecraft.resources.ResourceLocation;

public class QuestsRegistry {

	private final static Map<ResourceLocation, Quest> QUESTS = Maps.newHashMap();

	public static final Quest INTRO_QUEST = registerQuest(ModDefinitions.getResource("intro"), new IntroQuest());

	public static final Quest WEREWOLF_QUEST = registerQuest(ModDefinitions.getResource("werewolf"), new WerewolfQuest());
	public static final Quest BELL_GHOST_QUEST = registerQuest(ModDefinitions.getResource("ghost"), new BellGhostQuest());
	public static final Quest CULT_QUEST = registerQuest(ModDefinitions.getResource("cult"), new CultQuest());
	public static final Quest BARREL_QUEST = registerQuest(ModDefinitions.getResource("barrel"), new BarrelQuest());
	public static final Quest ALCHEMIST = registerQuest(ModDefinitions.getResource("alchemist"), new AlchemistQuest());
	public static final Quest MASK_GHOST_QUEST = registerQuest(ModDefinitions.getResource("mask_ghost"), new MaskGhostQuest());
	public static final Quest WITCH_QUEST = registerQuest(ModDefinitions.getResource("witch"), new WitchQuest());

	public static Quest registerQuest(ResourceLocation registry, Quest quest) {
		quest.setRegistryName(registry);
		QUESTS.put(registry, quest);
		return quest;
	}

	public static Quest getQuest(ResourceLocation registry) {
		return QUESTS.get(registry);
	}

}
