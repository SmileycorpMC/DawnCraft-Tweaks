package com.afunproject.dawncraft.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.PacketListener;
import net.smileycorp.atlas.api.network.SimpleAbstractMessage;

public class ToastMessage extends SimpleAbstractMessage {

    private byte b;

    public ToastMessage() {}

    public ToastMessage(byte b) {
        this.b = b;
    }

    @Override
    public void read(FriendlyByteBuf buf) {
        b = buf.readByte();
    }

    @Override
    public void write(FriendlyByteBuf buf) {
        buf.writeByte(b);
    }

    @Override
    public void handle(PacketListener p_131342_) {}

    public byte get() {
        return b;
    }

}
