package com.afunproject.dawncraft.invasion;

import com.afunproject.dawncraft.capability.DCCapabilities;
import com.afunproject.dawncraft.capability.Invader;
import com.google.common.collect.Lists;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.registries.ForgeRegistries;
import net.smileycorp.atlas.api.util.DirectionUtils;

import java.util.List;

public class InvasionEntry {

	protected final String name;
	protected final List<EntityType<?>> entities = Lists.newArrayList();

	public InvasionEntry(String name, EntityType<?>... entities) {
		this.name = name;
		for (EntityType<?> entity : entities) this.entities.add(entity);
	}

	public InvasionEntry(CompoundTag tag) {
		name = tag.getString("name");
		for (Tag value : tag.getList("tag", 8)) {
			try {
				ResourceLocation loc = new ResourceLocation(((StringTag)value).getAsString());
				entities.add(ForgeRegistries.ENTITIES.getValue(loc));
			} catch (Exception e) {}
		}
	}

	public void spawnEntities(Player player) {
		Level level = player.level;
		for (EntityType<?> type : entities) {
			Vec3 dir = DirectionUtils.getRandomDirectionVecXZ(player.getRandom());
			BlockPos pos = DirectionUtils.getClosestLoadedPos(level, player.blockPosition(), dir, 50);
			Mob entity = (Mob) type.create(level);
			entity.setPos(pos.getX(), pos.getY(), pos.getZ());
			DifficultyInstance difficulty = level.getCurrentDifficultyAt(pos);
			entity.finalizeSpawn((ServerLevelAccessor) level, difficulty, null, null, null);
			entity.getAttribute(Attributes.FOLLOW_RANGE).setBaseValue(100.0D);
			if (entity instanceof PathfinderMob) {
				entity.targetSelector.addGoal(1, new HurtByTargetGoal((PathfinderMob)entity));
			}
			entity.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(entity, Player.class, true));
			entity.goalSelector.addGoal(6, new InvasionHuntPlayerGoal(entity, player));
			entity.setPersistenceRequired();
			LazyOptional<Invader> cap = entity.getCapability(DCCapabilities.INVADER);
			if (cap.isPresent()) cap.orElseGet(null).setInvasion(player);
			level.addFreshEntity(entity);
			entity.addEffect(new MobEffectInstance(MobEffects.GLOWING, 1000));
			Vec3 sound = player.position().add(dir);
			level.playSound(null, sound.x, sound.y, sound.z, SoundEvents.PORTAL_TRAVEL, SoundSource.HOSTILE, 1f, level.random.nextFloat());
			player.displayClientMessage(new TranslatableComponent("message.dawncraft.invasion", name).setStyle(Style.EMPTY.withColor(ChatFormatting.RED).withBold(true)), true);
		}
	}

	public CompoundTag save() {
		CompoundTag tag =  new CompoundTag();
		tag.putString("name", name);
		ListTag list = new ListTag();
		for (EntityType<?> type : entities) list.add(StringTag.valueOf(type.getRegistryName().toString()));
		tag.put("entities", list);
		return tag;
	}

	public InvasionEntry copy() {
		return new InvasionEntry(name, entities.toArray(new EntityType<?>[] {}));
	}
}