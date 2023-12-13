package com.afunproject.dawncraft.integration.journeymap.network;

import com.afunproject.dawncraft.integration.quests.custom.QuestEntity;
import com.afunproject.dawncraft.integration.quests.custom.quests.Quest;
import com.afunproject.dawncraft.integration.quests.custom.quests.QuestsRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.PacketListener;
import net.minecraft.network.chat.BaseComponent;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.Level;
import net.smileycorp.atlas.api.network.SimpleAbstractMessage;

public class AddWaypointMessage extends SimpleAbstractMessage {

	private BlockPos pos = null;
	private String structure = null;

	public AddWaypointMessage() {}

	public AddWaypointMessage(BlockPos pos, String structure) {
		this.pos = pos;
		this.structure = structure;
	}

	@Override
	public void write(FriendlyByteBuf buf) {
		if (pos != null) buf.writeBlockPos(pos);
		if (structure != null) buf.writeUtf(structure);
	}

	@Override
	public void read(FriendlyByteBuf buf) {
		pos = buf.readBlockPos();
		structure = buf.readUtf();
	}

	@Override
	public void handle(PacketListener listener) {}

	public BlockPos getPos() {
		return pos;
	}

	public String getStructure() {
		return new TranslatableComponent(structure).getString();
	}

}
