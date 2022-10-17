package com.fazziclay.neosocket;

import com.fazziclay.neosocket.packet.PacketsRegistry;

import java.io.IOException;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    private final int port;
    private final PacketsRegistry registry;
    private boolean isBusy = true;
    private final PacketHandlerCreator handlerCreator;
    private final PrintStream printStream;


    public Server(int port, PacketsRegistry registry, PacketHandlerCreator handlerCreator, PrintStream printStream) {
        this.port = port;
        this.registry = registry;
        this.handlerCreator = handlerCreator;
        this.printStream = printStream;
    }

    public void run() {
        try {
            ServerSocket serverSocket = new ServerSocket(port);
            if (printStream != null) printStream.println("Server started");

            while (!serverSocket.isClosed()) {
                Socket socket = serverSocket.accept();
                if (printStream != null) printStream.println("Client connected! " + socket.getInetAddress().getHostAddress());
                newClient(socket);
            }

        } catch (IOException e) {
            if (printStream != null) e.printStackTrace(printStream);
        }
        isBusy = false;
    }

    private void newClient(Socket socket) {
        new Thread(() -> {
            try {
                new Client(socket, registry, handlerCreator).run();
            } catch (IOException e) {
                if (printStream != null) e.printStackTrace(printStream);
            }
        }).start();
    }

    public boolean isBusy() {
        return isBusy;
    }

    public void tick() {
    }
}
