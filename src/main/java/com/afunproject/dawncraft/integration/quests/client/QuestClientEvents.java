package com.afunproject.dawncraft.integration.quests.client;

import com.afunproject.dawncraft.client.ClientEventRegister;
import com.afunproject.dawncraft.client.entity.FallenRenderer;
import com.afunproject.dawncraft.client.entity.PlayerEntityRenderer;
import com.afunproject.dawncraft.integration.quests.client.screens.QuestScreen;
import com.afunproject.dawncraft.integration.quests.custom.QuestEntity;
import com.afunproject.dawncraft.integration.quests.custom.QuestType;
import com.afunproject.dawncraft.integration.quests.custom.entity.QuestEntities;
import com.afunproject.dawncraft.integration.quests.custom.entity.QuestEntityBase;
import com.afunproject.dawncraft.integration.quests.custom.quests.Quest;
import com.afunproject.dawncraft.integration.quests.network.OpenQuestMessage;
import com.feywild.quest_giver.util.RenderEnum;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import com.mojang.math.Matrix4f;
import net.mcreator.simplemobs.entity.KorokIntroEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Mob;
import net.minecraftforge.client.event.EntityRenderersEvent.RegisterRenderers;
import net.minecraftforge.client.event.RenderNameplateEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class QuestClientEvents {

	public static void init() {
		MinecraftForge.EVENT_BUS.register(QuestClientEvents.class);
		ClientEventRegister.RENDERER_REGISTERS.add(QuestClientEvents::registerEntityRenderers);
	}

	public static void registerEntityRenderers(RegisterRenderers event) {
		event.registerEntityRenderer(QuestEntities.FALLEN_ADVENTURER.get(), FallenRenderer::new);
		event.registerEntityRenderer(QuestEntities.QUEST_PLAYER.get(), PlayerEntityRenderer::new);
	}

	public static void openQuestGUI(OpenQuestMessage message) {
		Minecraft mc = Minecraft.getInstance();
		Mob mob = message.get(mc.level);
		QuestEntity entity = QuestEntity.safeCast(mob);
		Quest quest = message.getQuest();
		QuestType type = quest == null ? QuestType.ACKNOWLEDGE : quest.getQuestType(message.getPhase() == 0 ? entity.getQuestPhase() : message.getPhase());
		String text = message.getMessage() == null ? entity.getQuestText() : message.getMessage();
		mc.setScreen(new QuestScreen(mob, new TranslatableComponent(text, mc.player), type));
	}

	private static final double MAX_DISTANCE = 64.0;
	private static final int Y_POS = -18;
	private static final int X_POS = -8;

	@SubscribeEvent
	public static void onRenderNamePlate(RenderNameplateEvent event) {
		Minecraft mc = Minecraft.getInstance();
		if (event.getEntity() instanceof QuestEntityBase &!(event.getEntity() instanceof KorokIntroEntity)) {
			QuestEntityBase entity = (QuestEntityBase) event.getEntity();
			Quest quest = entity.getCurrentQuest();
			if (quest != null && quest.isQuestActive(entity, entity.getQuestPhase()) &! (mc.screen instanceof QuestScreen)) {
				PoseStack poseStack = event.getPoseStack();
				EntityRenderDispatcher dispatcher = mc.getEntityRenderDispatcher();
				double squareDistance = dispatcher.distanceToSqr(entity);
				double fadeDistance = 0.5 * MAX_DISTANCE;
				double opacityDistance = Mth.clamp(1.0 - ((Math.sqrt(squareDistance) - fadeDistance) / (MAX_DISTANCE - fadeDistance)), 0.0, 1.0);
				float markerHeight = entity.getBbHeight() + 0.5F;

				if (squareDistance > MAX_DISTANCE * MAX_DISTANCE) {
					return; //STOP RENDERING MARK IF TO FAR AWAY FROM ENTITY
				}

				poseStack.pushPose();
				poseStack.translate(0.0D, markerHeight, 0.0D);
				poseStack.mulPose(dispatcher.cameraOrientation());
				poseStack.scale(-0.025F, -0.025F, 0.025F);

				RenderSystem.enableBlend();
				RenderSystem.defaultBlendFunc();

				RenderSystem.enableDepthTest();

				RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, (float) opacityDistance);  // 1.0 is full

				renderMarker(RenderEnum.EXCLAMATION.getRender(), poseStack);

				RenderSystem.disableBlend();
				RenderSystem.disableDepthTest();

				poseStack.popPose();
			}
		}
	}

	private static void renderMarker(ResourceLocation resource, PoseStack poseStack){
		poseStack.pushPose();
		poseStack.scale(1f, 1f, 1.0F);
		renderIcon(resource, poseStack);
		poseStack.popPose();
	}

	private static void renderIcon(ResourceLocation resource, PoseStack poseStack)
	{
		Matrix4f matrix = poseStack.last().pose();

		Minecraft.getInstance().getTextureManager().getTexture(resource).setFilter(false, false);
		RenderSystem.setShaderTexture(0, resource);

		RenderSystem.setShader(GameRenderer:: getPositionTexShader);
		BufferBuilder bufferbuilder = Tesselator.getInstance().getBuilder();
		bufferbuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
		bufferbuilder.vertex(matrix, X_POS, Y_POS + 16,0).uv( 0, 1).endVertex();
		bufferbuilder.vertex(matrix, X_POS + 16, Y_POS + 16, 0).uv(1, 1).endVertex();
		bufferbuilder.vertex(matrix, X_POS + 16, Y_POS,	0).uv(1, 0).endVertex();
		bufferbuilder.vertex(matrix, X_POS, Y_POS, 0).uv(0, 0).endVertex();
		bufferbuilder.end();
		BufferUploader.end(bufferbuilder);
	}


}
