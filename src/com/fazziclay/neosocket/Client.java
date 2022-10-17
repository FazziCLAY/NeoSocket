package com.fazziclay.neosocket;

import com.fazziclay.neosocket.packet.Packet;
import com.fazziclay.neosocket.packet.PacketParser;
import com.fazziclay.neosocket.packet.PacketWritter;
import com.fazziclay.neosocket.packet.PacketsRegistry;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.Socket;

public class Client {
    private final int side; // 0 - client 1 - server
    private String host;
    private int port;
    private Socket socket;
    private final PacketsRegistry registry;
    private final PacketHandler handler;

    private PacketParser parser;
    private PacketWritter writter;
    private boolean shutdown = false;

    public Client(Socket socket, PacketsRegistry registry, PacketHandlerCreator handler) {
        this.side = 1;
        this.socket = socket;
        this.registry = registry;
        this.handler = handler.create(this);
    }

    public Client(String host, int port, PacketsRegistry registry, PacketHandler handler) {
        this.side = 0;
        this.host = host;
        this.port = port;
        this.registry = registry;
        this.handler = handler;
    }

    public void run() throws IOException {
        if (side == 0) socket = new Socket(this.host, this.port);
        parser = new PacketParser(new BytePacketParser(new BufferedInputStream(socket.getInputStream())), registry);
        writter = new PacketWritter(new BytePacketWritter(new BufferedOutputStream(socket.getOutputStream())), registry);

        handler.setup(this);

        try {
            loop();
        } catch (Exception e) {
            handler.fatalException(this, e);
        }

        handler.preDisconnect(this);

        if (!socket.isClosed()) {
            socket.close();
        }
    }

    public void send(Packet d) throws IOException {
        writter.write(d);
        writter.flush();
    }

    private void loop() throws IOException {
        while (!socket.isClosed() && socket.isConnected() && !shutdown) {
            Packet packet = parser.parse();
            if (packet != null) {
                handler.received(this, packet);
            }
        }
    }

    public void shutdown() {
        this.shutdown = true;
    }

    public String getAddress() {
        return socket.getInetAddress().getHostAddress();
    }

    public int getPort() {
        return socket.getPort();
    }
}
