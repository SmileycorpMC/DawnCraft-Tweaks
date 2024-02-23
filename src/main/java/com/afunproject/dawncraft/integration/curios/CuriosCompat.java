package com.afunproject.dawncraft.integration.curios;

import com.google.common.collect.Lists;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.util.LazyOptional;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.type.capability.ICuriosItemHandler;
import top.theillusivec4.curios.api.type.inventory.IDynamicStackHandler;

import java.util.List;

public class CuriosCompat {
    
    public static List<ItemStack> getEquippedCurios(Player player) {
        List<ItemStack> items = Lists.newArrayList();
        LazyOptional<ICuriosItemHandler> optional = CuriosApi.getCuriosHelper().getCuriosHandler(player);
        if (optional.isPresent()) optional.orElseGet(null).getCurios().forEach((s, handler) -> {
            IDynamicStackHandler stacks = handler.getStacks();
            if (stacks == null) return;
            for (int i = 0; i < stacks.getSlots(); i++) items.add(stacks.getStackInSlot(i));
        });
        return items;
    }
    
}
