package com.fazziclay.neosocket.packet;

import java.util.ArrayList;
import java.util.List;

public class PacketsRegistry {
    private final List<Info> registry = new ArrayList<>();

    public PacketsRegistry(Info[] r) {
        for (Info info : r) {
            for (Info e : registry) {
                if (info.id == e.id) throw new RuntimeException("Duplicate packetId: " + info.clazz + ": " + info.id + " and " + e.clazz + ": " + e.id);
            }
            registry.add(info);
        }
    }

    public Info findById(int id) {
        for (Info info : registry) {
            if (info.id == id) {
                return info;
            }
        }
        return null;
    }

    public Info findByClass(Class<? extends Packet> clazz) {
        for (Info info : registry) {
            if (info.clazz == clazz) {
                return info;
            }
        }
        return null;
    }

    public static class Info {
        private final int id;
        private final Class<? extends Packet> clazz;
        private final PacketConverter converter;

        public Info(int id, Class<? extends Packet> clazz, PacketConverter converter) {
            this.id = id;
            this.clazz = clazz;
            this.converter = converter;
        }

        public int getId() {
            return id;
        }

        public Class<? extends Packet> getClazz() {
            return clazz;
        }

        public PacketConverter getConverter() {
            return converter;
        }
    }
}
