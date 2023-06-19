package com.afunproject.dawncraft.mixin;

import com.afunproject.dawncraft.Constants;
import com.afunproject.dawncraft.DCConfig;
import com.afunproject.dawncraft.effects.DawnCraftEffects;
import com.afunproject.dawncraft.integration.create.CreateCompat;
import com.afunproject.dawncraft.integration.sophisticatedbackpacks.SophisticatedBackpacksCompat;
import com.google.common.collect.Lists;
import net.minecraft.core.NonNullList;
import net.minecraft.core.Registry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.tags.TagKey;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(Player.class)
public abstract class MixinPlayer extends LivingEntity {

	@Shadow
	private Inventory inventory;

	@Shadow
	public int totalExperience;

	private TagKey<Item> valuables = TagKey.m_203882_(Registry.ITEM_REGISTRY, Constants.loc("valuables"));

	protected MixinPlayer(EntityType<? extends LivingEntity> p_20966_, Level p_20967_) {
		super(p_20966_, p_20967_);
	}

	@Inject(at=@At("HEAD"), method = "dropEquipment()V", cancellable = true)
	public void dropEquipment(CallbackInfo callback) {
		if (!DCConfig.harderKeepInventory.get() |! level.getGameRules().getBoolean(GameRules.RULE_KEEPINVENTORY)) return;
		destroyVanishingCursedItems();
		for (List<ItemStack> list : inventory.compartments) {
			handleItems(list, true);
		}
		callback.cancel();
	}

	public List<ItemStack> handleItems(List<ItemStack> items, boolean damageItems) {
		int amplifier = 1;
		if (hasEffect(DawnCraftEffects.FRACTURED_SOUL.get())) amplifier = getEffect(DawnCraftEffects.FRACTURED_SOUL.get()).getAmplifier() + 1;
		for (int i = 0; i < items.size(); i++) {
			ItemStack stack = items.get(i);
			if (stack.isDamageableItem()) {
				if (damageItems) stack.setDamageValue((int) Math.min(stack.getMaxDamage(), stack.getDamageValue() + (amplifier*0.02*stack.getMaxDamage())));
			}
			if (stack.m_204117_(valuables)) {
				if (level.random.nextInt(10) < 3) {
					ItemStack drop = stack.copy();
					int count = stack.getCount();
					int loss = random.nextInt(0, stack.getCount());
					int newCount = count - loss;
					if (newCount < 0) drop.setCount(stack.getCount());
					else drop.setCount(loss);
					stack.setCount(Math.max(0, newCount));
					drop(drop, true, false);
				}
			} else if (stack.getItem() == Blocks.SHULKER_BOX.asItem()) {
				CompoundTag tag = BlockItem.getBlockEntityData(stack);
				if (tag !=null) {
					if(tag.contains("Items", 9)) {
						NonNullList<ItemStack> shulker_items = NonNullList.withSize(27, ItemStack.EMPTY);
						ContainerHelper.loadAllItems(tag, shulker_items);
						handleItems(shulker_items, false);
						ContainerHelper.saveAllItems(tag, shulker_items);
						BlockItem.setBlockEntityData(stack, BlockEntityType.SHULKER_BOX, tag);
					}
				}
			} else {
				if (ModList.get().isLoaded("sophisticatedbackpacks")) if (SophisticatedBackpacksCompat.isBackpack(stack.getItem())) {
					LazyOptional<IItemHandler> optional = stack.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY);
					if (optional.isPresent()) {
						IItemHandler itemhandler = optional.resolve().get();
						List<ItemStack> backpack_items = Lists.newArrayList();
						for (int j = 0; j < itemhandler.getSlots(); j++) backpack_items.add(itemhandler.getStackInSlot(j));
						handleItems(backpack_items, false);
						for (int j = 0; j < backpack_items.size(); j++) {
							ItemStack stack0 = itemhandler.getStackInSlot(j);
							itemhandler.extractItem(j, stack0.getCount(), false);
							itemhandler.insertItem(j, backpack_items.get(j), false);
						}
					}
				}
				if (ModList.get().isLoaded("create")) if (CreateCompat.isToolbox(stack.getItem())) {
					CompoundTag tag = stack.getOrCreateTag();
					if (tag !=null) {
						if(tag.contains("Items", 9)) {
							NonNullList<ItemStack> toolbox_items = NonNullList.withSize(27, ItemStack.EMPTY);
							ContainerHelper.loadAllItems(tag, toolbox_items);
							handleItems(toolbox_items, false);
							ContainerHelper.saveAllItems(tag, toolbox_items);
							stack.save(tag);
						}
					}
				}
			}
		}
		return items;
	}

	@Inject(at=@At("HEAD"), method = "getExperienceReward(Lnet/minecraft/world/entity/player/Player;)I", cancellable = true)
	protected void getExperienceReward(Player player, CallbackInfoReturnable<Integer> callback) {
		if (!DCConfig.harderKeepInventory.get() |! level.getGameRules().getBoolean(GameRules.RULE_KEEPINVENTORY)) return;
		if (hasEffect(DawnCraftEffects.FRACTURED_SOUL.get())) {
			giveExperienceLevels(-getEffect(DawnCraftEffects.FRACTURED_SOUL.get()).getAmplifier()-1);
		} else {
			giveExperienceLevels(-1);
		}
	}

	@Shadow
	protected abstract void destroyVanishingCursedItems();

	@Shadow
	public abstract ItemEntity drop(ItemStack p_36179_, boolean p_36180_, boolean p_36181_);

	@Shadow
	public abstract void giveExperienceLevels(int p_36276_);

}
