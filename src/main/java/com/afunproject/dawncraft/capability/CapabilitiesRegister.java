package com.afunproject.dawncraft.capability;

import org.apache.commons.lang3.ArrayUtils;

import com.afunproject.dawncraft.Constants;
import com.afunproject.dawncraft.integration.quests.custom.QuestEntity;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class CapabilitiesRegister {

	private static EntityType<?>[] QUEST_ENTITIES = {};

	public static Capability<FollowQuest> FOLLOW_QUEST = CapabilityManager.get(new CapabilityToken<FollowQuest>(){});
	public static Capability<RestrictBlock> RESTRICT_BLOCK = CapabilityManager.get(new CapabilityToken<RestrictBlock>(){});
	public static Capability<Invasions> INVASIONS = CapabilityManager.get(new CapabilityToken<Invasions>(){});
	public static Capability<QuestEntity> QUEST_ENTITY = CapabilityManager.get(new CapabilityToken<QuestEntity>(){});
	public static Capability<SpawnTracker> SPAWN_TRACKER = CapabilityManager.get(new CapabilityToken<SpawnTracker>(){});
	public static Capability<SageQuestTracker> SAGE_QUEST_TRACKER = CapabilityManager.get(new CapabilityToken<SageQuestTracker>(){});
	public static Capability<QuestTracker> QUEST_TRACKER = CapabilityManager.get(new CapabilityToken<QuestTracker>(){});

	@SubscribeEvent
	public void registerCapabilities(RegisterCapabilitiesEvent event) {
		event.register(FollowQuest.class);
		event.register(RestrictBlock.class);
		event.register(Invasions.class);
		event.register(QuestEntity.class);
		event.register(SpawnTracker.class);
		event.register(SageQuestTracker.class);
		event.register(QuestTracker.class);
	}

	@SubscribeEvent
	public void attachEntityCapabilities(AttachCapabilitiesEvent<Entity> event) {
		Entity entity = event.getObject();
		if (entity instanceof Mob) {
			event.addCapability(Constants.loc("follow_quest"), new FollowQuest.Provider());
		}
		if (entity instanceof PathfinderMob) {
			event.addCapability(Constants.loc("restrict_block"), new RestrictBlock.Provider());
		}
		if (ArrayUtils.contains(QUEST_ENTITIES, entity.getType())) {
			event.addCapability(Constants.loc("quest"), new QuestProvider());
		}
		if (entity instanceof Mob || entity instanceof Player) {
			event.addCapability(Constants.loc("spawn_tracker"), new SpawnTracker.Provider());
		}
		if (entity instanceof Player) {
			event.addCapability(Constants.loc("sage_quest"), new SageQuestTracker.Provider());
			event.addCapability(Constants.loc("quests"), new QuestTracker.Provider());
			event.addCapability(Constants.loc("invasions"), new Invasions.Provider());
		}
	}

}
