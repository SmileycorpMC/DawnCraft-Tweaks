package com.afunproject.dawncraft;

import com.afunproject.dawncraft.entities.DawnCraftEntities;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

import net.minecraft.core.Vec3i;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.TagParser;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public enum QuestNPC implements StringRepresentable {

	WEREWOLF(DawnCraftEntities.QUEST_PLAYER.get(), "{\"username\":\"hjvu55\", \"quest\":\"dawncraft:werewolf\", \"damageable\":1b}"),
	BELL_GHOST(DawnCraftEntities.FALLEN.get(), "{\"username\":\"NickWins\", \"quest\":\"dawncraft:ghost\"}"),
	CULT_INFORMER(DawnCraftEntities.QUEST_PLAYER.get(), "{\"username\":\"dg2q\", \"quest\":\"dawncraft:cult\"}"),
	BARREL(DawnCraftEntities.QUEST_PLAYER.get(), "{\"username\":\"JosslynMC\", \"quest\":\"dawncraft:barrel\"}"),
	ALCHEMIST(DawnCraftEntities.QUEST_PLAYER.get(), "{\"username\":\"EmpyreanAsura\", \"quest\":\"dawncraft:alchemist\"}\"}");

	private final EntityType<?> type;
	private CompoundTag tag = new CompoundTag();

	QuestNPC(EntityType<?> type, String tag) {
		this.type = type;
		try {
			this.tag = new TagParser(new StringReader(tag)).readStruct();
		} catch (CommandSyntaxException e) {}
	}

	@Override
	public String getSerializedName() {
		return name().toLowerCase();
	}

	public Entity spawnEntity(Level level, double x, double y, double z) {
		return spawnEntity(level, new Vec3(x, y, z));
	}

	public Entity spawnEntity(Level level, Vec3i pos) {
		return spawnEntity(level, Vec3.atCenterOf(pos));
	}

	public Entity spawnEntity(Level level, Vec3 pos) {
		Entity entity = type.create(level);
		entity.load(tag);
		entity.setPos(pos);
		level.addFreshEntity(entity);
		return entity;
	}

}
