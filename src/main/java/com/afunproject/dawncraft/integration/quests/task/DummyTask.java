package com.afunproject.dawncraft.integration.quests.task;

import com.feywild.quest_giver.quest.task.TaskType;
import com.google.gson.JsonObject;
import net.minecraft.server.level.ServerPlayer;

public class DummyTask implements TaskType<Object, Boolean> {

	@Override
	public Class<Object> element() {
		return Object.class;
	}

	@Override
	public Class<Boolean> testType() {
		return Boolean.class;
	}

	@Override
	public boolean checkCompleted(ServerPlayer player, Object element, Boolean match) {
		return match;
	}

	@Override
	public Object fromJson(JsonObject json) {
		return json;
	}

	@Override
	public JsonObject toJson(Object element) {
		return new JsonObject();
	}

}
