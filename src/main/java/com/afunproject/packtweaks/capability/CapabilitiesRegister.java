package com.afunproject.packtweaks.capability;

import com.afunproject.packtweaks.PackTweaks;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class CapabilitiesRegister {

	public static Capability<IFollowQuest> FOLLOW_QUEST_CAPABILITY = CapabilityManager.get(new CapabilityToken<IFollowQuest>(){});

	@SubscribeEvent
	public void registerCapabilities(RegisterCapabilitiesEvent event) {
		event.register(IFollowQuest.class);
	}

	@SubscribeEvent
	public void attachEntityCapabilities(AttachCapabilitiesEvent<Entity> event) {
		Entity entity = event.getObject();
		if (entity instanceof Mob) {
			event.addCapability(PackTweaks.location("follow_quest"), new IFollowQuest.Provider());
		}
	}

}
