package com.afunproject.dawncraft.integration.journeymap.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.PacketListener;
import net.minecraft.network.chat.TranslatableComponent;

public class RemoveWaypointMessage extends WaypointMessage {
	
	private String structure = null;

	public RemoveWaypointMessage() {}

	public RemoveWaypointMessage(String structure) {
		super(structure);
	}

	@Override
	public void write(FriendlyByteBuf buf) {
		if (structure != null) buf.writeUtf(structure);
	}

	@Override
	public void read(FriendlyByteBuf buf) {
		structure = buf.readUtf();
	}

	@Override
	public void handle(PacketListener listener) {}

	public String getStructure() {
		return new TranslatableComponent(structure).getString();
	}

}
