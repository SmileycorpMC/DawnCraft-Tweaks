package com.afunproject.dawncraft.integration.dcmobs;

import com.afunproject.dawncraft.capability.DCCapabilities;
import com.afunproject.dawncraft.capability.Invasion;
import com.afunproject.dawncraft.capability.SpawnTracker;
import com.afunproject.dawncraft.event.DCSubCommandsEvent;
import com.afunproject.dawncraft.invasion.InvasionEntry;
import com.afunproject.dawncraft.invasion.InvasionKey;
import com.afunproject.dawncraft.invasion.InvasionRegistry;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.mcreator.simplemobs.entity.*;
import net.mcreator.simplemobs.init.SimpleMobsModEntities;
import net.mcreator.simplemobs.init.SimpleMobsModItems;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerEvent.PlayerLoggedInEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.server.command.EnumArgument;
import net.smileycorp.atlas.api.util.DirectionUtils;
import yesman.epicfight.world.item.EpicFightItems;

public class SimpleMobsEvents {

	public static void init() {
		MinecraftForge.EVENT_BUS.register(new SimpleMobsEvents());
	}

	public static void registerInvasions() {
		InvasionRegistry.register(new InvasionEntry("Getsuga65", SimpleMobsModEntities.GETSUGA_65.get()));
		InvasionRegistry.register(new InvasionEntry("Wooden_Day", SimpleMobsModEntities.WOODENDAY.get()));
		InvasionRegistry.register(new InvasionEntry("ShadowMewYT", SimpleMobsModEntities.SHADOW_MEW_YT.get()));
		InvasionRegistry.register(new InvasionEntry("SolarPixel", SimpleMobsModEntities.SOLAR_PIXEL.get()));
		InvasionRegistry.register(new InvasionEntry("merlin1306", SimpleMobsModEntities.MERLIN_1306.get()));
		InvasionRegistry.register(new InvasionEntry("uGone", SimpleMobsModEntities.U_GONE.get()));
		InvasionRegistry.register(new InvasionEntry("MonikaSunrise", SimpleMobsModEntities.MONIKA_SUNRISE.get()));
		InvasionRegistry.register(new InvasionEntry("YesImDavid", SimpleMobsModEntities.YES_IM_DAVID.get()));
		InvasionRegistry.register(new InvasionEntry("LiverCirrhosi", SimpleMobsModEntities.LIVER_CIRRHOSIS.get()));
		InvasionRegistry.register(new InvasionEntry("Jungharam", SimpleMobsModEntities.JUNGHARAM.get()));
		InvasionRegistry.register(new InvasionEntry("ManuelPoky", SimpleMobsModEntities.MANUEL_POKY.get()));
		InvasionRegistry.register(new InvasionEntry("PPX", SimpleMobsModEntities.PPX.get()));
		InvasionRegistry.register(new InvasionEntry("Lorde_RK", SimpleMobsModEntities.LORDE_RK.get()));
		InvasionRegistry.register(new InvasionEntry("Ibr17eem171", SimpleMobsModEntities.IBR_17EEM_171.get()));
		InvasionRegistry.addReward(2, new ItemStack(SimpleMobsModItems.TORN_NOTE.get()));
	}

	@SubscribeEvent
	public void playerJoinWorld(PlayerLoggedInEvent event) {
		if (event.getPlayer() instanceof ServerPlayer) {
			Player player = event.getPlayer();
			LazyOptional<SpawnTracker> optional = player.getCapability(DCCapabilities.SPAWN_TRACKER);
			if (optional.isPresent()) {
				SpawnTracker spawnTracker = optional.resolve().get();
				if (!spawnTracker.hasSpawned()) {
					BlockPos pos = DirectionUtils.getClosestLoadedPos(player.level, player.blockPosition(), player.getLookAngle(), 5);
					SimpleMobsModEntities.KOROK_INTRO.get().spawn((ServerLevel)player.getLevel(), null, player, pos, MobSpawnType.MOB_SUMMONED, false, false);
					spawnTracker.setSpawned();
				}
			}
		}

	}

	@SubscribeEvent
	public void playerClone(PlayerEvent.Clone event) {
		Player original = event.getOriginal();
		Player player = event.getPlayer();
		original.reviveCaps();
		LazyOptional<SpawnTracker> optionalOld = original.getCapability(DCCapabilities.SPAWN_TRACKER);
		LazyOptional<SpawnTracker> optional = player.getCapability(DCCapabilities.SPAWN_TRACKER);
		if (optionalOld.isPresent() && optional.isPresent()) if (optionalOld.resolve().get().hasSpawned()) optional.resolve().get().setSpawned();
	}

	@SubscribeEvent
	public void entityJoinWorld(EntityJoinWorldEvent event) {
		if (event.getEntity() instanceof Getsuga65Entity) {
			Getsuga65Entity entity = (Getsuga65Entity) event.getEntity();
			entity.setCustomName(new TextComponent("Getsuga65"));
			entity.setCustomNameVisible(true);
			entity.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation("hexerei", "warhammer"))));
			entity.setItemSlot(EquipmentSlot.CHEST, new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation("scattered_weapons", "hunter_armor_chestplate"))));
		}
		if (event.getEntity() instanceof WoodendayEntity) {
			WoodendayEntity entity = (WoodendayEntity) event.getEntity();
			entity.setCustomNameVisible(true);
			entity.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(EpicFightItems.IRON_TACHI.get()));
			entity.setItemSlot(EquipmentSlot.CHEST, new ItemStack(SimpleMobsModItems.DIABOLIUM_CHESTPLATE.get()));
			entity.setItemSlot(EquipmentSlot.LEGS, new ItemStack(SimpleMobsModItems.DIABOLIUM_LEGGINGS.get()));
			entity.setCustomName(new TextComponent("Wooden_Day"));
		}
		if (event.getEntity() instanceof ShadowMewYTEntity) {
			ShadowMewYTEntity entity = (ShadowMewYTEntity) event.getEntity();
			entity.setCustomNameVisible(true);
			entity.setCustomName(new TextComponent("ShadowMewYT"));
		}
		if (event.getEntity() instanceof SolarPixelEntity) {
			SolarPixelEntity entity = (SolarPixelEntity) event.getEntity();
			entity.setCustomNameVisible(true);
			entity.setItemSlot(EquipmentSlot.CHEST, new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation("cataclysm", "ignitium_chestplate"))));
			entity.setCustomName(new TextComponent("SolarPixel"));
		}
		if (event.getEntity() instanceof Merlin1306Entity) {
			Merlin1306Entity entity = (Merlin1306Entity) event.getEntity();
			entity.setCustomNameVisible(true);
			entity.setItemSlot(EquipmentSlot.CHEST, new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation("ob_aquamirae", "abyssal_chestplate"))));
			entity.setCustomName(new TextComponent("merlin1306"));
		}
		if (event.getEntity() instanceof UGoneEntity) {
			UGoneEntity entity = (UGoneEntity) event.getEntity();
			entity.setCustomNameVisible(true);
			entity.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(EpicFightItems.IRON_DAGGER.get()));
			entity.setItemSlot(EquipmentSlot.CHEST, new ItemStack(EpicFightItems.STRAY_ROBE.get()));
			entity.setCustomName(new TextComponent("uGone"));
		}
		if (event.getEntity() instanceof MonikaSunriseEntity) {
			MonikaSunriseEntity entity = (MonikaSunriseEntity) event.getEntity();
			entity.setCustomNameVisible(true);
			entity.setCustomName(new TextComponent("MonikaSunrise"));
		}
		if (event.getEntity() instanceof YesImDavidEntity) {
			YesImDavidEntity entity = (YesImDavidEntity) event.getEntity();
			entity.setCustomNameVisible(true);
			entity.setItemSlot(EquipmentSlot.CHEST, new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation("ob_core", "paladin_chestplate"))));
			entity.setCustomName(new TextComponent("YesImDavid"));
		}
		if (event.getEntity() instanceof LiverCirrhosisEntity) {
			LiverCirrhosisEntity entity = (LiverCirrhosisEntity) event.getEntity();
			entity.setCustomNameVisible(true);
			entity.setItemSlot(EquipmentSlot.CHEST, new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation("nourished_end", "voidsteel_armor_chestplate"))));
			entity.setItemSlot(EquipmentSlot.LEGS, new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation("nourished_end", "voidsteel_armor_leggings"))));
			entity.setCustomName(new TextComponent("LiverCirrhosis"));
		}
		if (event.getEntity() instanceof ManuelPokyEntity) {
			ManuelPokyEntity entity = (ManuelPokyEntity) event.getEntity();
			entity.setCustomNameVisible(true);
			entity.setItemSlot(EquipmentSlot.CHEST, new ItemStack(Items.IRON_CHESTPLATE));
			entity.setCustomName(new TextComponent("ManuelPoky"));
		}
		if (event.getEntity() instanceof JungharamEntity) {
			JungharamEntity entity = (JungharamEntity) event.getEntity();
			entity.setCustomNameVisible(true);
			entity.setItemSlot(EquipmentSlot.CHEST, new ItemStack(Items.IRON_CHESTPLATE));
			entity.setCustomName(new TextComponent("Jungharam"));
		}
		if (event.getEntity() instanceof PPXEntity) {
			PPXEntity entity = (PPXEntity) event.getEntity();
			entity.setCustomNameVisible(true);
			entity.setCustomName(new TextComponent("PPX"));
		}
	}

	@SubscribeEvent
	public void registerSubCommands(DCSubCommandsEvent event) {
		event.addSubCommand(Commands.literal("spawnInvader").then(Commands.argument("invader", EnumArgument.enumArgument(InvasionKey.class))
				.executes(ctx -> spawnInvasion(ctx))));
		event.addSubCommand(Commands.literal("spawnInvader").then(Commands.argument("invader", EnumArgument.enumArgument(InvasionKey.class)).then(Commands.argument("player", EntityArgument.player())
				.executes(ctx -> spawnInvasion(ctx, EntityArgument.getPlayer(ctx, "player"))))));
		event.addSubCommand(Commands.literal("enableInvasions").then(Commands.argument("player", EntityArgument.player())
				.executes(ctx -> enableInvasions(ctx))));
	}

	public static int spawnInvasion(CommandContext<CommandSourceStack> ctx) throws CommandSyntaxException {
		CommandSourceStack source = ctx.getSource();
		if (source.getEntity() instanceof Player) {
			return spawnInvasion(ctx, (Player) source.getEntity());
		}
		return 0;
	}

	public static int spawnInvasion(CommandContext<CommandSourceStack> ctx, Player player) throws CommandSyntaxException {
		InvasionEntry entry = InvasionRegistry.getInvasion(ctx.getArgument("invader", InvasionKey.class));
		entry.spawnEntities(player);
		return 0;
	}

	public static int enableInvasions(CommandContext<CommandSourceStack> ctx) throws CommandSyntaxException {
		Player player = EntityArgument.getPlayer(ctx, "player");
		LazyOptional<Invasion> optional = player.getCapability(DCCapabilities.INVASIONS);
		if (optional.isPresent()) {
			int time = player.getRandom().nextInt(30000) + 6000;
			optional.resolve().get().setNextSpawn(time);
			player.level.playSound(null, player.position().x, player.position().y, player.position().z, SoundEvents.ENDER_DRAGON_AMBIENT, SoundSource.HOSTILE, 1f, player.level.random.nextFloat());
			player.sendMessage(new TranslatableComponent("message.dawncraft.invasions_enabled_0").withStyle(ChatFormatting.RED), null);
		}
		return 0;
	}


}
