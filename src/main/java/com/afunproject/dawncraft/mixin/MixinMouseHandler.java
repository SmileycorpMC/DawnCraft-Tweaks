package com.afunproject.dawncraft.mixin;

import com.afunproject.dawncraft.client.ClientHandler;
import com.afunproject.dawncraft.effects.DawnCraftEffects;
import net.minecraft.client.Minecraft;
import net.minecraft.client.MouseHandler;
import net.minecraft.client.player.LocalPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MouseHandler.class)
public class MixinMouseHandler {

	@Inject(at=@At("HEAD"), method = "onPress(JIII)V", cancellable = true)
	private void onPress(long screen, int button, int action, int mods, CallbackInfo callback) {
		Minecraft mc = Minecraft.getInstance();
		if (mc.screen == null) {
			LocalPlayer player = mc.player;
			if (player != null) {
				if (player.hasEffect(DawnCraftEffects.IMMOBILIZED.get())) {
					callback.cancel();
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

	@Inject(at=@At("HEAD"), method = "onMove(JDD)V", cancellable = true)
	public void onMove(long p_91562_, double p_91563_, double p_91564_, CallbackInfo callback) {
		Minecraft mc = Minecraft.getInstance();
		LocalPlayer player = mc.player;
		if (player != null && mc.screen == null) {
			if (player.hasEffect(DawnCraftEffects.IMMOBILIZED.get())) {
				callback.cancel();
				return;
			}
		}

	}

}
