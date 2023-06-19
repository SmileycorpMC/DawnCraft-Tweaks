package com.afunproject.dawncraft.dungeon.item;

import com.afunproject.dawncraft.CreativeTabs;
import com.afunproject.dawncraft.dungeon.KeyColour;
import com.afunproject.dawncraft.dungeon.block.entity.interfaces.Lockable;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.BaseComponent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;

import javax.annotation.Nullable;
import java.util.List;

public class LockItem extends Item {

	private final KeyColour colour;

	public LockItem(KeyColour colour) {
		super(new Properties().tab(CreativeTabs.DUNGEON_ITEMS));
		this.colour = colour;
	}

	public KeyColour getColour() {
		return colour;
	}

	@Override
	public Component getName(ItemStack stack) {
		BaseComponent component = ((BaseComponent)super.getName(stack));
		return component.withStyle(component.getStyle().withColor(colour.getColour()));
	}

	@Override
	public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> lines, TooltipFlag flag) {
		lines.add(new TranslatableComponent("tooltip.dawncraft.lock"));
	}

	@Override
	public InteractionResult useOn(UseOnContext ctx) {
		if (ctx.getPlayer().isCreative()) {
			Level level = ctx.getLevel();
			BlockPos pos = ctx.getClickedPos();
			BlockEntity blockentity = level.getBlockEntity(pos);
			if (blockentity instanceof Lockable) {
				((Lockable)blockentity).setLockColour(colour);
				level.playLocalSound(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, SoundEvents.CHAIN_BREAK, SoundSource.BLOCKS, 0.75f, 0, false);
				if (!ctx.getPlayer().isCreative()) ctx.getItemInHand().shrink(1);
				return InteractionResult.CONSUME;
			}
		}
		return super.useOn(ctx);
	}

}
