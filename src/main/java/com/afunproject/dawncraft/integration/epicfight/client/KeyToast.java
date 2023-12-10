package com.afunproject.dawncraft.integration.epicfight.client;

import com.afunproject.dawncraft.Constants;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.toasts.Toast;
import net.minecraft.client.gui.components.toasts.ToastComponent;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import java.util.Locale;

public class KeyToast implements Toast {

    private static final ResourceLocation TEXTURE = Constants.loc("textures/gui/dc_toast.png");

    private final String message;
    private final KeyMapping mapping;
    private boolean pressed;

    public KeyToast(String message, KeyMapping mapping) {
        this.message = message;
        this.mapping = mapping;
    }

    @Override
    public Visibility render(PoseStack poseStack, ToastComponent component, long time) {
        if (!pressed && mapping.isDown()) pressed = true;
        Minecraft mc = component.getMinecraft();
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderTexture(0, TEXTURE);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        component.blit(poseStack, 0, 0, 0, 0, width(), height());
        mc.getItemRenderer().renderAndDecorateFakeItem(new ItemStack(Items.IRON_SWORD), 5, 8);
        poseStack.pushPose();
        poseStack.scale(0.75f, 0.75f, 0.75f);
        mc.font.draw(poseStack, new TranslatableComponent(message + ".title",
                new TextComponent(mapping.getTranslatedKeyMessage().getString().toUpperCase(Locale.US))), 32.0F, 7.0F, 0x9E0CD2);
        mc.font.draw(poseStack,  new TranslatableComponent(message + ".text0"), 32.0F, 18.0F, -1);
        mc.font.draw(poseStack,  new TranslatableComponent(message + ".text1"), 32.0F, 29.0F, -1);
        poseStack.scale(1, 1, 1);
        poseStack.popPose();
        return pressed ? Visibility.HIDE : Visibility.SHOW;
    }

}
