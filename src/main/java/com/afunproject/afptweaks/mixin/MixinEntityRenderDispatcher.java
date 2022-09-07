package com.afunproject.afptweaks.mixin;

import org.spongepowered.asm.mixin.Mixin;

import com.afunproject.afptweaks.client.EntityRenderDispatcherExtension;

import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.server.packs.resources.ResourceManagerReloadListener;

@Mixin(EntityRenderDispatcher.class)
public abstract class MixinEntityRenderDispatcher implements ResourceManagerReloadListener, EntityRenderDispatcherExtension {

	private boolean shouldRenderNameplate = true;

	@Override
	public void setRenderNameplate(boolean bool) {
		shouldRenderNameplate = bool;
	}

	@Override
	public boolean shouldRenderNameplate() {
		return shouldRenderNameplate;
	}

}
