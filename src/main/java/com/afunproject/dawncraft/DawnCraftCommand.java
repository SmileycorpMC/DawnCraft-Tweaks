package com.afunproject.dawncraft;

import com.afunproject.dawncraft.invasion.InvasionEntry;
import com.afunproject.dawncraft.invasion.InvasionRegistry;
import com.afunproject.dawncraft.invasion.InvasionKey;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.server.command.EnumArgument;

@EventBusSubscriber(modid=ModDefinitions.MODID)
public class DawnCraftCommand {

	@SubscribeEvent
	public static void register(RegisterCommandsEvent event) {
		CommandDispatcher<CommandSourceStack> dispatcher = event.getDispatcher();
		registerCommand(dispatcher, Commands.literal("dawncraft"));
		registerCommand(dispatcher, Commands.literal("dc"));
	}

	private static void registerCommand(CommandDispatcher<CommandSourceStack> dispatcher, LiteralArgumentBuilder<CommandSourceStack> command) {
		command = command.requires((commandSource) -> commandSource.hasPermission(1));
		command = command.then(Commands.literal("spawnInvader").then(Commands.argument("invader", EnumArgument.enumArgument(InvasionKey.class))
				.executes(ctx -> executeInvasion(ctx))));
		dispatcher.register(command);
	}

	public static int executeInvasion(CommandContext<CommandSourceStack> ctx) throws CommandSyntaxException {
		CommandSourceStack source = ctx.getSource();
		if (source.getEntity() instanceof Player) {
			InvasionEntry entry = InvasionRegistry.getInvasion(ctx.getArgument("invader", InvasionKey.class));
			entry.spawnEntities((Player) source.getEntity());
		}
		return 0;
	}

}
