package com.fazziclay.neosocket.packet;

public abstract class PacketConverter {
    public abstract Packet decode(byte[] data);
    public abstract byte[] encode(Packet packet);
}
