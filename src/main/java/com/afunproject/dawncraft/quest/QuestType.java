package com.afunproject.dawncraft.quest;

public enum QuestType {

	//can accept or deny quest, with different results based on choice
	ACCEPT_QUEST,
	//automatically accepts quest when clicking on the last buttons
	ACKNOWLEDGE,
	//automatically denies quest when clicking on the last buttons
	DENY,
	//has next page button on the last page, closes when pressed
	AUTO_CLOSE;

}
