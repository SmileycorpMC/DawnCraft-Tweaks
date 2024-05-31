package com.afunproject.dawncraft.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Quaternion;
import com.mojang.math.Vector3f;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.MapRenderer;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.level.saveddata.maps.MapDecoration;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(MapRenderer.MapInstance.class)
public class MixinMapInstance {

    private boolean is_player;

    @Redirect(method = "draw", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/saveddata/maps/MapDecoration;getImage()B"))
    public byte dctweaks$getImage(MapDecoration decoration) {
        is_player = decoration.getType() == MapDecoration.Type.PLAYER ||
                decoration.getType() == MapDecoration.Type.PLAYER_OFF_LIMITS || decoration.getType() == MapDecoration.Type.PLAYER_OFF_MAP;
        return is_player ? 0 : decoration.getImage();
    }

    @Redirect(method = "draw", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/vertex/PoseStack;mulPose(Lcom/mojang/math/Quaternion;)V"))
    public void dctweaks$mulPose(PoseStack poseStack, Quaternion quaternion) {
        if (is_player) {
            LocalPlayer player = Minecraft.getInstance().player;
            poseStack.mulPose(Vector3f.ZP.rotationDegrees(player.getYRot() + (player.getYRot() > 0 ? -8: 8)));
            is_player = false;
        } else poseStack.mulPose(quaternion);
    }

}
