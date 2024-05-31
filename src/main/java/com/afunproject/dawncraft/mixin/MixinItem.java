package com.afunproject.dawncraft.mixin;

import com.afunproject.dawncraft.integration.ironspellbooks.IronsSpellbooksCompat;
import com.afunproject.dawncraft.integration.sophisticatedbackpacks.SophisticatedBackpacksCompat;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.fml.ModList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Item.class)
public abstract class MixinItem {

	@Inject(at=@At("HEAD"), method = "canFitInsideContainerItems()Z", cancellable = true)
	public void dctweaks$anFitInsideContainerItems(CallbackInfoReturnable<Boolean> callback) {
		if (ModList.get().isLoaded("sophisticatedbackpacks")) if (SophisticatedBackpacksCompat.isBackpack((Item)(Object)this))
			callback.setReturnValue(false);
	}
	
	@Inject(at = @At("HEAD"), method = "finishUsingItem")
	public void dctweaks$finishUsingItem(ItemStack stack, Level level, LivingEntity entity, CallbackInfoReturnable<ItemStack> callback) {
		if (ModList.get().isLoaded("irons_spellbooks")) if (IronsSpellbooksCompat.isSpellBook(stack))
			entity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 20, 2));
	}
	
}
