package com.afunproject.dawncraft.entities;

import com.afunproject.dawncraft.DawnCraft;
import com.google.common.collect.Lists;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializer;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.smileycorp.atlas.api.util.DirectionUtils;

import java.util.List;

public class RitualItemEntity extends Entity {

	public static final EntityDataAccessor<ItemStack> ITEM = SynchedEntityData.defineId(RitualItemEntity.class, EntityDataSerializers.ITEM_STACK);
	private List<BlockPos> pedestals = Lists.newArrayList();

	protected ItemStack result = ItemStack.EMPTY;

	public RitualItemEntity(EntityType<? extends RitualItemEntity> p_31991_, Level p_31992_) {
		super(p_31991_, p_31992_);
	}

	public RitualItemEntity(Level level, ItemStack stack, ItemStack result, BlockPos pos, List<BlockPos> pedestals) {
		super(DawnCraftEntities.RITUAL_ITEM.get(), level);
		setPos(pos.getX()+0.5f, pos.getY()+1.5f, pos.getZ()+0.5f);
		entityData.set(ITEM, stack);
		this.result = result;
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
		return pedestals;
	}

	@Override
	public void tick() {
		super.tick();
		if (!(level instanceof ServerLevel)) return;
		moveTo(position().x, position().y+0.009, position().z);
		if (random.nextInt(30) == 0) playSound(SoundEvents.ZOMBIE_VILLAGER_CURE, 0.5f, random.nextFloat());
		if (tickCount == 400) {
			ItemEntity drop = new ItemEntity(level, position().x, position().y, position().z, result);
			this.kill();
			level.addFreshEntity(drop);
			playSound(SoundEvents.END_PORTAL_SPAWN, 0.75f, 0.75f);
			LightningBolt bolt = EntityType.LIGHTNING_BOLT.create(level);
			bolt.setPos(position().add(0, -3, 0));
			bolt.setVisualOnly(true);
			level.addFreshEntity(bolt);
		}
		if (tickCount >= 330) return;
		for (BlockPos pos : pedestals) {
			Vec3 start = new Vec3(pos.getX() + 0.5, pos.getY() + 1, pos.getZ() + 0.5);
			Vec3 dir = DirectionUtils.getDirectionVec(start, position().add(0, 1, 0));
			Vec3 dir1 = DirectionUtils.getDirectionVecXZ(position(), start);
			((ServerLevel)level).sendParticles(ParticleTypes.REVERSE_PORTAL, start.x, start.y, start.z, 1, dir.x*0.15, dir.y*0.15, dir.z*0.15, 0);
			((ServerLevel)level).sendParticles(ParticleTypes.REVERSE_PORTAL, position().x, start.y-0.5f, position().z, 1, dir1.x*0.1, dir1.y*0.1, dir1.z*0.1, 0);
		}
	}

	@Override
	protected void defineSynchedData() {
		entityData.define(ITEM, ItemStack.EMPTY);
	}

	@Override
	protected void readAdditionalSaveData(CompoundTag tag) {
		if (tag.contains("item")) entityData.set(ITEM, ItemStack.of(tag.getCompound("item")));
		if (tag.contains("result")) result = ItemStack.of(tag.getCompound("result"));
		if (tag.contains("pedestals")) {
			for (Tag pedestal : tag.getList("pedestals", 10)) {
				CompoundTag pos = (CompoundTag) pedestal;
				pedestals.add(new BlockPos (pos.getInt("x"), pos.getInt("y"), pos.getInt("z")));
			}
		}
	}

	@Override
	protected void addAdditionalSaveData(CompoundTag tag) {
		CompoundTag stack = entityData.get(ITEM).save(new CompoundTag());
		CompoundTag result = this.result.save(new CompoundTag());
		ListTag pedestals = new ListTag();
		for (BlockPos pos : this.pedestals) {
			CompoundTag posTag = new CompoundTag();
			posTag.putInt("x", pos.getX());
			posTag.putInt("y", pos.getY());
			posTag.putInt("z", pos.getZ());
			pedestals.add(posTag);
		}
		tag.put("item", stack);
		tag.put("result", result);
		tag.put("pedestals", pedestals);
	}

	@Override
	public Packet<?> getAddEntityPacket() {
		return new ClientboundAddEntityPacket(this);
	}

}
