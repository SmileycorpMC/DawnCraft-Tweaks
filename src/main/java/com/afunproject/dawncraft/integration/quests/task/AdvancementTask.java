package com.afunproject.dawncraft.integration.quests.task;

import com.feywild.quest_giver.quest.task.TaskType;
import com.google.gson.JsonObject;
import net.minecraft.advancements.Advancement;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

public class AdvancementTask implements TaskType<ResourceLocation, Advancement> {

	public static AdvancementTask INSTANCE = new AdvancementTask();

	@Override
	public Class<ResourceLocation> element() {
		return ResourceLocation.class;
	}

	@Override
	public Class<Advancement> testType() {
		return Advancement.class;
	}

	@Override
	public boolean checkCompleted(ServerPlayer player, ResourceLocation element, Advancement match) {
		return match.getId().equals(element);
	}

	@Override
	public ResourceLocation fromJson(JsonObject json) {
		return new ResourceLocation(json.get("advancement").getAsString());
	}

	@Override
	public JsonObject toJson(ResourceLocation element) {
		JsonObject json = new JsonObject();
		json.addProperty("advancement", element.toString());
		return json;
	}

}
