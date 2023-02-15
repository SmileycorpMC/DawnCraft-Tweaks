package com.afunproject.dawncraft.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.afunproject.dawncraft.client.ClientHandler;
import com.afunproject.dawncraft.effects.DawnCraftEffects;

import net.minecraft.client.Minecraft;
import net.minecraft.client.MouseHandler;
import net.minecraft.client.player.LocalPlayer;

@Mixin(MouseHandler.class)
public class MixinMouseHandler {

	@Inject(at=@At("HEAD"), method = "onPress(JIII)V", cancellable = true)
	private void onPress(long screen, int button, int action, int mods, CallbackInfo callback) {
		Minecraft mc = Minecraft.getInstance();
		if (mc.screen == null) {
			LocalPlayer player = mc.player;
			if (player != null) {
				if (player.hasEffect(DawnCraftEffects.IMMOBILIZED.get())) {
					if (ClientHandler.shouldImmobilize(button, true)) {
						callback.cancel();
						return;
					}
				}
				if (player.hasEffect(DawnCraftEffects.FROGFORM.get())) {
					if (ClientHandler.isUnusableByFrog(button, true)) {
						callback.cancel();
						return;
					}
				}
			}
		}
	}

}
