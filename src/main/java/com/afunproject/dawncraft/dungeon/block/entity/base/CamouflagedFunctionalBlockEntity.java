package com.afunproject.dawncraft.dungeon.block.entity.base;

import com.afunproject.dawncraft.dungeon.block.DungeonBlocks;
import com.afunproject.dawncraft.dungeon.block.entity.interfaces.Disguisable;
import com.afunproject.dawncraft.dungeon.block.entity.interfaces.Functional;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.model.data.IModelData;
import net.minecraftforge.client.model.data.ModelDataMap;

public abstract class CamouflagedFunctionalBlockEntity extends BlockEntity implements Functional, Disguisable {

	protected BlockState texture = DungeonBlocks.FOREST_BRICK.getBase().defaultBlockState();

	public CamouflagedFunctionalBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
		super(type, pos, state);
	}

	@Override
	public BlockState getTexture() {
		return texture;
	}

	@Override
	public void setTexture(BlockState block) {
		texture = block;
	}

	@Override
	public void load(CompoundTag tag) {
		if (tag.contains("texture")) try {
			texture = NbtUtils.readBlockState(tag.getCompound("texture"));
		} catch (Exception e) {}
		super.load(tag);
	}

	@Override
	protected void saveAdditional(CompoundTag tag) {
		super.saveAdditional(tag);
		if (texture != null) tag.put("texture", NbtUtils.writeBlockState(texture));
	}

	@Override
	public CompoundTag getUpdateTag() {
		CompoundTag tag = super.getUpdateTag();
		if (texture != null) tag.put("texture", NbtUtils.writeBlockState(texture));
		return tag;
	}

	@Override
	public IModelData getModelData() {
		return new ModelDataMap.Builder().withInitial(TEXTURE, texture).build();
	}

}
