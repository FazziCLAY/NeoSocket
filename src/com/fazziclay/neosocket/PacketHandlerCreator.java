package com.fazziclay.neosocket;

public interface PacketHandlerCreator {
    PacketHandler create(Client client);
}
