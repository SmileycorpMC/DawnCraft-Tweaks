package com.afunproject.dawncraft.mixin;

import com.afunproject.dawncraft.capability.DCCapabilities;
import com.afunproject.dawncraft.capability.Toasts;
import com.afunproject.dawncraft.integration.epicfight.EpicFightCompat;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fml.ModList;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Inventory.class)
public class MixinInventory {

    @Shadow @Final public Player player;

    @Inject(at=@At("HEAD"), method = "add(ILnet/minecraft/world/item/ItemStack;)Z", cancellable = true)
    public void dctweaks$add(int slot, ItemStack stack, CallbackInfoReturnable<Boolean> callback) {
       if (player instanceof ServerPlayer && ModList.get().isLoaded("epicfight") && EpicFightCompat.isSkillBook(stack)) {
           LazyOptional<Toasts> cap = player.getCapability(DCCapabilities.TOASTS);
           if (cap.isPresent()) cap.orElseGet(null).sendToast((ServerPlayer) player, (byte) 4);
       }
    }

    @Inject(at=@At("HEAD"), method = "setItem", cancellable = true)
    public void dctweaks$setItem(int slot, ItemStack stack, CallbackInfo callback) {
        if (player instanceof ServerPlayer && ModList.get().isLoaded("epicfight") && EpicFightCompat.isSkillBook(stack)) {
            LazyOptional<Toasts> cap = player.getCapability(DCCapabilities.TOASTS);
            if (cap.isPresent()) cap.orElseGet(null).sendToast((ServerPlayer) player, (byte) 4);
        }
    }

}
