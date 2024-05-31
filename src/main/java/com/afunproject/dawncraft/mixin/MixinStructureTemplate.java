package com.afunproject.dawncraft.mixin;

import com.afunproject.dawncraft.dungeon.block.entity.interfaces.RotatableBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate.StructureBlockInfo;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;
import java.util.Random;

@Mixin(StructureTemplate.class)
public abstract class MixinStructureTemplate {

	private List<StructureBlockInfo> blockCache = null;

	@Shadow
	private Vec3i size = null;

	@Inject(at=@At("TAIL"), method = "placeInWorld(Lnet/minecraft/world/level/ServerLevelAccessor;Lnet/minecraft/core/BlockPos;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructurePlaceSettings;Ljava/util/Random;I)Z", cancellable = true)
	public void dctweaks$placeInWorld(ServerLevelAccessor level, BlockPos start, BlockPos end, StructurePlaceSettings settings, Random rand, int p_74542_, CallbackInfoReturnable<Boolean> callback) {
		if (callback.getReturnValue() && blockCache != null && size.getX() >= 1 && size.getY() >= 1 && size.getZ() >= 1) {
			for(StructureTemplate.StructureBlockInfo structuretemplate$structureblockinfo : blockCache) {
				BlockPos blockpos = structuretemplate$structureblockinfo.pos;
				BlockEntity blockentity1 = level.getBlockEntity(blockpos);
				if (blockentity1 instanceof RotatableBlockEntity) {
					((RotatableBlockEntity) blockentity1).mirror(settings.getMirror());
					((RotatableBlockEntity) blockentity1).rotate(settings.getRotation());
				}
			}
			blockCache = null;
		}
	}

	@ModifyVariable(method = "placeInWorld", at = @At(value = "STORE", ordinal = 4))
	private List<StructureBlockInfo> dctweaks$placeInWorld(List<StructureBlockInfo> blockInfo) {
		blockCache = blockInfo;
		return blockInfo;
	}

}
