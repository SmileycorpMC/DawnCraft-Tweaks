package com.afunproject.dawncraft.integration.quests;

import com.afunproject.dawncraft.DCSubCommandsEvent;
import com.afunproject.dawncraft.ModDefinitions;
import com.afunproject.dawncraft.integration.quests.custom.entity.QuestEntities;
import com.afunproject.dawncraft.integration.quests.custom.entity.QuestEntityBase;
import com.afunproject.dawncraft.integration.quests.network.QuestNetworkHandler;
import com.afunproject.dawncraft.integration.quests.task.AdvancementTask;
import com.feywild.quest_giver.quest.player.QuestData;
import com.feywild.quest_giver.quest.task.TaskTypes;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.coordinates.Vec3Argument;
import net.minecraft.commands.arguments.coordinates.WorldCoordinates;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.event.entity.player.AdvancementEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.server.command.EnumArgument;

public class QuestEvents {

	public static void init() {
		TaskTypes.register(ModDefinitions.getResource("advancement"), AdvancementTask.INSTANCE);
		MinecraftForge.EVENT_BUS.register(new QuestEvents());
		MinecraftForge.EVENT_BUS.register(QuestEvents.class);
		QuestEntities.ENTITIES.register(FMLJavaModLoadingContext.get().getModEventBus());
		QuestNetworkHandler.initPackets();
		if(ModList.get().isLoaded("followme")) MinecraftForge.EVENT_BUS.register(new FollowMeQuestEvents());
	}

	@SubscribeEvent
	public static void registerAttributes(EntityAttributeCreationEvent event) {
		event.put(QuestEntities.FALLEN.get(), QuestEntityBase.createAttributes());
		event.put(QuestEntities.QUEST_PLAYER.get(), QuestEntityBase.createAttributes());
	}

	@SubscribeEvent
	public void advancementTrigger(AdvancementEvent event) {
		QuestData quests = QuestData.get((ServerPlayer) event.getPlayer());
		quests.checkComplete(AdvancementTask.INSTANCE, event.getAdvancement());
	}

	@SubscribeEvent
	public void registerSubCommands(DCSubCommandsEvent event) {
		event.addSubCommand(Commands.literal("spawnNPC").then(Commands.argument("npc", EnumArgument.enumArgument(QuestNPC.class)).executes(ctx -> spawnNPCNoPos(ctx))
				.then(Commands.argument("pos", Vec3Argument.vec3()).executes(ctx -> spawnNPC(ctx)))));
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
