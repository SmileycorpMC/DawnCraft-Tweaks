package com.afunproject.afptweaks.dungeon.block.entity;

import com.afunproject.afptweaks.dungeon.block.entity.base.CamouflagedTriggerableBlockEntity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class RedstoneTriggerBlockEntity extends CamouflagedTriggerableBlockEntity {

	public RedstoneTriggerBlockEntity(BlockPos pos, BlockState state) {
		this(DungeonBlockEntities.REDSTONE_TRIGGER.get(), pos, state);
	}

	public RedstoneTriggerBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
		super(type, pos, state);
	}

}
