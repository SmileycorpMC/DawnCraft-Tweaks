package com.afunproject.dawncraft.dungeon.item;

import com.afunproject.dawncraft.CreativeTabs;
import com.afunproject.dawncraft.dungeon.block.LockedBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockBehaviour.BlockStateBase;

import javax.annotation.Nullable;
import java.util.List;

public class SkeletonKeyItem extends Item implements AdventureItem {

	public SkeletonKeyItem() {
		super(new Properties().tab(CreativeTabs.DUNGEON_ITEMS).stacksTo(1).rarity(Rarity.EPIC));
	}

	@Override
	public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> lines, TooltipFlag flag) {
		lines.add(new TranslatableComponent("tooltip.dawncraft.skeleton_key_0"));
		lines.add(new TranslatableComponent("tooltip.dawncraft.skeleton_key_1"));
	}

	@Override
	public InteractionResult useOn(UseOnContext ctx) {
		Level level = ctx.getLevel();
		BlockPos pos = ctx.getClickedPos();
		BlockStateBase state = level.getBlockState(pos);
		if (state.getBlock() instanceof LockedBlock) {
			LockedBlock block = (LockedBlock) state.getBlock();
			if (block.open(level, pos, state, true)) {
				level.playLocalSound(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, SoundEvents.CHAIN_BREAK, SoundSource.BLOCKS, 0.75f, 0, false);
				if (!ctx.getPlayer().isCreative()) ctx.getItemInHand().shrink(1);
				return InteractionResult.SUCCESS;
			}
		}
		return super.useOn(ctx);
	}

}
