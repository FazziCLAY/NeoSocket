package com.fazziclay.neosocket.test;

import com.fazziclay.neosocket.packet.Packet;
import com.fazziclay.neosocket.packet.PacketConverter;

import java.nio.charset.StandardCharsets;

public class TestMessagePacket implements Packet {
    public static final PacketConverter CONVERTER = new PacketConverter() {
        @Override
        public Packet decode(byte[] data) {
            return new TestMessagePacket(data);
        }

        @Override
        public byte[] encode(Packet packet) {
            return ((TestMessagePacket)packet).message;
        }
    };

    private byte[] message;

    public TestMessagePacket(byte[] message) {
        this.message = message;
    }

    public byte[] getMessage() {
        return message;
    }
}
