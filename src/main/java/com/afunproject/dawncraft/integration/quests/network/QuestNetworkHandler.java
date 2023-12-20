package com.afunproject.dawncraft.integration.quests.network;

import com.afunproject.dawncraft.integration.quests.client.QuestClientEvents;
import com.afunproject.dawncraft.integration.quests.custom.QuestEntity;
import com.afunproject.dawncraft.integration.quests.custom.quests.Quest;
import com.afunproject.dawncraft.network.DCNetworkHandler;
import net.minecraft.world.entity.Mob;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent.Context;
import net.smileycorp.atlas.api.network.NetworkUtils;

public class QuestNetworkHandler {

	public static void initPackets() {
		NetworkUtils.registerMessage(DCNetworkHandler.NETWORK_INSTANCE, 3, TriggerQuestCompleteMessage.class,
				(T, K)-> processQuestCompleteMessage(T, K.get()));
		NetworkUtils.registerMessage(DCNetworkHandler.NETWORK_INSTANCE, 4, OpenQuestMessage.class,
				(T, K)-> processOpenQuestMessage(T, K.get()));
	}

	public static void processQuestCompleteMessage(TriggerQuestCompleteMessage message, Context ctx) {
		ctx.enqueueWork(() -> {
			Mob entity = message.get(ctx.getSender().level);
			QuestEntity questEntity = QuestEntity.safeCast(entity);
			Quest quest = questEntity.getCurrentQuest();
			if (quest != null) {
				quest.completeQuest(ctx.getSender(), entity, questEntity.getQuestPhase(), message.isAccepted());
			}
		});
		ctx.setPacketHandled(true);
	}

	private static void processOpenQuestMessage(OpenQuestMessage message, Context ctx) {
		ctx.enqueueWork(() -> DistExecutor.safeRunWhenOn(Dist.CLIENT, () -> () -> QuestClientEvents.openQuestGUI(message)));
		ctx.setPacketHandled(true);
	}

}
