package com.afunproject.dawncraft.integration.simplemobs;

import com.afunproject.dawncraft.DCSubCommandsEvent;
import com.afunproject.dawncraft.capability.CapabilitiesRegister;
import com.afunproject.dawncraft.capability.Invasions;
import com.afunproject.dawncraft.integration.simplemobs.invasion.InvasionEntry;
import com.afunproject.dawncraft.integration.simplemobs.invasion.InvasionKey;
import com.afunproject.dawncraft.integration.simplemobs.invasion.InvasionRegistry;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

import net.mcreator.simplemobs.entity.Getsuga65Entity;
import net.mcreator.simplemobs.entity.Merlin1306Entity;
import net.mcreator.simplemobs.entity.ShadowMewYTEntity;
import net.mcreator.simplemobs.entity.SolarPixelEntity;
import net.mcreator.simplemobs.entity.UGoneEntity;
import net.mcreator.simplemobs.entity.WoodendayEntity;
import net.mcreator.simplemobs.init.SimpleMobsModItems;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.TickEvent.Phase;
import net.minecraftforge.event.TickEvent.PlayerTickEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.server.command.EnumArgument;
import yesman.epicfight.world.item.EpicFightItems;

public class SimpleMobsEvents {

	public static void init() {
		MinecraftForge.EVENT_BUS.register(new SimpleMobsEvents());
	}

	@SubscribeEvent
	public void playerTick(PlayerTickEvent event) {
		Player player = event.player;
		if (event.phase == Phase.END && player != null && !(player instanceof FakePlayer)) {
			if (!player.level.isClientSide) {
				LazyOptional<Invasions> optional = player.getCapability(CapabilitiesRegister.INVASIONS);
				if (optional.isPresent()) optional.resolve().get().tryToSpawnInvasion(player);
			}
		}
	}

	@SubscribeEvent
	public void entityJoinWorld(EntityJoinWorldEvent event) {
		if (event.getEntity() instanceof Getsuga65Entity) {
			Getsuga65Entity entity = (Getsuga65Entity) event.getEntity();
			entity.setCustomName(new TextComponent("Getsuga65"));
			entity.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation("hexerei", "warhammer"))));
			entity.setItemSlot(EquipmentSlot.CHEST, new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation("scattered_weapons", "hunter_armor_chestplate"))));
		}
		if (event.getEntity() instanceof WoodendayEntity) {
			WoodendayEntity entity = (WoodendayEntity) event.getEntity();
			entity.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(EpicFightItems.IRON_TACHI.get()));
			entity.setItemSlot(EquipmentSlot.CHEST, new ItemStack(SimpleMobsModItems.DIABOLIUM_CHESTPLATE.get()));
			entity.setItemSlot(EquipmentSlot.LEGS, new ItemStack(SimpleMobsModItems.DIABOLIUM_LEGGINGS.get()));
			entity.setCustomName(new TextComponent("Wooden_Day"));
		}
		if (event.getEntity() instanceof ShadowMewYTEntity) {
			ShadowMewYTEntity entity = (ShadowMewYTEntity) event.getEntity();
			entity.setCustomName(new TextComponent("ShadowMewYT"));
		}
		if (event.getEntity() instanceof SolarPixelEntity) {
			SolarPixelEntity entity = (SolarPixelEntity) event.getEntity();
			entity.setItemSlot(EquipmentSlot.CHEST, new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation("cataclysm", "ignitium_chestplate"))));
			entity.setCustomName(new TextComponent("SolarPixel"));
		}
		if (event.getEntity() instanceof Merlin1306Entity) {
			Merlin1306Entity entity = (Merlin1306Entity) event.getEntity();
			entity.setItemSlot(EquipmentSlot.CHEST, new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation("ob_aquamirae", "abyssal_chestplate"))));
			entity.setCustomName(new TextComponent("merlin1306"));
		}
		if (event.getEntity() instanceof UGoneEntity) {
			UGoneEntity entity = (UGoneEntity) event.getEntity();
			entity.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(EpicFightItems.IRON_DAGGER.get()));
			entity.setItemSlot(EquipmentSlot.CHEST, new ItemStack(EpicFightItems.STRAY_ROBE.get()));
			entity.setCustomName(new TextComponent("uGone"));
		}
	}

	@SubscribeEvent
	public void registerSubCommands(DCSubCommandsEvent event) {
		event.addSubCommand(Commands.literal("spawnInvader").then(Commands.argument("invader", EnumArgument.enumArgument(InvasionKey.class))
				.executes(ctx -> spawnInvasion(ctx))));
	}

	public static int spawnInvasion(CommandContext<CommandSourceStack> ctx) throws CommandSyntaxException {
		CommandSourceStack source = ctx.getSource();
		if (source.getEntity() instanceof Player) {
			InvasionEntry entry = InvasionRegistry.getInvasion(ctx.getArgument("invader", InvasionKey.class));
			entry.spawnEntities((Player) source.getEntity());
		}
		return 0;
	}

}
