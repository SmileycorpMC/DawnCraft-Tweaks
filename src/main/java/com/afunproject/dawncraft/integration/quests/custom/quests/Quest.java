package com.afunproject.dawncraft.integration.quests.custom.quests;

import java.util.Optional;

import com.afunproject.dawncraft.integration.quests.custom.QuestEntity;
import com.afunproject.dawncraft.integration.quests.custom.QuestType;
import com.afunproject.dawncraft.integration.quests.custom.conditions.QuestCondition;
import com.mojang.datafixers.util.Pair;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.Registry;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.MapItem;
import net.minecraft.world.level.levelgen.feature.ConfiguredStructureFeature;
import net.minecraft.world.level.saveddata.maps.MapDecoration.Type;
import net.minecraft.world.level.saveddata.maps.MapItemSavedData;

public abstract class Quest {

	protected ResourceLocation registry;
	protected QuestCondition[] conditions;

	public Quest(QuestCondition... conditions) {
		this.conditions = conditions;
	}

	void setRegistryName(ResourceLocation registry) {
		this.registry = registry;
	}

	public ResourceLocation getRegistryName() {
		return registry;
	}

	public boolean isQuestComplete(Player player, Mob entity, int phase) {
		for (QuestCondition condition : conditions) if (!condition.apply(player, entity, phase, true)) return false;
		for (QuestCondition condition : conditions) condition.apply(player, entity, phase, false);
		return true;
	}

	protected void setPhase(Mob entity, int phase) {
		QuestEntity.safeCast(entity).setQuestPhase(phase);
	}

	public void onDeath(Mob entity, DamageSource source) {}

	public void onHurt(Mob entity, DamageSource source) {}

	public abstract void completeQuest(Player quest_completer, Mob entity, int phase, boolean accepted);

	public abstract String getText(int phase, boolean accepted);

	public abstract QuestType getQuestType(int phase);

	public abstract boolean isQuestActive(Mob entity, int phase);

	protected final void giveItem(Player player, ItemStack stack) {
		if (!player.addItem(stack)) {
			player.drop(stack, false);
		}
	}

	protected final ItemStack createMap(ServerLevel level, BlockPos center, ResourceLocation structure, String name) {
		Registry<ConfiguredStructureFeature<?, ?>> registry = level.registryAccess().registryOrThrow(Registry.CONFIGURED_STRUCTURE_FEATURE_REGISTRY);
		ResourceKey<ConfiguredStructureFeature<?, ?>> structureKey = ResourceKey.create(Registry.CONFIGURED_STRUCTURE_FEATURE_REGISTRY, structure);
		Optional<Holder<ConfiguredStructureFeature<?, ?>>> structureOptional = registry.m_203636_(structureKey);
		if (structureOptional.isPresent()) {
			Pair<BlockPos, Holder<ConfiguredStructureFeature<?, ?>>> pair = level.getChunkSource().getGenerator()
					.m_207970_(level, HolderSet.m_205809_(structureOptional.get()), center, 1, false);
			if (pair != null) {
				BlockPos pos = pair.getFirst();
				ItemStack itemstack = MapItem.create(level, pos.getX(), pos.getZ(), (byte)2, true, true);
				MapItem.renderBiomePreviewMap((ServerLevel) level, itemstack);
				MapItemSavedData.addTargetDecoration(itemstack, pos, "+", Type.BANNER_BLACK);
				itemstack.setHoverName(new TranslatableComponent("cultist_map"));
				return itemstack;
			}
		}
		return MapItem.create(level, center.getX(), center.getZ(), (byte)2, true, true);
	}

}
