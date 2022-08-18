package com.afunproject.afptweaks.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.afunproject.afptweaks.client.ClientHandler;
import com.afunproject.afptweaks.effects.AFPTweaksEffects;

import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;

@Mixin(KeyMapping.class)
public class MixinKeyMapping {

	@Inject(at=@At("HEAD"), method = "isDown()Z", cancellable = true)
	public void isDown(CallbackInfoReturnable<Boolean> callback) {
		Minecraft mc = Minecraft.getInstance();
		LocalPlayer player = mc.player;
		if (player != null) {
			if (player.hasEffect(AFPTweaksEffects.IMMOBILIZED.get())) {
				KeyMapping mapping = (KeyMapping)(Object)this;
				if (ClientHandler.IMMOBILIZED_KEYS.contains(mapping)) {
					callback.setReturnValue(false);
					callback.cancel();
				}
			}
		}
	}

	@Inject(at=@At("HEAD"), method = "consumeClick()Z", cancellable = true)
	public void consumeClick(CallbackInfoReturnable<Boolean> callback) {
		Minecraft mc = Minecraft.getInstance();
		LocalPlayer player = mc.player;
		if (player != null) {
			if (player.hasEffect(AFPTweaksEffects.IMMOBILIZED.get())) {
				KeyMapping mapping = (KeyMapping)(Object)this;
				if (ClientHandler.IMMOBILIZED_KEYS.contains(mapping)) {
					callback.setReturnValue(false);
					callback.cancel();
				}
			}
		}
	}

	@Inject(at=@At("HEAD"), method = "matches(II)Z", cancellable = true)
	public void matches(int key, int scancode, CallbackInfoReturnable<Boolean> callback) {
		Minecraft mc = Minecraft.getInstance();
		LocalPlayer player = mc.player;
		if (player != null) {
			if (player.hasEffect(AFPTweaksEffects.IMMOBILIZED.get())) {
				KeyMapping mapping = (KeyMapping)(Object)this;
				if (ClientHandler.IMMOBILIZED_KEYS.contains(mapping)) {
					callback.setReturnValue(false);
					callback.cancel();
				}
			}
		}
	}

	@Inject(at=@At("HEAD"), method = "matchesMouse(I)Z", cancellable = true)
	public void matchesMouse(int button, CallbackInfoReturnable<Boolean> callback) {
		Minecraft mc = Minecraft.getInstance();
		LocalPlayer player = mc.player;
		if (player != null) {
			if (player.hasEffect(AFPTweaksEffects.IMMOBILIZED.get())) {
				KeyMapping mapping = (KeyMapping)(Object)this;
				if (ClientHandler.IMMOBILIZED_KEYS.contains(mapping)) {
					callback.setReturnValue(false);
					callback.cancel();
				}
			}
		}
	}

}
