package com.afunproject.dawncraft.integration.quests.custom.quests.dc;

import com.afunproject.dawncraft.integration.journeymap.JourneyMapEvents;
import com.afunproject.dawncraft.integration.quests.custom.QuestEntity;
import com.afunproject.dawncraft.integration.quests.custom.QuestType;
import com.afunproject.dawncraft.integration.quests.custom.entity.QuestEntityBase;
import com.afunproject.dawncraft.integration.quests.custom.quests.ItemQuest;
import com.afunproject.dawncraft.integration.quests.network.OpenQuestMessage;
import com.afunproject.dawncraft.network.DCNetworkHandler;
import net.mcreator.simplemobs.init.SimpleMobsModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.network.NetworkDirection;

public class CultQuest extends ItemQuest {

	public CultQuest() {
		super(new ItemStack(Items.PAPER), new ItemStack(SimpleMobsModItems.PLAGUEMASK_HELMET.get()));
	}

	@Override
	public void completeQuest(Player quest_completer, Mob entity, int phase, boolean accepted) {
		if (phase == -1) {
			QuestEntity.safeCast(entity).setQuestPhase(2);
		}
		if (phase == 1 && accepted) {
			QuestEntity quest_entity = QuestEntity.safeCast(entity);
			quest_entity.setQuestPhase(-1);
			quest_entity.setQuestText(getText() + "1b");
			DCNetworkHandler.NETWORK_INSTANCE.sendTo(new OpenQuestMessage(entity, this), ((ServerPlayer) quest_completer).connection.connection, NetworkDirection.PLAY_TO_CLIENT);
		} else {
			super.completeQuest(quest_completer, entity, phase, accepted);
		}
	}

	@Override
	protected void completeItemQuest(Player quest_completer, Mob entity, int phase, boolean accepted) {
		if (phase == 3) {
			BlockPos pos = findStructure((ServerLevel)quest_completer.level, new ResourceLocation("custom:church"),  quest_completer.blockPosition());
			ItemStack map = createMap((ServerLevel)quest_completer.level, pos);
			map.setHoverName(new TranslatableComponent("map.dawncraft.cultist"));
			ListTag tag = new ListTag();
			tag.add(StringTag.valueOf(Component.Serializer.toJson(new TranslatableComponent("map.dawncraft.cultist.lore"))));
			map.addTagElement(ItemStack.TAG_LORE, tag);
			giveItem(quest_completer, map);
			if (ModList.get().isLoaded("journeymap")) JourneyMapEvents.addWaypoint(pos, "waypoint.dawncraft.cultist", (ServerPlayer) quest_completer);
		}
		if (phase == end_phase) {
			if (entity instanceof QuestEntityBase) {
				((QuestEntityBase) entity).setDespawnable(true);
			}
			BlockPos pos = findStructure((ServerLevel)quest_completer.level, new ResourceLocation("custom:church_father"),  quest_completer.blockPosition());
			ItemStack map = createMap((ServerLevel)quest_completer.level, pos);
			map.setHoverName(new TranslatableComponent("map.dawncraft.cultist_2"));
			ListTag tag = new ListTag();
			tag.add(StringTag.valueOf(Component.Serializer.toJson(new TranslatableComponent("map.dawncraft.cultist_2.lore"))));
			map.addTagElement(ItemStack.TAG_LORE, tag);
			giveItem(quest_completer, map);
			if (ModList.get().isLoaded("journeymap")) JourneyMapEvents.addWaypoint(pos, "waypoint.dawncraft.cultist_2", (ServerPlayer) quest_completer);
		}
	}

	@Override
	public QuestType getQuestType(int phase) {
		if (phase == -1) return QuestType.AUTO_CLOSE;
		return super.getQuestType(phase);
	}

	@Override
	protected String getText() {
		return "text.dawncraft.quest.cult";
	}

}
