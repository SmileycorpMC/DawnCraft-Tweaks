package com.afunproject.dawncraft.mixin;

import java.io.DataInput;
import java.util.Map;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.afunproject.dawncraft.ModDefinitions;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtAccounter;
import net.minecraft.nbt.Tag;

@Mixin(CompoundTag.class)
public abstract class MixinCompoundTag implements Tag {

	@Shadow
	private Map<String, Tag> tags;

	//automatically convert nbt from the old modid to the new one to make old worlds compatible
	@Inject(at=@At("TAIL"), method = "readNamedTagName(Ljava/io/DataInput;Lnet/minecraft/nbt/NbtAccounter;)Ljava/lang/String;", cancellable = true)
	private static void readNamedTagName(DataInput dataInput, NbtAccounter nbtAccounter, CallbackInfoReturnable<String> callback) {
		if (callback.getReturnValue().contains(ModDefinitions.LEGACY_MODID))
			callback.setReturnValue(callback.getReturnValue().replace(ModDefinitions.LEGACY_MODID, ModDefinitions.MODID));
	}

}
