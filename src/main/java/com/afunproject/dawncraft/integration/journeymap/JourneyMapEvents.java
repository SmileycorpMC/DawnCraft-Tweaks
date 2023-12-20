package com.afunproject.dawncraft.integration.journeymap;

import com.afunproject.dawncraft.event.DCSubCommandsEvent;
import com.afunproject.dawncraft.integration.journeymap.client.JourneyMapPlugin;
import com.afunproject.dawncraft.integration.journeymap.network.AddWaypointMessage;
import com.afunproject.dawncraft.network.DCNetworkHandler;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import com.mojang.datafixers.util.Pair;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.commands.arguments.ResourceOrTagLocationArgument;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.Registry;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.levelgen.feature.ConfiguredStructureFeature;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkEvent;
import net.smileycorp.atlas.api.network.NetworkUtils;

import java.util.function.Supplier;

public class JourneyMapEvents {

	public static void init() {
		MinecraftForge.EVENT_BUS.register(new JourneyMapEvents());
		NetworkUtils.registerMessage(DCNetworkHandler.NETWORK_INSTANCE, 5, AddWaypointMessage.class, JourneyMapEvents::receivePacket);
	}

	@SubscribeEvent
	public void registerSubCommands(DCSubCommandsEvent event) {
		event.addSubCommand(Commands.literal("addWaypoint").requires(stack -> stack.hasPermission(2))
				.then(Commands.argument("player", EntityArgument.players()).then(Commands.argument("structure", ResourceOrTagLocationArgument.m_210968_(Registry.CONFIGURED_STRUCTURE_FEATURE_REGISTRY))
						.then(Commands.argument("name", StringArgumentType.string()).executes(this::addWaypoint)))));
	}

	private int addWaypoint(CommandContext<CommandSourceStack> ctx) throws CommandSyntaxException {
		ResourceOrTagLocationArgument.Result<ConfiguredStructureFeature<?, ?>> result = ResourceOrTagLocationArgument.m_210970_(ctx, "structure");
		Entity entity = ctx.getSource().getEntity();
		if (entity == null) return 0;
		ServerLevel level = ctx.getSource().getLevel();
		Registry<ConfiguredStructureFeature<?, ?>> registry = level.registryAccess().registryOrThrow(Registry.CONFIGURED_STRUCTURE_FEATURE_REGISTRY);
		HolderSet<ConfiguredStructureFeature<?, ?>> holderset = result.m_207418_().map(rk ->
				registry.m_203636_(rk).map(HolderSet::m_205809_), registry::m_203431_)
				.orElseThrow(new SimpleCommandExceptionType(new TranslatableComponent("commands.locate.invalid", result.m_207276_()))::create);
		Pair<BlockPos, Holder<ConfiguredStructureFeature<?, ?>>> pair = level.getChunkSource().getGenerator()
				.m_207970_(level, holderset, new BlockPos(ctx.getSource().getPosition()), 100, false);
		if (pair == null) throw new SimpleCommandExceptionType(new TranslatableComponent("commands.locate.failed", result.m_207276_())).create();
		for (ServerPlayer player : EntityArgument.getPlayers(ctx, "player"))
			DCNetworkHandler.NETWORK_INSTANCE.sendTo(new AddWaypointMessage(pair.getFirst(), StringArgumentType.getString(ctx, "name")),
					player.connection.connection, NetworkDirection.PLAY_TO_CLIENT);
		return 1;
	}

	private static void receivePacket(AddWaypointMessage message, Supplier<NetworkEvent.Context> supplier) {
		supplier.get().enqueueWork(() -> DistExecutor.safeRunWhenOn(Dist.CLIENT, () ->
				() -> JourneyMapPlugin.getInstance().addWaypoint(message, true)));
	}

}
