package com.afunproject.afptweaks.dungeon.item;

import java.util.List;

import javax.annotation.Nullable;

import com.afunproject.afptweaks.CreativeTabs;
import com.afunproject.afptweaks.ModUtils;
import com.afunproject.afptweaks.dungeon.block.entity.base.FunctionalBlockEntity;
import com.afunproject.afptweaks.dungeon.block.entity.base.SingleUse;
import com.afunproject.afptweaks.dungeon.block.entity.base.TriggerableBlockEntity;
import com.afunproject.afptweaks.network.AFPPacketHandler;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.BaseComponent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.network.NetworkDirection;
import net.smileycorp.atlas.api.network.SimpleStringMessage;

public class DungeonConfiguratorItem extends Item {

	public DungeonConfiguratorItem() {
		super(new Properties().tab(CreativeTabs.DUNGEON_ITEMS).stacksTo(1));
	}

	@Override
	public void fillItemCategory(CreativeModeTab tab, NonNullList<ItemStack> items) {
		if (this.allowdedIn(tab)) {
			ItemStack stack = new ItemStack(this);
			setMode(stack, ConfiguratorMode.LINK_FUNCTIONAL);
			items.add(stack);
		}
	}

	@Override
	public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> lines, TooltipFlag flag) {
		ConfiguratorMode mode = ConfiguratorMode.getmode(stack);
		lines.add(new TranslatableComponent(mode.getModeMessage()).withStyle(ChatFormatting.AQUA));
		lines.add(mode.getTooltip());
		lines.add(new TranslatableComponent("tooltip.afptweaks.configuratormode").withStyle(ChatFormatting.YELLOW));
	}

	@Override
	public Component getName(ItemStack stack) {
		return ((BaseComponent)super.getName(stack)).withStyle(ChatFormatting.LIGHT_PURPLE);
	}

	@Override
	public InteractionResult useOn(UseOnContext ctx) {
		Level level = ctx.getLevel();
		if (level.isClientSide) return super.useOn(ctx);
		BlockPos pos = ctx.getClickedPos();
		ItemStack stack = ctx.getItemInHand();
		BlockEntity block_entity = level.getBlockEntity(pos);
		Player player = ctx.getPlayer();
		if (player.isCrouching()) {
			ConfiguratorMode mode = shiftMode(stack);
			if (player instanceof ServerPlayer) {
				AFPPacketHandler.NETWORK_INSTANCE.sendTo(new SimpleStringMessage(mode.getModeMessage()), ((ServerPlayer) player).connection.connection, NetworkDirection.PLAY_TO_CLIENT);
			}
		} else {
			switch(ConfiguratorMode.getmode(stack)) {
			case LINK_FUNCTIONAL:
				if (block_entity instanceof FunctionalBlockEntity) {
					if (pos != null) {
						player.sendMessage(new TranslatableComponent("message.afptweaks.selectfunctionalblock", ModUtils.getPosString(pos)), null);
						setSelectedBlock(stack, pos);
					}
				}
				else if (block_entity instanceof TriggerableBlockEntity) {
					BlockPos linked_pos = getSelectedPos(stack);
					if (linked_pos != null) {
						player.sendMessage(new TranslatableComponent("message.afptweaks.linktriggerableblock", ModUtils.getPosString(linked_pos), ModUtils.getPosString(pos)), null);
						((TriggerableBlockEntity) block_entity).addLinkedBlock(level, linked_pos);
						setSelectedBlock(stack, null);
					}
				}
				break;
			case UNLINK_FUNCTIONAL:
				if (block_entity instanceof FunctionalBlockEntity) {
					if (pos != null) {
						player.sendMessage(new TranslatableComponent("message.afptweaks.selectfunctionalblock", ModUtils.getPosString(pos)), null);
						setSelectedBlock(stack, pos);
					}
				}
				else if (block_entity instanceof TriggerableBlockEntity) {
					BlockPos linked_pos = getSelectedPos(stack);
					if (linked_pos != null) {
						player.sendMessage(new TranslatableComponent("message.afptweaks.unlinktriggerableblock", ModUtils.getPosString(linked_pos), ModUtils.getPosString(pos)), null);
						((TriggerableBlockEntity) block_entity).removeLinkedBlock(linked_pos);
						setSelectedBlock(stack, null);
					}
				}
				break;
			case RESET_FUNCTIONAL:
				if (block_entity instanceof SingleUse) {
					if (((SingleUse)block_entity).hasBeenUsed()) {
						((SingleUse)block_entity).reset();
						player.sendMessage(new TranslatableComponent("message.afptweaks.resetfunctionalblock", ModUtils.getPosString(pos)), null);
					}
				}
			default: break;
			}
		}
		return super.useOn(ctx);
	}

	public ConfiguratorMode shiftMode(ItemStack stack) {
		ConfiguratorMode mode = ConfiguratorMode.getmode(stack);;
		ConfiguratorMode newMode = ConfiguratorMode.values()[(mode.ordinal() + 1) % ConfiguratorMode.values().length];
		setMode(stack, newMode);
		return newMode;
	}

	public void setMode(ItemStack stack, ConfiguratorMode mode) {
		CompoundTag tag = new CompoundTag();
		tag.putString("mode", mode.toString().toLowerCase());
		stack.setTag(tag);
	}

	private void setSelectedBlock(ItemStack stack, BlockPos pos) {
		CompoundTag tag = stack.getOrCreateTag();
		if (pos == null) {
			tag.remove("selected_pos");
		} else {
			CompoundTag pos_tag = new CompoundTag();
			pos_tag.putInt("x", pos.getX());
			pos_tag.putInt("y", pos.getY());
			pos_tag.putInt("z", pos.getZ());
			tag.put("linked_pos", pos_tag);
		}
	}

	private BlockPos getSelectedPos(ItemStack stack) {
		CompoundTag tag = stack.getOrCreateTag();
		if (tag.contains("selected_pos")) {
			CompoundTag pos = (CompoundTag) tag.get("selected_pos");
			return new BlockPos(pos.getInt("x"), pos.getInt("y"), pos.getInt("z"));
		}
		return null;
	}

	public static enum ConfiguratorMode {
		LINK_FUNCTIONAL,
		UNLINK_FUNCTIONAL,
		RESET_FUNCTIONAL;


		public String getModeMessage() {
			return "configuratormode.afptweaks." + toString().toLowerCase();
		}

		public TranslatableComponent getTooltip() {
			return new TranslatableComponent("tooltip.afptweaks.configuratormode." + toString().toLowerCase());
		}

		public static ConfiguratorMode getmode(ItemStack stack) {
			CompoundTag tag = stack.getOrCreateTag();
			if (tag.contains("mode")) {
				return ConfiguratorMode.valueOf(tag.getString("mode").toUpperCase());
			}
			return LINK_FUNCTIONAL;
		}
	}

}
