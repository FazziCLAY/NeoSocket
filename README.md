# NeoSocket
Simple Library for network data transmission via packets

# Byte structure
HEX: [00 00 00 05] [00 00 00 00] [FF]

DEX: [5] [0] [255]

* **First** - Length (int: 4 bytes)
`(Length = 4 + **PacketID**.length + **Data**.length)`

* **Center** - PacketID (int: 4 bytes)

* **Last** - Data (any size)

# Overview & Statup
## Server
```java
PacketsRegistry registry = new TestRegistry(); // Packets list
PacketHandlerCreator clientCreator = new PacketHandlerCreator() {
    @Override
    public PacketHandler create(Client client) {
        return return new PacketHandler() {
            @Override
            public void received(Client client, Packet packet) {
                // Packet received from client
            }

            @Override
            public void setup(Client client) {
                // Client connected
                client.send(new TestMessagePacket("Hello from server!"));
            }

            @Override
            public void preDisconnect(Client client) {
                // Client disconnecting...
            }

            @Override
            public void fatalException(Client client, Exception e) {
                // Exception...
            }
        };
    }
};
//                        PORT   Registry  ClientCrator   Logs
Server server = new Server(5433, registry, clientCreator, System.out);
new Thread(server::run).start(); // Dont forget server.run()!
```

## Clients
```java
PacketHandler handler = new PacketHandler() {
    @Override
    public void received(Client client, Packet packet) {
        System.out.println("received from server: " + packet);
    }
    
    @Override
    public void setup(Client client) {
        System.out.println("Client setup");
    }
    
    @Override
    public void preDisconnect(Client client) {
        System.out.println("Disconneded.");
    }
    
    @Override
    public void fatalException(Client client, Exception e) {
        System.out.println("fatal error" + e);
    }
};

Client client = new Client("localhost", 5433, new TestRegistry(), handler);
client.run();
```


# Packet Structure
## Simple text packet
A packet containing only a text message
```java
import com.fazziclay.neosocket.packet.Packet;
import com.fazziclay.neosocket.packet.PacketConverter;

public class TestMessagePacket implements Packet {
    public static final PacketConverter CONVERTER = new PacketConverter() {
        @Override
        public Packet decode(byte[] data) {
            return new TestMessagePacket(new String(data, StandardCharsets.UTF_8));
        }

        @Override
        public byte[] encode(Packet packet) {
            return ((TestMessagePacket)packet).message.getBytes(StandardCharsets.UTF_8);
        }
    };

    private String message;

    public TestMessagePacket(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
```


# Packet Registry
On both sides, the ID and CONVERTER must be the same in order for the server and the client to understand each other
```java
import com.fazziclay.neosocket.packet.PacketsRegistry;

public class TestRegistry extends PacketsRegistry {
    public TestRegistry() {
        super(new Info[]{
                //      ID  Class                    Converter
                new Info(1, TestMessagePacket.class, TestMessagePacket.CONVERTER)
        });
    }
}
```