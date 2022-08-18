package com.afunproject.afptweaks.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.afunproject.afptweaks.client.ClientHandler;
import com.afunproject.afptweaks.effects.AFPTweaksEffects;

import net.minecraft.client.KeyboardHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;

@Mixin(KeyboardHandler.class)
public class MixinKeyboardHandler {

	@Inject(at=@At("HEAD"), method = "keyPress(JIIII)V", cancellable = true)
	public void keyPress(long screen, int key, int scanCode, int action, int modifier, CallbackInfo callback) {
		Minecraft mc = Minecraft.getInstance();
		if (mc.screen == null) {
			LocalPlayer player = mc.player;
			if (player != null) {
				if (player.hasEffect(AFPTweaksEffects.IMMOBILIZED.get())) {
					if (ClientHandler.shouldImmobilize(key, false)) {
						callback.cancel();
					}
				}
			}
		}
	}

}
