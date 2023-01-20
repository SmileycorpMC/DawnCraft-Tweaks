package com.afunproject.dawncraft.dungeon.block.entity;

import com.afunproject.dawncraft.dungeon.block.entity.base.CamouflagedTriggerBlockEntity;

import net.minecraft.core.BlockPos;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class RedstoneTriggerBlockEntity extends CamouflagedTriggerBlockEntity {

	public RedstoneTriggerBlockEntity(BlockPos pos, BlockState state) {
		this(DungeonBlockEntities.REDSTONE_TRIGGER.get(), pos, state);
	}

	public RedstoneTriggerBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
		super(type, pos, state);
	}

	@Override
	public ClientboundBlockEntityDataPacket getUpdatePacket() {
		return ClientboundBlockEntityDataPacket.create(this);
	}

}
