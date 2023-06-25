package com.afunproject.dawncraft.dungeon.item;

import com.afunproject.dawncraft.CreativeTabs;
import com.afunproject.dawncraft.ModUtils;
import com.afunproject.dawncraft.dungeon.block.entity.interfaces.DungeonTrigger;
import com.afunproject.dawncraft.dungeon.block.entity.interfaces.Functional;
import com.afunproject.dawncraft.dungeon.block.entity.interfaces.SingleUse;
import com.afunproject.dawncraft.network.DCNetworkHandler;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.BaseComponent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.network.NetworkDirection;
import net.smileycorp.atlas.api.network.SimpleStringMessage;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Locale;

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
		lines.add(new TranslatableComponent("tooltip.dawncraft.configuratormode").withStyle(ChatFormatting.YELLOW));
	}

	@Override
	public Component getName(ItemStack stack) {
		return ((BaseComponent)super.getName(stack)).withStyle(ChatFormatting.LIGHT_PURPLE);
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
		if (level.isClientSide) return super.use(level, player, hand);
		if (player.isCreative()) {
			if (player.isCrouching()) {
				ConfiguratorMode mode = shiftMode(player.getItemInHand(hand));
				if (player instanceof ServerPlayer) {
					DCNetworkHandler.NETWORK_INSTANCE.sendTo(new SimpleStringMessage(mode.getModeMessage()), ((ServerPlayer) player).connection.connection, NetworkDirection.PLAY_TO_CLIENT);
				}
			}
		} else player.sendMessage(new TranslatableComponent("message.dawncraft.creative_required"), null);
		return super.use(level, player, hand);
	}

	@Override
	public InteractionResult useOn(UseOnContext ctx) {
		Level level = ctx.getLevel();
		if (level.isClientSide) return super.useOn(ctx);
		BlockPos pos = ctx.getClickedPos();
		ItemStack stack = ctx.getItemInHand();
		BlockEntity block_entity = level.getBlockEntity(pos);
		BlockState state = level.getBlockState(pos);
		Player player = ctx.getPlayer();
		if (player.isCreative()) {
			if (player.isCrouching()) {
				ConfiguratorMode mode = shiftMode(stack);
				if (player instanceof ServerPlayer) {
					DCNetworkHandler.NETWORK_INSTANCE.sendTo(new SimpleStringMessage(mode.getModeMessage()), ((ServerPlayer) player).connection.connection, NetworkDirection.PLAY_TO_CLIENT);
				}
			} else {
				ConfiguratorMode mode = ConfiguratorMode.getmode(stack);
				switch(mode) {
				case LINK_FUNCTIONAL:
					if (block_entity instanceof Functional) {
						if (pos != null) {
							player.sendMessage(new TranslatableComponent("message.dawncraft.select_functional_block", ModUtils.getPosString(pos)), null);
							setSelectedBlock(stack, pos);
						}
					}
					else if (block_entity instanceof DungeonTrigger) {
						BlockPos linked_pos = getSelectedPos(stack);
						if (linked_pos != null) {
							player.sendMessage(new TranslatableComponent("message.dawncraft.link_triggerable_block", ModUtils.getPosString(linked_pos), ModUtils.getPosString(pos)), null);
							((DungeonTrigger) block_entity).addLinkedBlock(level, linked_pos);
							setSelectedBlock(stack, null);
						}
					}
					break;
				case UNLINK_FUNCTIONAL:
					if (block_entity instanceof Functional) {
						if (pos != null) {
							player.sendMessage(new TranslatableComponent("message.dawncraft.select_functional_block", ModUtils.getPosString(pos)), null);
							setSelectedBlock(stack, pos);
						}
					}
					else if (block_entity instanceof DungeonTrigger) {
						BlockPos linked_pos = getSelectedPos(stack);
						if (linked_pos != null) {
							player.sendMessage(new TranslatableComponent("message.dawncraft.unlink_triggerable_block", ModUtils.getPosString(linked_pos), ModUtils.getPosString(pos)), null);
							((DungeonTrigger) block_entity).removeLinkedBlock(linked_pos);
							setSelectedBlock(stack, null);
						}
					}
					break;
				case RESET_FUNCTIONAL:
					if (block_entity instanceof SingleUse) {
						if (((SingleUse)block_entity).hasBeenUsed()) {
							((SingleUse)block_entity).reset();
							player.sendMessage(new TranslatableComponent("message.dawncraft.reset_functional_block", ModUtils.getPosString(pos)), null);
						}
					}
					break;
				case ROTATE:
					if (state != null) state.rotate(level, pos, Rotation.CLOCKWISE_90);
					break;
				default:
					player.sendMessage(new TranslatableComponent("message.dawncraft.no_mode", mode), null);
					break;
				}
			}
			return InteractionResult.SUCCESS;
		} else player.sendMessage(new TranslatableComponent("message.dawncraft.creative_required"), null);
		return super.useOn(ctx);
	}

	public ConfiguratorMode shiftMode(ItemStack stack) {
		ConfiguratorMode mode = ConfiguratorMode.getmode(stack);;
		ConfiguratorMode newMode = ConfiguratorMode.values()[(mode.ordinal() + 1) % ConfiguratorMode.values().length];
		setMode(stack, newMode);
		return newMode;
	}

	protected void setMode(ItemStack stack, ConfiguratorMode mode) {
		CompoundTag tag = new CompoundTag();
		tag.putString("mode", mode.toString().toLowerCase(Locale.US));
		stack.setTag(tag);
	}

	protected void setSelectedBlock(ItemStack stack, BlockPos pos) {
		CompoundTag tag = stack.getOrCreateTag();
		if (pos == null) {
			tag.remove("selected_pos");
		} else {
			tag.put("selected_pos", ModUtils.savePosToNBT(pos));
		}
	}

	public static BlockPos getSelectedPos(ItemStack stack) {
		CompoundTag tag = stack.getOrCreateTag();
		if (tag.contains("selected_pos")) {
			CompoundTag pos = (CompoundTag) tag.get("selected_pos");
			return (BlockPos) ModUtils.readPosFromNBT(pos, true);
		}
		return null;
	}

	public static enum ConfiguratorMode {
		LINK_FUNCTIONAL,
		UNLINK_FUNCTIONAL,
		RESET_FUNCTIONAL,
		ROTATE;

		public String getModeMessage() {
			return "configuratormode.dawncraft." + toString().toLowerCase(Locale.US);
		}

		public TranslatableComponent getTooltip() {
			return new TranslatableComponent("tooltip.dawncraft.configuratormode." + toString().toLowerCase(Locale.US));
		}

		public static ConfiguratorMode getmode(ItemStack stack) {
			CompoundTag tag = stack.getOrCreateTag();
			if (tag.contains("mode")) {
				return ConfiguratorMode.valueOf(tag.getString("mode").toUpperCase(Locale.US));
			}
			return LINK_FUNCTIONAL;
		}
	}

}
