package com.afunproject.dawncraft.quest_giver.quest.task;

import com.afunproject.dawncraft.ModDefinitions;
import com.feywild.quest_giver.quest.task.TaskTypes;

import net.minecraftforge.common.MinecraftForge;

public class QuestModule {

	public static void init() {
		MinecraftForge.EVENT_BUS.register(new QuestEventListener());
		TaskTypes.register(ModDefinitions.getResource("follow_quest"), FollowTask.INSTANCE);
		TaskTypes.register(ModDefinitions.getResource("advancement"), AdvancementTask.INSTANCE);
	}

}
