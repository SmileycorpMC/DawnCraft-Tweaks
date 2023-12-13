package com.afunproject.dawncraft.integration.journeymap;

import com.afunproject.dawncraft.capability.CapabilitiesRegister;
import com.afunproject.dawncraft.capability.Invasions;
import com.afunproject.dawncraft.capability.SpawnTracker;
import com.afunproject.dawncraft.client.ClientHandler;
import com.afunproject.dawncraft.event.DCSubCommandsEvent;
import com.afunproject.dawncraft.integration.journeymap.client.JourneyMapPlugin;
import com.afunproject.dawncraft.integration.journeymap.network.AddWaypointMessage;
import com.afunproject.dawncraft.invasion.InvasionEntry;
import com.afunproject.dawncraft.invasion.InvasionKey;
import com.afunproject.dawncraft.invasion.InvasionRegistry;
import com.afunproject.dawncraft.network.DCNetworkHandler;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandExceptionType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import com.mojang.datafixers.util.Either;
import com.mojang.datafixers.util.Pair;
import net.mcreator.simplemobs.entity.*;
import net.mcreator.simplemobs.init.SimpleMobsModEntities;
import net.mcreator.simplemobs.init.SimpleMobsModItems;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.commands.arguments.ResourceOrTagLocationArgument;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.Registry;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.commands.LocateCommand;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.levelgen.feature.ConfiguredStructureFeature;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerEvent.PlayerLoggedInEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.server.command.EnumArgument;
import net.smileycorp.atlas.api.network.NetworkUtils;
import net.smileycorp.atlas.api.network.SimpleAbstractMessage;
import net.smileycorp.atlas.api.network.SimpleIntMessage;
import net.smileycorp.atlas.api.util.DirectionUtils;
import yesman.epicfight.world.item.EpicFightItems;

import java.util.function.Supplier;

public class JourneyMapEvents {

	public static void init() {
		MinecraftForge.EVENT_BUS.register(new JourneyMapEvents());
		NetworkUtils.registerMessage(DCNetworkHandler.NETWORK_INSTANCE, 12, AddWaypointMessage.class, JourneyMapEvents::recievePacket);
	}

	@SubscribeEvent
	public void registerSubCommands(DCSubCommandsEvent event) {
		event.addSubCommand(Commands.literal("addWaypoint").requires(stack -> stack.hasPermission(2))
				.then(Commands.argument("player", EntityArgument.players()))
				.then(Commands.argument("structure", ResourceOrTagLocationArgument.m_210968_(Registry.CONFIGURED_STRUCTURE_FEATURE_REGISTRY))
				.executes(this::addWaypoint)));
	}

	private int addWaypoint(CommandContext<CommandSourceStack> ctx) throws CommandSyntaxException {
		ResourceOrTagLocationArgument.Result<ConfiguredStructureFeature<?, ?>> result = ResourceOrTagLocationArgument.m_210970_(ctx, "structure");
		Either<ResourceKey<ConfiguredStructureFeature<?, ?>>, TagKey<ConfiguredStructureFeature<?, ?>>> structure = result.m_207418_();
		Entity entity = ctx.getSource().getEntity();
		if (entity == null) return 0;
		ServerLevel level = ctx.getSource().getLevel();
		Registry<ConfiguredStructureFeature<?, ?>> registry = level.registryAccess().registryOrThrow(Registry.CONFIGURED_STRUCTURE_FEATURE_REGISTRY);
		HolderSet<ConfiguredStructureFeature<?, ?>> holderset = structure.map(p_207532_ ->
				registry.m_203636_(p_207532_).map(HolderSet::m_205809_), registry::m_203431_)
				.orElseThrow(() -> new SimpleCommandExceptionType(new TranslatableComponent("commands.locate.invalid", result.m_207276_())).create());
		Pair<BlockPos, Holder<ConfiguredStructureFeature<?, ?>>> pair = level.getChunkSource().getGenerator()
				.m_207970_(level, holderset, new BlockPos(ctx.getSource().getPosition()), 100, false);
		if (pair == null) throw new SimpleCommandExceptionType(new TranslatableComponent("commands.locate.failed", result.m_207276_())).create();
		BlockPos pos = pair.getFirst();
		String name = structure.map(rk -> rk.toString(), rk -> rk.toString());
		for (ServerPlayer player : EntityArgument.getPlayers(ctx, "player"))
			DCNetworkHandler.NETWORK_INSTANCE.sendTo(new AddWaypointMessage(pos, name),
					player.connection.connection, NetworkDirection.PLAY_TO_CLIENT);
		return 1;
	}

	private static void recievePacket(AddWaypointMessage message, Supplier<NetworkEvent.Context> supplier) {
		supplier.get().enqueueWork(() -> DistExecutor.safeRunWhenOn(Dist.CLIENT, () ->
				() -> JourneyMapPlugin.getInstance().addWaypoint(message)));
	}

}
