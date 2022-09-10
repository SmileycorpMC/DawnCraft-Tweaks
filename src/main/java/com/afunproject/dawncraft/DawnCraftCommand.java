package com.afunproject.dawncraft;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.smileycorp.hordes.common.Hordes;
import net.smileycorp.hordes.common.hordeevent.capability.IHordeEvent;

@EventBusSubscriber(modid=ModDefinitions.MODID)
public class DawnCraftCommand {

	@SubscribeEvent
	public static void register(RegisterCommandsEvent event) {
		CommandDispatcher<CommandSourceStack> dispatcher = event.getDispatcher();
		registerCommand(dispatcher, Commands.literal("dawncraft"));
		LiteralArgumentBuilder<CommandSourceStack> command = Commands.literal("dawncraft")
				.requires((commandSource) -> commandSource.hasPermission(1))
				.then(Commands.argument("count", IntegerArgumentType.integer())
						.executes(ctx -> execute(ctx, IntegerArgumentType.getInteger(ctx, "count"))));
		dispatcher.register(command);
	}

	private static void registerCommand(CommandDispatcher<CommandSourceStack> dispatcher, LiteralArgumentBuilder<CommandSourceStack> command) {
		command = command.requires((commandSource) -> commandSource.hasPermission(1));
		command = command.then(Commands.literal("spawnInvader")).then(Commands.argument("value", InvaderArgumentType)
				.executes(ctx -> execute(ctx, IntegerArgumentType.getInteger(ctx, "count"))));
		dispatcher.register(command);
	}

	public static int execute(CommandContext<CommandSourceStack> ctx, int count) throws CommandSyntaxException {
		CommandSourceStack source = ctx.getSource();
		if (source.getEntity() instanceof Player) {
			Player player = (Player) source.getEntity();
			LazyOptional<IHordeEvent> optional = player.getCapability(Hordes.HORDE_EVENT, null);
			if (optional.isPresent()) {
				optional.resolve().get().spawnWave(player, count);
				return 1;
			}
		}
		return 0;
	}

}
