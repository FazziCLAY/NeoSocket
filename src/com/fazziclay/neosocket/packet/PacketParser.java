package com.fazziclay.neosocket.packet;

import com.fazziclay.neosocket.BytePacketParser;
import com.fazziclay.neosocket.Util;

import java.io.IOException;

public class PacketParser {
    private final BytePacketParser bParser;
    private final PacketsRegistry registry;

    public PacketParser(BytePacketParser bParser, PacketsRegistry registry) {
        this.bParser = bParser;
        this.registry = registry;
    }

    /**
     * Parse packet null is packet none
     * @return parsed packet or null if packets none
     * @throws IOException default socket exception
     */
    public Packet parse() throws IOException {
        byte[] bytePacket = bParser.parse();
        if (bytePacket == null) {
            return null;
        }

        if (bytePacket.length < 4) {
            throw new RuntimeException("Packet not contain packetId! length < 4: " + bytePacket.length);
        }


        int id = Util.bytesToInt(new byte[]{bytePacket[0], bytePacket[1], bytePacket[2], bytePacket[3]});
        byte[] packetData = new byte[bytePacket.length - 4];

        System.arraycopy(bytePacket, 4, packetData, 0, bytePacket.length - 4);

        PacketsRegistry.Info info = registry.findById(id);
        if (info == null) {
            throw new RuntimeException("Packet not found: " + id);
        }
        PacketConverter converter = info.getConverter();
        return converter.decode(packetData);
    }
}
