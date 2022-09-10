package com.afunproject.dawncraft.capability;

import com.afunproject.dawncraft.ModDefinitions;

import net.minecraft.world.entity.Entity;
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

	public static Capability<FollowQuest> FOLLOW_QUEST_CAPABILITY = CapabilityManager.get(new CapabilityToken<FollowQuest>(){});
	public static Capability<RestrictBlock> RESTRICT_BLOCK_CAPABILITY = CapabilityManager.get(new CapabilityToken<RestrictBlock>(){});
	public static Capability<Invasions> INVASIONS = CapabilityManager.get(new CapabilityToken<Invasions>(){});

	@SubscribeEvent
	public void registerCapabilities(RegisterCapabilitiesEvent event) {
		event.register(FollowQuest.class);
		event.register(RestrictBlock.class);
		event.register(Invasions.class);
	}

	@SubscribeEvent
	public void attachEntityCapabilities(AttachCapabilitiesEvent<Entity> event) {
		Entity entity = event.getObject();
		if (entity instanceof Mob) {
			event.addCapability(ModDefinitions.getResource("follow_quest"), new FollowQuest.Provider());
		}
		if (entity instanceof PathfinderMob) {
			event.addCapability(ModDefinitions.getResource("restrict_block"), new RestrictBlock.Provider());
		}
		if (entity instanceof Player) {
			event.addCapability(ModDefinitions.getResource("invasions"), new Invasions.Provider());
		}
	}

}
