package com.silverminer.dungeon_quest.structure;

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
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public class DungeonQuestStructureFeature extends StructureFeature<DungeonQuestConfiguration> {

   public DungeonQuestStructureFeature() {
      super(DungeonQuestConfiguration.CODEC, DungeonQuestStructureFeature::place);
   }

   private static @NotNull Optional<PieceGenerator<DungeonQuestConfiguration>> place(PieceGeneratorSupplier.Context<DungeonQuestConfiguration> context) {
      if(!checkLocation(context)) {
         return Optional.empty();
      } else {
         BlockPos position = context.chunkPos().getMiddleBlockPosition(0);
         position = new BlockPos(position.getX(),
               context.chunkGenerator().getFirstFreeHeight(position.getX(), position.getZ(), Heightmap.Types.WORLD_SURFACE_WG, context.heightAccessor()),
               position.getZ());
         Optional<PieceGenerator<JigsawConfiguration>> pieceGenerator = JigsawPlacement.addPieces(
               new PieceGeneratorSupplier.Context<>(context.chunkGenerator(), context.biomeSource(), context.seed(),
                     context.chunkPos(), context.config().jigsawConfiguration(), context.heightAccessor(),
                     context.validBiome(), context.structureManager(), context.registryAccess()),
               PoolElementStructurePiece::new, position,
               false, true);
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