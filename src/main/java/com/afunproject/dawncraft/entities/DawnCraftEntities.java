package com.afunproject.dawncraft.entities;

import com.afunproject.dawncraft.ModDefinitions;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class DawnCraftEntities {

	public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(ForgeRegistries.ENTITIES, ModDefinitions.MODID);

	public static final RegistryObject<EntityType<Fallen>> FALLEN = ENTITIES.register("fallen",  () ->
	EntityType.Builder.<Fallen>of(Fallen::new, MobCategory.MONSTER).sized(0.6F, 1.95F).clientTrackingRange(16).build("fallen"));

	public static final RegistryObject<EntityType<QuestPlayer>> QUEST_PLAYER = ENTITIES.register("quest_player",  () ->
	EntityType.Builder.<QuestPlayer>of(QuestPlayer::new, MobCategory.CREATURE).sized(0.6F, 1.95F).clientTrackingRange(16).build("quest_player"));

	public static final RegistryObject<EntityType<KnightPlayer>> KNIGHT_PLAYER = ENTITIES.register("knight_player",  () ->
	EntityType.Builder.<KnightPlayer>of(KnightPlayer::new, MobCategory.AMBIENT).sized(0.6F, 1.8F).build("knight_player"));

	/*public static final RegistryObject<EntityType<GreaterSandwyrm>> GREATER_SANDWYRM = ENTITIES.register("greater_sandwyrm",  () ->
	EntityType.Builder.<GreaterSandwyrm>of(GreaterSandwyrm::new, MobCategory.MONSTER).sized(0.6F, 1.8F).build("greater_sandwyrm"));

	public static final RegistryObject<EntityType<GreaterSandwyrmSegment>> GREATER_SANDWYRM_SEGMENT = ENTITIES.register("greater_sandwyrm_segment",  () ->
	EntityType.Builder.<GreaterSandwyrmSegment>of(GreaterSandwyrmSegment::new, MobCategory.MONSTER).sized(0.6F, 1.8F).build("greater_sandwyrm_segment"));*/

}
