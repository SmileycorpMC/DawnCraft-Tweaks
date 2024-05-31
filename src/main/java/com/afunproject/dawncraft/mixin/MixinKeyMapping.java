package com.afunproject.dawncraft.mixin;

import com.afunproject.dawncraft.client.ClientHandler;
import com.afunproject.dawncraft.effects.DawnCraftEffects;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(KeyMapping.class)
public class MixinKeyMapping {

	@Shadow
	boolean isDown;

	@Inject(at=@At("HEAD"), method = "isDown()Z", cancellable = true)
	public void dctweaks$isDown(CallbackInfoReturnable<Boolean> callback) {
		Minecraft mc = Minecraft.getInstance();
		LocalPlayer player = mc.player;
		if (player != null) {
			if (player.hasEffect(DawnCraftEffects.IMMOBILIZED.get())) {
				callback.setReturnValue(false);
				if (isDown) isDown = false;
				return;
			}
			if (player.hasEffect(DawnCraftEffects.FROGFORM.get())) {
				KeyMapping mapping = (KeyMapping)(Object)this;
				if (ClientHandler.FROG_KEYS.contains(mapping)) {
					callback.setReturnValue(false);
					if (isDown) isDown = false;
				}
			}
		}
	}

	@Inject(at=@At("HEAD"), method = "consumeClick()Z", cancellable = true)
	public void dctweaks$consumeClick(CallbackInfoReturnable<Boolean> callback) {
		Minecraft mc = Minecraft.getInstance();
		LocalPlayer player = mc.player;
		if (player != null) {
			if (player.hasEffect(DawnCraftEffects.IMMOBILIZED.get())) {
				callback.setReturnValue(false);
				return;
			}
			if (player.hasEffect(DawnCraftEffects.FROGFORM.get())) {
				KeyMapping mapping = (KeyMapping)(Object)this;
				if (ClientHandler.FROG_KEYS.contains(mapping)) callback.setReturnValue(false);
			}
		}
	}

	@Inject(at=@At("HEAD"), method = "matches(II)Z", cancellable = true)
	public void dctweaks$matches(int key, int scancode, CallbackInfoReturnable<Boolean> callback) {
		Minecraft mc = Minecraft.getInstance();
		LocalPlayer player = mc.player;
		if (player != null) {
			if (player.hasEffect(DawnCraftEffects.IMMOBILIZED.get())) {
				callback.setReturnValue(false);
				return;
			}
			if (player.hasEffect(DawnCraftEffects.FROGFORM.get())) {
				KeyMapping mapping = (KeyMapping)(Object)this;
				if (ClientHandler.FROG_KEYS.contains(mapping)) callback.setReturnValue(false);
			}
		}
	}

	@Inject(at=@At("HEAD"), method = "matchesMouse(I)Z", cancellable = true)
	public void dctweaks$matchesMouse(int button, CallbackInfoReturnable<Boolean> callback) {
		Minecraft mc = Minecraft.getInstance();
		LocalPlayer player = mc.player;
		if (player != null) {
			if (player.hasEffect(DawnCraftEffects.IMMOBILIZED.get())) {
				callback.setReturnValue(false);
				callback.cancel();
				return;
			}
			if (player.hasEffect(DawnCraftEffects.FROGFORM.get())) {
				KeyMapping mapping = (KeyMapping)(Object)this;
				if (ClientHandler.FROG_KEYS.contains(mapping)) callback.setReturnValue(false);
			}
		}
	}

}
