package com.afunproject.dawncraft;

import com.afunproject.dawncraft.event.DCSubCommandsEvent;
import com.google.common.collect.Lists;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

import java.util.List;
import java.util.function.Function;

@EventBusSubscriber(modid=Constants.MODID)
public class DawnCraftCommand {

	public static List<Function<LiteralArgumentBuilder<CommandSourceStack>, LiteralArgumentBuilder<CommandSourceStack>>> subcommands = Lists.newArrayList();

	@SubscribeEvent
	public static void register(RegisterCommandsEvent event) {
		CommandDispatcher<CommandSourceStack> dispatcher = event.getDispatcher();
		registerCommand(dispatcher, Commands.literal("dawncraft"));
		registerCommand(dispatcher, Commands.literal("dc"));
	}

	private static void registerCommand(CommandDispatcher<CommandSourceStack> dispatcher, LiteralArgumentBuilder<CommandSourceStack> command) {
		DCSubCommandsEvent event = new DCSubCommandsEvent(command.requires(src -> src.hasPermission(1)));
		MinecraftForge.EVENT_BUS.post(event);
		dispatcher.register(event.getCommand());
	}

}
