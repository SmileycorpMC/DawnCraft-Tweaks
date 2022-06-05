/*
 * Copyright © 2022 AFunProject
 *
 * Permission is hereby granted, free of charge,
 * to any person obtaining a copy of this software
 * and associated documentation files (the “Software”),
 * to deal in the Software without restriction, including
 * without limitation the rights to use, copy, modify, merge,
 * publish, distribute, sublicense, and/or sell copies of the
 * Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED “AS IS”, WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL
 * THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
 * WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
 * CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.afunproject.packtweaks.silverminer.structuregen.structure;

import java.util.Optional;

import com.afunproject.packtweaks.PackTweaks;
import com.mojang.logging.LogUtils;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.StructureFeature;
import net.minecraft.world.level.levelgen.feature.configurations.JigsawConfiguration;
import net.minecraft.world.level.levelgen.structure.PoolElementStructurePiece;
import net.minecraft.world.level.levelgen.structure.StructureSet;
import net.minecraft.world.level.levelgen.structure.pieces.PieceGenerator;
import net.minecraft.world.level.levelgen.structure.pieces.PieceGeneratorSupplier;
import net.minecraft.world.level.levelgen.structure.placement.StructurePlacement;
import net.minecraft.world.level.levelgen.structure.pools.JigsawPlacement;

public class DungeonQuestStructureFeature extends StructureFeature<DungeonQuestConfiguration> {

	public DungeonQuestStructureFeature() {
		super(DungeonQuestConfiguration.CODEC, DungeonQuestStructureFeature::place);
	}

	private static @NotNull Optional<PieceGenerator<DungeonQuestConfiguration>> place(PieceGeneratorSupplier.Context<DungeonQuestConfiguration> context) {
		if(!checkLocation(context)) {
			return Optional.empty();
		} else {
			BlockPos position = context.chunkPos().getMiddleBlockPosition(0);
			int worldSurface = context.chunkGenerator().getFirstFreeHeight(position.getX(), position.getZ(), Heightmap.Types.WORLD_SURFACE_WG, context.heightAccessor());
			int minY = context.chunkGenerator().getMinY();
			position = new BlockPos(position.getX(),
					((worldSurface - minY) / 2) + minY,
					position.getZ());
			Optional<PieceGenerator<JigsawConfiguration>> pieceGenerator = JigsawPlacement.addPieces(
					new PieceGeneratorSupplier.Context<>(context.chunkGenerator(), context.biomeSource(), context.seed(),
							context.chunkPos(), context.config().jigsawConfiguration(), context.heightAccessor(),
							context.validBiome(), context.structureManager(), context.registryAccess()),
					PoolElementStructurePiece::new, position,
					false, false);
			return pieceGenerator.isEmpty() ? Optional.empty() :
				Optional.of((structurePieceBuilder, pieceGeneratorContext) ->
				pieceGenerator.get().generatePieces(structurePieceBuilder, convertContext(pieceGeneratorContext)));
		}
	}

	private static boolean checkLocation(@NotNull PieceGeneratorSupplier.Context<DungeonQuestConfiguration> context) {
		return hasFeatureChunkInRange(context.config().structureSetHolder().value(), context.seed(), context.chunkPos().x, context.chunkPos().z, context.config().distance(), context.chunkGenerator());
	}

	private static boolean hasFeatureChunkInRange(StructureSet structureSet, long seed, int chunkX, int chunkZ, int range, ChunkGenerator chunkGenerator) {
		if (structureSet != null) {
			StructurePlacement structureplacement = structureSet.placement();

			for (int i = chunkX - range; i <= chunkX + range; ++i) {
				for (int j = chunkZ - range; j <= chunkZ + range; ++j) {
					if (structureplacement.isFeatureChunk(chunkGenerator, range, i, j)) {
						return true;
					}
				}
			}

		}
		return false;
	}

	@Contract("_ -> new")
	private static PieceGenerator.@NotNull Context<JigsawConfiguration> convertContext(PieceGenerator.@NotNull Context<DungeonQuestConfiguration> context) {
		return new PieceGenerator.Context<>(
				context.config().jigsawConfiguration(),
				context.chunkGenerator(),
				context.structureManager(),
				context.chunkPos(),
				context.heightAccessor(),
				context.random(),
				context.seed()
				);
	}

	@Override
	@NotNull
	public GenerationStep.Decoration step() {
		return GenerationStep.Decoration.UNDERGROUND_STRUCTURES;
	}
}