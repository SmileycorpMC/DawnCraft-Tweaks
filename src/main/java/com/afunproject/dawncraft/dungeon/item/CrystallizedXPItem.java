package com.afunproject.dawncraft.dungeon.item;

import com.afunproject.dawncraft.CreativeTabs;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.List;

public class CrystallizedXPItem extends Item {
    
    public CrystallizedXPItem() {
        super(new Properties().tab(CreativeTabs.DUNGEON_ITEMS).rarity(Rarity.UNCOMMON));
    }
    
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        if (player == null) return InteractionResultHolder.pass(ItemStack.EMPTY);
        ItemStack stack = player.getItemInHand(hand);
        int value = getValue(stack);
        if (value <=0 ) return InteractionResultHolder.pass(stack);
        if (level.isClientSide()) player.playSound(SoundEvents.EXPERIENCE_ORB_PICKUP, 0.5f, 1f);
        else {
            int count = player.isCrouching() ? 1 : stack.getCount();
            player.giveExperiencePoints(value * count);
            stack.shrink(count);
        }
        return InteractionResultHolder.consume(stack);
    }
    
    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> lines, TooltipFlag flag) {
        int value = getValue(stack);
        if (value <= 0) return;
        lines.add(new TranslatableComponent("tooltip.dawncraft.crystallized_xp_0", value).withStyle(Style.EMPTY.withColor(TextColor.fromRgb(0xAAED52))));
        lines.add(new TranslatableComponent("tooltip.dawncraft.crystallized_xp_1"));
    }
    
    @Override
    public void fillItemCategory(CreativeModeTab tab, NonNullList<ItemStack> items) {
        if (allowdedIn(tab)) {
            items.add(withValue(1));
            items.add(withValue(5));
            items.add(withValue(10));
            items.add(withValue(50));
            items.add(withValue(100));
            items.add(withValue(500));
            items.add(withValue(1000));
        }
    }
    
    public static int getValue(ItemStack stack) {
        if (!stack.hasTag()) return 0;
        CompoundTag tag = stack.getTag();
        return tag.contains("value") ? tag.getInt("value") : 0;
    }
    
    public static ItemStack withValue(int value) {
        if (value <= 0) return ItemStack.EMPTY;
        ItemStack stack = new ItemStack(DungeonItems.CRYSTALLIZED_XP.get());
        CompoundTag tag = stack.getOrCreateTag();
        tag.putInt("value", value);
        return stack;
    }
    
}
