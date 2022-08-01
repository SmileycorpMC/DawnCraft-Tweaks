package com.afunproject.afptweaks.quest.task;

import com.afunproject.afptweaks.ModDefinitions;
import com.feywild.quest_giver.quest.task.TaskTypes;

import net.minecraftforge.common.MinecraftForge;

public class QuestModule {

	public static void init() {
		MinecraftForge.EVENT_BUS.register(new QuestEventListener());
		TaskTypes.register(ModDefinitions.getResource("follow_quest"), FollowTask.INSTANCE);
		TaskTypes.register(ModDefinitions.getResource("advancement"), AdvancementTask.INSTANCE);
	}

}
