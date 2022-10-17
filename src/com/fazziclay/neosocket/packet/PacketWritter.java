package com.fazziclay.neosocket.packet;

import com.fazziclay.neosocket.BytePacketWritter;
import com.fazziclay.neosocket.Util;

import java.io.IOException;

public class PacketWritter {
    private final BytePacketWritter bWritter;
    private final PacketsRegistry registry;

    public PacketWritter(BytePacketWritter bWritter, PacketsRegistry registry) {
        this.bWritter = bWritter;
        this.registry = registry;
    }

    public void write(Packet packet) throws IOException {
        PacketsRegistry.Info info = registry.findByClass(packet.getClass());
        PacketConverter converter = info.getConverter();

        byte[] packetData = converter.encode(packet);
        byte[] packetId = Util.intToBytes(info.getId());

        byte[] result = new byte[packetData.length + 4];

        System.arraycopy(packetId, 0, result, 0, packetId.length);
        System.arraycopy(packetData, 0, result, 4, packetData.length);

        bWritter.write(result);
    }

    public void flush() throws IOException {
        bWritter.flush();
    }
}
