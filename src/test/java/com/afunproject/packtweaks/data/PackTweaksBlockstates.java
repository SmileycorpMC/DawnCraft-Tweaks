package com.afunproject.packtweaks.data;

import com.afunproject.dawncraft.Constants;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.ExistingFileHelper;

public class PackTweaksBlockstates /*extends AtlasDataProvider*/ {

	public PackTweaksBlockstates(DataGenerator gen, ExistingFileHelper exFileHelper) {
		//super(gen, PackTweaks.MODID, exFileHelper);
	}

	//@Override
	public String getName() {
		return Constants.MODID;
	}

	//@Override
	protected void registerData() {
		/*shapedBlock(DungeonBlocks.FOREST_BRICK);
		shapedBlock(DungeonBlocks.FIRE_BRICK);
		shapedBlock(DungeonBlocks.CORAL);
		shapedBlock(DungeonBlocks.MAGMA_SLATE);
		shapedBlock(DungeonBlocks.MOSS_BRICK);
		shapedBlock(DungeonBlocks.MUD_BRICK);
		shapedBlock(DungeonBlocks.ROCK);
		shapedBlock(DungeonBlocks.SAND_BRICK);
		shapedBlock(DungeonBlocks.SAND_SLATE);
		shapedBlock(DungeonBlocks.SHADOW_BRICK);
		shapedBlock(DungeonBlocks.WATER_BRICK);*/
	}

}
