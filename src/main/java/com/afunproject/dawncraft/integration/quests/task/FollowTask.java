package com.afunproject.dawncraft.integration.quests.task;

import com.feywild.quest_giver.quest.task.TaskType;
import com.google.gson.JsonObject;
import net.minecraft.server.level.ServerPlayer;

public class FollowTask implements TaskType<String, String> {

	public static FollowTask INSTANCE = new FollowTask();

	@Override
	public Class<String> element() {
		return String.class;
	}

	@Override
	public Class<String> testType() {
		return String.class;
	}

	@Override
	public boolean checkCompleted(ServerPlayer player, String element, String match) {
		if (element == null || match == null) return false;
		if ((element.contains("#") &! match.contains("#")) || (match.contains("#") &! element.contains("#"))) return false;
		return element.replace("#", "").equals(match.replace("#", ""));
	}

	@Override
	public String fromJson(JsonObject json) {
		return json.get("structure").getAsString();
	}

	@Override
	public JsonObject toJson(String element) {
		JsonObject json = new JsonObject();
		json.addProperty("structure", element);
		return json;
	}

}
