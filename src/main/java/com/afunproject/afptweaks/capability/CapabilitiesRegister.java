package com.afunproject.afptweaks.capability;

import com.afunproject.afptweaks.ModDefinitions;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class CapabilitiesRegister {

	public static Capability<IFollowQuest> FOLLOW_QUEST_CAPABILITY = CapabilityManager.get(new CapabilityToken<IFollowQuest>(){});
	public static Capability<IRestrictBlock> RESTRICT_BLOCK_CAPABILITY = CapabilityManager.get(new CapabilityToken<IRestrictBlock>(){});

	@SubscribeEvent
	public void registerCapabilities(RegisterCapabilitiesEvent event) {
		event.register(IFollowQuest.class);
		event.register(IRestrictBlock.class);
	}

	@SubscribeEvent
	public void attachEntityCapabilities(AttachCapabilitiesEvent<Entity> event) {
		Entity entity = event.getObject();
		if (entity instanceof Mob) {
			event.addCapability(ModDefinitions.getResource("follow_quest"), new IFollowQuest.Provider());
		}
		if (entity instanceof PathfinderMob) {
			event.addCapability(ModDefinitions.getResource("restrict_block"), new IRestrictBlock.Provider());
		}
	}

}
