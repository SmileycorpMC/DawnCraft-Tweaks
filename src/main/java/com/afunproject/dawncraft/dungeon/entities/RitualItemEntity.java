package com.afunproject.dawncraft.dungeon.entities;

import java.util.List;

import com.google.common.collect.Lists;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializer;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class RitualItemEntity extends Entity {

	public static final EntityDataSerializer<List<BlockPos>> POS_LIST_SERIALIZER = new EntityDataSerializer<List<BlockPos>>() {

		@Override
		public void write(FriendlyByteBuf buf, List<BlockPos> positions) {
			for (BlockPos pos : positions) buf.writeBlockPos(pos);
		}

		@Override
		public List<BlockPos> read(FriendlyByteBuf buf) {
			List<BlockPos> positions = Lists.newArrayList();
			while (true) {
				try {
					BlockPos pos = buf.readBlockPos();
					positions.add(pos);
				} catch (Exception e) {
					break;
				}
			}
			return positions;
		}

		@Override
		public List<BlockPos> copy(List<BlockPos> list) {
			return Lists.newArrayList(list);
		}
	};

	static {
		EntityDataSerializers.registerSerializer(POS_LIST_SERIALIZER);
	}

	public static final EntityDataAccessor<ItemStack> ITEM = SynchedEntityData.defineId(RitualItemEntity.class, EntityDataSerializers.ITEM_STACK);
	public static final EntityDataAccessor<List<BlockPos>> PEDESTALS = SynchedEntityData.defineId(RitualItemEntity.class, POS_LIST_SERIALIZER);

	protected ItemStack result = ItemStack.EMPTY;
	protected int timer = 100;

	public RitualItemEntity(EntityType<? extends RitualItemEntity> p_31991_, Level p_31992_) {
		super(p_31991_, p_31992_);
	}

	public RitualItemEntity(Level level, ItemStack stack, ItemStack result, BlockPos pos, List<BlockPos> pedestals) {
		super(DawnCraftEntities.RITUAL_ITEM.get(), level);
		setPos(pos.getX()+0.5f, pos.getY()+0.5f, pos.getZ()+0.5f);
		entityData.set(ITEM, stack);
		this.result = result;
		entityData.set(PEDESTALS, pedestals);
		noPhysics = true;
	}

	@Override
	public boolean fireImmune() {
		return true;
	}

	public ItemStack getItem() {
		return entityData.get(ITEM);
	}

	public ItemStack getResult() {
		return result;
	}

	public void setItem(ItemStack stack) {
		entityData.set(ITEM, stack);
	}

	public void setResult(ItemStack stack) {
		result = stack;
	}

	public List<BlockPos> getPedestals() {
		return entityData.get(PEDESTALS);
	}

	@Override
	public void tick() {
		super.tick();
	}

	@Override
	protected void defineSynchedData() {
		entityData.define(ITEM, ItemStack.EMPTY);
		entityData.define(PEDESTALS, Lists.newArrayList());
	}

	@Override
	protected void readAdditionalSaveData(CompoundTag p_20052_) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void addAdditionalSaveData(CompoundTag p_20139_) {
		// TODO Auto-generated method stub

	}

	@Override
	public Packet<?> getAddEntityPacket() {
		// TODO Auto-generated method stub
		return null;
	}

}
