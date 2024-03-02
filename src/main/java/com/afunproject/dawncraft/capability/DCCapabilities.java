package com.afunproject.dawncraft.capability;

import com.afunproject.dawncraft.Constants;
import com.afunproject.dawncraft.integration.quests.custom.QuestEntity;
import com.afunproject.dawncraft.invasion.InvasionRegistry;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.apache.commons.lang3.ArrayUtils;

public class DCCapabilities {

	private static EntityType<?>[] QUEST_ENTITIES = {};

	public static Capability<FollowQuest> FOLLOW_QUEST = CapabilityManager.get(new CapabilityToken<>(){});
	public static Capability<RestrictBlock> RESTRICT_BLOCK = CapabilityManager.get(new CapabilityToken<>(){});
	public static Capability<Invasion> INVASIONS = CapabilityManager.get(new CapabilityToken<>(){});
	public static Capability<Invader> INVADER = CapabilityManager.get(new CapabilityToken<>(){});
	public static Capability<QuestEntity> QUEST_ENTITY = CapabilityManager.get(new CapabilityToken<>(){});
	public static Capability<SpawnTracker> SPAWN_TRACKER = CapabilityManager.get(new CapabilityToken<>(){});
	public static Capability<SageQuestTracker> SAGE_QUEST_TRACKER = CapabilityManager.get(new CapabilityToken<>(){});
	public static Capability<Toasts> TOASTS = CapabilityManager.get(new CapabilityToken<>(){});

	@SubscribeEvent
	public void registerCapabilities(RegisterCapabilitiesEvent event) {
		event.register(FollowQuest.class);
		event.register(RestrictBlock.class);
		event.register(Invasion.class);
		event.register(Invader.class);
		event.register(QuestEntity.class);
		event.register(SpawnTracker.class);
		event.register(SageQuestTracker.class);
		event.register(Toasts.class);
	}

	@SubscribeEvent
	public void attachEntityCapabilities(AttachCapabilitiesEvent<Entity> event) {
		Entity entity = event.getObject();
		if (entity instanceof LivingEntity && InvasionRegistry.contains(entity)) {
			event.addCapability(Constants.loc("invader"), new Invader.Provider((LivingEntity) entity));
		}
		if (entity instanceof Mob) {
			event.addCapability(Constants.loc("follow_quest"), new FollowQuest.Provider());
		}
		if (entity instanceof PathfinderMob) {
			event.addCapability(Constants.loc("restrict_block"), new RestrictBlock.Provider());
		}
		if (entity instanceof Player) {
			event.addCapability(Constants.loc("invasions"), new Invasion.Provider((Player) entity));
			event.addCapability(Constants.loc("sage_quest"), new SageQuestTracker.Provider());
			event.addCapability(Constants.loc("toasts"), new Toasts.Provider());
		}
		if (ArrayUtils.contains(QUEST_ENTITIES, entity.getType())) {
			event.addCapability(Constants.loc("quest"), new QuestProvider());
		}
		if (entity instanceof Mob || entity instanceof Player) {
			event.addCapability(Constants.loc("spawn_tracker"), new SpawnTracker.Provider());
		}
	}

}
