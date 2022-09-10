package com.afunproject.afptweaks.capability;

import java.util.List;

import com.google.common.collect.Lists;

import net.mcreator.simplemobs.init.SimpleMobsModEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.registries.ForgeRegistries;
import net.smileycorp.atlas.api.entity.ai.GoToEntityPositionGoal;
import net.smileycorp.atlas.api.util.DirectionUtils;

public interface Invasions {

	public void tryToSpawnInvasion(Player player);

	public CompoundTag save();

	public void load(CompoundTag tag);

	public static class Implementation implements Invasions {

		private List<InvasionEntry> invasions = Lists.newArrayList();

		public Implementation() {
			invasions.add(new InvasionEntry(240000, "Getsuga65", SimpleMobsModEntities.GETSUGA_65.get()));
			invasions.add(new InvasionEntry(600000, "Wooden_Day", SimpleMobsModEntities.WOODENDAY.get()));
		}

		@Override
		public void tryToSpawnInvasion(Player player) {
			if (!player.level.isClientSide &! invasions.isEmpty()) {
				InvasionEntry invasion = invasions.get(0);
				if (invasion.getTime() <= player.tickCount) {
					invasion.spawnEntities(player);
					invasions.remove(invasion);
				}
			}
		}

		@Override
		public CompoundTag save() {
			CompoundTag tag = new CompoundTag();
			ListTag list = new ListTag();
			for (InvasionEntry invasion : invasions) list.add(invasion.save());
			tag.put("invasions", list);
			return tag;
		}

		@Override
		public void load(CompoundTag tag) {
			invasions.clear();
			for (Tag invasion : tag.getList("invasions", 10)) try {
				invasions.add(new InvasionEntry((CompoundTag)invasion));
			} catch (Exception e) {}
		}

	}

	public static class InvasionEntry {

		protected final int time;
		protected final String name;
		protected final List<EntityType<?>> entities = Lists.newArrayList();

		public InvasionEntry(int time, String name, EntityType<?>... entities) {
			this.time = time;
			this.name = name;
			for (EntityType<?> entity : entities) this.entities.add(entity);
		}

		public InvasionEntry(CompoundTag tag) {
			time = tag.getInt("time");
			name = tag.getString("name");
			for (Tag value : tag.getList("tag", 8)) {
				try {
					ResourceLocation loc = new ResourceLocation(((StringTag)value).getAsString());
					entities.add(ForgeRegistries.ENTITIES.getValue(loc));
				} catch (Exception e) {}
			}
		}

		public int getTime() {
			return time;
		}

		public void spawnEntities(Player player) {
			Level level = player.level;
			for (EntityType<?> type : entities) {
				BlockPos pos = DirectionUtils.getClosestLoadedPos(level, player.blockPosition(), DirectionUtils.getRandomDirectionVecXZ(player.getRandom()), 50);
				Mob entity = (Mob) type.create(level);
				entity.setPos(pos.getX(), pos.getY(), pos.getZ());
				DifficultyInstance difficulty = level.getCurrentDifficultyAt(pos);
				entity.finalizeSpawn((ServerLevelAccessor) level, difficulty, null, null, null);
				entity.getAttribute(Attributes.FOLLOW_RANGE).setBaseValue(100.0D);
				if (entity instanceof PathfinderMob) {
					entity.targetSelector.addGoal(1, new HurtByTargetGoal((PathfinderMob)entity));
				}
				entity.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(entity, Player.class, true));
				entity.goalSelector.addGoal(6, new GoToEntityPositionGoal(entity, player, 1.5));
				entity.setPersistenceRequired();
				level.addFreshEntity(entity);
				player.displayClientMessage(new TranslatableComponent("message.dawncraft.invasion", name), true);
			}
		}

		public CompoundTag save() {
			CompoundTag tag =  new CompoundTag();
			tag.putInt("time", time);
			tag.putString("name", name);
			ListTag list = new ListTag();
			for (EntityType<?> type : entities) list.add(StringTag.valueOf(type.getRegistryName().toString()));
			tag.put("entities", list);
			return tag;
		}
	}

	public static class Provider implements ICapabilitySerializable<CompoundTag> {

		protected Invasions impl = new Implementation();

		@Override
		public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction facing) {
			return cap == CapabilitiesRegister.INVASIONS ? LazyOptional.of(() -> impl).cast() : LazyOptional.empty();
		}

		@Override
		public CompoundTag serializeNBT() {
			return impl.save();
		}

		@Override
		public void deserializeNBT(CompoundTag nbt) {
			impl.load(nbt);
		}

	}

}
