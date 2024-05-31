package com.afunproject.dawncraft.mixin;

import com.afunproject.dawncraft.client.ClientHandler;
import com.afunproject.dawncraft.effects.DawnCraftEffects;
import net.minecraft.client.KeyboardHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(KeyboardHandler.class)
public class MixinKeyboardHandler {

	@Inject(at=@At("HEAD"), method = "keyPress(JIIII)V", cancellable = true)
	public void dctweaks$keyPress(long screen, int key, int scanCode, int action, int modifier, CallbackInfo callback) {
		Minecraft mc = Minecraft.getInstance();
		if (mc.screen != null) return;
		LocalPlayer player = mc.player;
		if (player != null) {
			if (player.hasEffect(DawnCraftEffects.IMMOBILIZED.get())) {
				callback.cancel();
				return;
			}
			if (player.hasEffect(DawnCraftEffects.FROGFORM.get()) && ClientHandler.isUnusableByFrog(key, false)) callback.cancel();
		}
	}

}
