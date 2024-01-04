package com.afunproject.dawncraft.integration.quests.custom.quests;

import com.afunproject.dawncraft.integration.quests.custom.QuestEntity;
import com.afunproject.dawncraft.integration.quests.custom.QuestType;
import com.afunproject.dawncraft.integration.quests.custom.conditions.QuestCondition;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.TagKey;
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

	protected final BlockPos findStructure(ServerLevel level, ResourceLocation structure, BlockPos center) {
		Registry<ConfiguredStructureFeature<?, ?>> r = level.registryAccess().registryOrThrow(Registry.CONFIGURED_STRUCTURE_FEATURE_REGISTRY);
		TagKey<ConfiguredStructureFeature<?, ?>> structureTag = TagKey.m_203882_(Registry.CONFIGURED_STRUCTURE_FEATURE_REGISTRY, structure);
		return level.getChunkSource().getGenerator()
				.m_207970_(level, r.m_203431_(structureTag).get(), new BlockPos(center), 100, false).getFirst();
	}

	protected final ItemStack createMap(ServerLevel level, BlockPos structure) {
		if (structure != null) {
			ItemStack itemstack = MapItem.create(level, structure.getX(), structure.getZ(), (byte)1, true, true);
			MapItem.renderBiomePreviewMap(level, itemstack);
			MapItemSavedData.addTargetDecoration(itemstack, structure, "+", Type.RED_X);
			return itemstack;
		}
		return MapItem.create(level, structure.getX(), structure.getZ(), (byte)2, true, true);
	}

}
