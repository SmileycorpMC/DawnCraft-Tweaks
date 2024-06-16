package com.afunproject.dawncraft.integration.quests;

import com.afunproject.dawncraft.integration.quests.custom.entity.QuestEntities;
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

import java.util.Locale;

public enum QuestNPC implements StringRepresentable {

	WEREWOLF(QuestEntities.QUEST_PLAYER.get(), "{\"username\":\"hjvu55\", \"quest\":\"dawncraft:werewolf\", \"damageable\":1b}"),
	BELL_GHOST(QuestEntities.FALLEN_ADVENTURER.get(), "{\"username\":\"NickWins\", \"quest\":\"dawncraft:ghost\"}"),
	CULT_INFORMER(QuestEntities.QUEST_PLAYER.get(), "{\"username\":\"dg2q\", \"quest\":\"dawncraft:cult\"}"),
	BARREL(QuestEntities.QUEST_PLAYER.get(), "{\"username\":\"JosslynMC\", \"quest\":\"dawncraft:barrel\"}"),
	ALCHEMIST(QuestEntities.QUEST_PLAYER.get(), "{\"username\":\"Jormungandirr\", \"quest\":\"dawncraft:alchemist\"}\"}"),
	MASK_GHOST(QuestEntities.FALLEN_ADVENTURER.get(), "{\"username\":\"Sir_Sense\", \"quest\":\"dawncraft:mask_ghost\"}"),
	WITCH(QuestEntities.QUEST_PLAYER.get(), "{\"username\":\"zherterry\", \"quest\":\"dawncraft:witch\", \"damageable\":1b}"),
	KING(QuestEntities.QUEST_PLAYER.get(), "{\"username\":\"Dark_King_0507\", \"quest\":\"dawncraft:king\"}"),
	MONSTER_SLAYER(QuestEntities.QUEST_PLAYER.get(), "{\"username\":\"ReaperDoge\", \"quest\":\"dawncraft:monster_slayer\"}\"}"),
	SAGE(QuestEntities.QUEST_PLAYER.get(), "{\"username\":\"1Venty1\", \"quest\":\"dawncraft:sage\"}\"}"),
	SORCERER(QuestEntities.QUEST_PLAYER.get(), "{\"username\":\"Arche_Princess\", \"quest\":\"dawncraft:sorcerer\"}\"}");

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
		return name().toLowerCase(Locale.US);
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
