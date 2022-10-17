package com.fazziclay.neosocket;

import com.fazziclay.neosocket.packet.Packet;

public interface PacketHandler {
    void received(Client client, Packet packet);
    void setup(Client client);
    void preDisconnect(Client client);
    void fatalException(Client client, Exception e);
}
