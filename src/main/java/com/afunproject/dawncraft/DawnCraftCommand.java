package com.afunproject.dawncraft;

import com.afunproject.dawncraft.invasion.InvasionEntry;
import com.afunproject.dawncraft.invasion.InvasionKey;
import com.afunproject.dawncraft.invasion.InvasionRegistry;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.coordinates.Vec3Argument;
import net.minecraft.commands.arguments.coordinates.WorldCoordinates;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
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
				.executes(ctx -> spawnInvasion(ctx))));
		command = command.then(Commands.literal("spawnNPC").then(Commands.argument("npc", EnumArgument.enumArgument(QuestNPC.class)).executes(ctx -> spawnNPCNoPos(ctx))
				.then(Commands.argument("pos", Vec3Argument.vec3()).executes(ctx -> spawnNPC(ctx)))));
		dispatcher.register(command);
	}

	public static int spawnInvasion(CommandContext<CommandSourceStack> ctx) throws CommandSyntaxException {
		CommandSourceStack source = ctx.getSource();
		if (source.getEntity() instanceof Player) {
			InvasionEntry entry = InvasionRegistry.getInvasion(ctx.getArgument("invader", InvasionKey.class));
			entry.spawnEntities((Player) source.getEntity());
		}
		return 0;
	}

	public static int spawnNPC(CommandContext<CommandSourceStack> ctx) throws CommandSyntaxException {
		WorldCoordinates pos = ctx.getArgument("pos", WorldCoordinates.class);
		spawnNPC(ctx, pos.getPosition(ctx.getSource()));
		return 0;
	}

	public static int spawnNPCNoPos(CommandContext<CommandSourceStack> ctx) throws CommandSyntaxException {
		spawnNPC(ctx, ctx.getSource().getPosition());
		return 0;
	}

	private static void spawnNPC(CommandContext<CommandSourceStack> ctx, Vec3 pos) {
		CommandSourceStack source = ctx.getSource();
		QuestNPC npc = ctx.getArgument("npc", QuestNPC.class);
		npc.spawnEntity(source.getLevel(), pos);
		source.sendSuccess(new TranslatableComponent("message.dawncraft.spawn_npc", npc, pos), false);
	}

}
