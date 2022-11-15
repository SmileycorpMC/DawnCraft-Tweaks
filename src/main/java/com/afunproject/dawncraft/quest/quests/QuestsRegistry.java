package com.afunproject.dawncraft.quest.quests;

import java.util.Map;

import com.afunproject.dawncraft.ModDefinitions;
import com.google.common.collect.Maps;

import net.minecraft.resources.ResourceLocation;

public class QuestsRegistry {

	private static Map<ResourceLocation, Quest> QUESTS = Maps.newHashMap();

	public static final Quest WEREWOLF_QUEST = registerQuest(ModDefinitions.getResource("werewolf"), new WerewolfQuest());
	public static final Quest GHOST_QUEST = registerQuest(ModDefinitions.getResource("ghost"), new GhostQuest());
	public static final Quest CULT_QUEST = registerQuest(ModDefinitions.getResource("cult"), new CultQuest());
	public static final Quest BARREL_QUEST = registerQuest(ModDefinitions.getResource("barrel"), new BarrelQuest());
	public static final Quest ALCHEMIST = registerQuest(ModDefinitions.getResource("alchemist"), new AlchemistQuest());

	public static Quest registerQuest(ResourceLocation registry, Quest quest) {
		quest.setRegistryName(registry);
		QUESTS.put(registry, quest);
		return quest;
	}

	public static Quest getQuest(ResourceLocation registry) {
		return QUESTS.get(registry);
	}

}
