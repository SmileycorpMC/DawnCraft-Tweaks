package com.afunproject.afptweaks.quest;

import java.util.Map;

import com.afunproject.afptweaks.ModDefinitions;
import com.google.common.collect.Maps;

import net.minecraft.resources.ResourceLocation;

public class QuestsRegistry {

	private static Map<ResourceLocation, Quest> QUESTS = Maps.newHashMap();

	public static Quest WEREWOLF_QUEST = registerQuest(ModDefinitions.getResource("werewolf"), new WerewolfQuest());
	public static Quest GHOST_QUEST = registerQuest(ModDefinitions.getResource("ghost"), new WerewolfQuest());

	public static Quest registerQuest(ResourceLocation registry, Quest quest) {
		quest.setRegistryName(registry);
		QUESTS.put(registry, quest);
		return quest;
	}

	public static Quest getQuest(ResourceLocation registry) {
		return QUESTS.get(registry);
	}

}
