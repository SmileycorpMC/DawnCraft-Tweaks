package com.afunproject.dawncraft.integration.quests.custom;

public enum QuestType {

	//can accept or deny quest, with different results based on choice
	ACCEPT_QUEST,
	//automatically accepts quest when clicking on the last buttons
	ACKNOWLEDGE,
	//automatically denies quest when clicking on the last buttons
	DENY,
	//has next page button on the last page, closes when pressed
	AUTO_CLOSE,
	//has several large buttons which link to a different page
	OPTIONS;

}
