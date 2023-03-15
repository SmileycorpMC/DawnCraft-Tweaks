package com.afunproject.dawncraft.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.afunproject.dawncraft.integration.sophisticatedbackpacks.SophisticatedBackpacksCompat;

import net.minecraft.world.item.Item;
import net.minecraftforge.fml.ModList;

@Mixin(Item.class)
public abstract class MixinItem {

	@Inject(at=@At("HEAD"), method = "canFitInsideContainerItems()Z", cancellable = true)
	public void canFitInsideContainerItems(CallbackInfoReturnable<Boolean> callback) {
		if (ModList.get().isLoaded("sophisticatedbackpacks")) if (SophisticatedBackpacksCompat.isBackpack((Item)(Object)this)) {
			callback.setReturnValue(false);
			callback.cancel();
		}
	}
}
