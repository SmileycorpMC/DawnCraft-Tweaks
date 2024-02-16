package com.afunproject.dawncraft.integration.quests;

import com.afunproject.dawncraft.Constants;
import com.afunproject.dawncraft.EventListener;
import com.afunproject.dawncraft.event.DCSubCommandsEvent;
import com.afunproject.dawncraft.integration.quests.custom.DummyQuestEntity;
import com.afunproject.dawncraft.integration.quests.custom.QuestEntity;
import com.afunproject.dawncraft.integration.quests.custom.entity.QuestEntities;
import com.afunproject.dawncraft.integration.quests.custom.entity.QuestEntityBase;
import com.afunproject.dawncraft.integration.quests.network.QuestNetworkHandler;
import com.afunproject.dawncraft.integration.quests.task.AdvancementTask;
import com.feywild.quest_giver.quest.player.QuestData;
import com.feywild.quest_giver.quest.task.TaskTypes;
import com.mojang.brigadier.arguments.DoubleArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.coordinates.Vec3Argument;
import net.minecraft.commands.arguments.coordinates.WorldCoordinates;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.entity.EntityTypeTest;
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
		TaskTypes.register(Constants.loc("advancement"), AdvancementTask.INSTANCE);
		MinecraftForge.EVENT_BUS.register(new QuestEvents());
		QuestEntities.ENTITIES.register(FMLJavaModLoadingContext.get().getModEventBus());
		EventListener.ATTRIBUTE_SUPPLIERS.add(QuestEvents::registerAttributes);
		QuestNetworkHandler.initPackets();
		if(ModList.get().isLoaded("followme")) MinecraftForge.EVENT_BUS.register(new FollowMeQuestEvents());
	}

	public static void registerAttributes(EntityAttributeCreationEvent event) {
		event.put(QuestEntities.FALLEN_ADVENTURER.get(), QuestEntityBase.createDefaultAttributes());
		event.put(QuestEntities.QUEST_PLAYER.get(), QuestEntityBase.createDefaultAttributes());
	}

	@SubscribeEvent
	public void advancementTrigger(AdvancementEvent event) {
		QuestData quests = QuestData.get((ServerPlayer) event.getPlayer());
		quests.checkComplete(AdvancementTask.INSTANCE, event.getAdvancement());
	}

	@SubscribeEvent
	public void registerSubCommands(DCSubCommandsEvent event) {
		event.addSubCommand(Commands.literal("spawnNPC").then(Commands.argument("npc", EnumArgument.enumArgument(QuestNPC.class))
				.executes(QuestEvents::spawnNPCNoPos)
				.then(Commands.argument("pos", Vec3Argument.vec3()).executes(QuestEvents::spawnNPC))));
		event.addSubCommand(Commands.literal("removeNPCs").then(Commands.argument("range", DoubleArgumentType.doubleArg(0, 255))
				.executes(QuestEvents::removeNPCs)));
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
	
	private static int removeNPCs(CommandContext<CommandSourceStack> ctx) throws CommandSyntaxException {
		Vec3 pos = ctx.getSource().getPosition();
		ServerLevel level = ctx.getSource().getLevel();
		double r = DoubleArgumentType.getDouble(ctx, "range");
		level.getEntities(EntityTypeTest.forClass(Entity.class), e -> !(QuestEntity.safeCast(e) instanceof DummyQuestEntity) && e.distanceToSqr(pos) < r * r)
				.forEach(e -> e.setRemoved(Entity.RemovalReason.DISCARDED));
		return 0;
	}

}
