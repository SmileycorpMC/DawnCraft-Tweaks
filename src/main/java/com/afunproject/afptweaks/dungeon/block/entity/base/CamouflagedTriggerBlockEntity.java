package com.afunproject.afptweaks.dungeon.block.entity.base;

import com.afunproject.afptweaks.dungeon.block.entity.interfaces.Disguisable;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.smileycorp.followme.common.ModDefinitions;

public abstract class CamouflagedTriggerBlockEntity extends TriggerBlockEntityBase implements Disguisable {

	protected ResourceLocation texture = ModDefinitions.getResource("forest_brick");

	public CamouflagedTriggerBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
		super(type, pos, state);
	}

	@Override
	public ResourceLocation getTexture() {
		return texture;
	}

	@Override
	public void setTexture(ResourceLocation block) {
		texture = block;
	}

	@Override
	public void load(CompoundTag tag) {
		if (tag.contains("texture")) try {
			texture = new ResourceLocation(tag.getString("texture"));
		} catch (Exception e) {}
		super.load(tag);
	}

	@Override
	protected void saveAdditional(CompoundTag tag) {
		super.saveAdditional(tag);
		tag.putString("texture", texture.toString());
	}

	@Override
	public CompoundTag getUpdateTag() {
		CompoundTag tag = super.getUpdateTag();
		tag.putString("texture", texture.toString());
		return tag;
	}

}
