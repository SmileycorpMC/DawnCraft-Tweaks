package com.afunproject.dawncraft.integration.journeymap.network;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.PacketListener;

public class AddWaypointMessage extends WaypointMessage {

	private BlockPos pos = null;

	public AddWaypointMessage() {}

	public AddWaypointMessage(BlockPos pos, String structure) {
		super(structure);
		this.pos = pos;
	}

	@Override
	public void write(FriendlyByteBuf buf) {
		super.write(buf);
		if (pos != null) buf.writeBlockPos(pos);
	}

	@Override
	public void read(FriendlyByteBuf buf) {
		super.read(buf);
		pos = buf.readBlockPos();
	}

	@Override
	public void handle(PacketListener listener) {}

	public BlockPos getPos() {
		return pos;
	}

}
