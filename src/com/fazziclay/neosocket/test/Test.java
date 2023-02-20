package com.fazziclay.neosocket.test;

import com.fazziclay.neosocket.Client;
import com.fazziclay.neosocket.PacketHandler;
import com.fazziclay.neosocket.PacketHandlerCreator;
import com.fazziclay.neosocket.Server;
import com.fazziclay.neosocket.packet.Packet;
import com.fazziclay.neosocket.packet.PacketsRegistry;

import java.io.IOException;
import java.util.Arrays;

public class Test {
    public static void main(String[] args) throws InterruptedException, IOException {
        PacketsRegistry registry = new TestRegistry();
        PacketHandlerCreator clientCreator = new PacketHandlerCreator() {
            @Override
            public PacketHandler create(Client client) {
                return new PacketHandler() {
                    @Override
                    public void received(Client client, Packet packet) {

                    }

                    @Override
                    public void setup(Client client) {
                        try {
                            byte[] b = new byte[1024*50];
                            Arrays.fill(b, (byte) -11);
                            client.send(new TestMessagePacket(b));
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }

                    @Override
                    public void preDisconnect(Client client) {

                    }

                    @Override
                    public void fatalException(Client client, Exception e) {

                    }
                };
            }
        };

        Server server = new Server(5433, registry, clientCreator, System.out);
        new Thread(server::run).start();
        Thread.sleep(1000);

        PacketHandler handler = new PacketHandler() {
            @Override
            public void received(Client client, Packet packet) {
                System.out.println("received "+ packet);

                if (packet instanceof TestMessagePacket) {
                    TestMessagePacket m = (TestMessagePacket) packet;
                    System.out.println(m.getMessage()[1] + "" + m.getMessage()[6554]);
                }
            }

            @Override
            public void setup(Client client) {
                System.out.println("setup");
            }

            @Override
            public void preDisconnect(Client client) {
                System.out.println("predisconnect");

            }

            @Override
            public void fatalException(Client client, Exception e) {
                System.out.println("fatal error" +  e);

            }
        };
        Client client = new Client("localhost", 5433, new TestRegistry(), handler);
        client.run();
    }
}
