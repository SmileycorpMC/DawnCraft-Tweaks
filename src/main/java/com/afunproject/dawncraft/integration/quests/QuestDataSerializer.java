package com.afunproject.dawncraft.integration.quests;

import java.util.Map;
import java.util.Map.Entry;

import com.google.common.collect.Maps;

import net.minecraft.nbt.CompoundTag;

public class QuestDataSerializer {

	private final static Map<String, Class<? extends QuestData>> DATA_TYPES = Maps.newHashMap();

	static {
		DATA_TYPES.put("dc", DCQuestData.class);
	}

	public static QuestData loadQuestData(CompoundTag tag) {
		if (tag.contains("type")) {
			String type = tag.getString("type");
			if (DATA_TYPES.containsKey(type)) {
				if (tag.contains("data")) try {
					QuestData data = DATA_TYPES.get(type).getDeclaredConstructor().newInstance();
					if (tag.contains("data")) data.load((CompoundTag) tag.get("data"));
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return null;
	}

	public static CompoundTag saveQuestData(QuestData data) {
		CompoundTag tag = new CompoundTag();
		tag.put("data", data.save());
		Class<? extends QuestData> clazz = data.getClass();
		for (Entry<String, Class<? extends QuestData>> entry : DATA_TYPES.entrySet()) {
			if (entry.getValue() == clazz) {
				tag.putString("type", entry.getKey());
				break;
			}
		}
		return tag;
	}

}
