package com.afunproject.dawncraft.mixin;

import com.afunproject.dawncraft.dungeon.item.AdventureItem;
import net.minecraft.core.Registry;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.pattern.BlockInWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ItemStack.class)
public abstract class MixinItemStack {

	@Inject(at=@At("HEAD"), method = "m_204121_(Lnet/minecraft/core/Registry;Lnet/minecraft/world/level/block/state/pattern/BlockInWorld;)Z", cancellable = true)
	public void dctweaks$m_204121_(Registry<Block> registry, BlockInWorld block, CallbackInfoReturnable<Boolean> callback) {
		if (getItem() instanceof AdventureItem) callback.setReturnValue(true);
	}

	@Shadow
	public abstract Item getItem();

}
