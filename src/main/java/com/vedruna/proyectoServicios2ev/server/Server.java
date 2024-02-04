package com.vedruna.proyectoServicios2ev.server;
import java.io.IOException;
import java.net.*;
import java.util.*;

import javafx.scene.paint.Color;
import org.apache.logging.log4j.*;

/*

Al servidor le llega:
                        tag>payload
El servidor emite (el username lo obtiene del map):
                        tag>username:payload


*/

public class Server implements Runnable {

    private static final Logger LOGGER = LogManager.getLogger(Server.class);

    App app;

    Map<InetSocketAddress, String> users;
    DatagramSocket datagramSocket;
    byte[] inputBuffer = new byte[1024];
    DatagramPacket inputDatagramPacket = new DatagramPacket(inputBuffer, inputBuffer.length);

    boolean running;

    public Server(int localPort, App app) throws Exception {
        users = new HashMap<>();
        datagramSocket = new DatagramSocket(localPort);
        running = true;
        this.app = app;
    }

    @Override
    public void run() {
        LOGGER.debug("Server::start");
        try {
            while (running) {
                LOGGER.debug("receiving");
                datagramSocket.receive(inputDatagramPacket);
                InetSocketAddress remoteSocketAddress = (InetSocketAddress) inputDatagramPacket.getSocketAddress();
                LOGGER.debug("received {}", remoteSocketAddress);
                String username = users.get(remoteSocketAddress);
                if (username != null) {
                    processMessage(remoteSocketAddress, username);
                }
                else {
                    LOGGER.debug("new");
                    registerUser(remoteSocketAddress);
                    LOGGER.debug("registered");
                }
            }
        }
        catch (Exception ex) {
            System.out.println(ex);
        }
    }

    private void registerUser(InetSocketAddress remoteSocketAddress) throws IOException {
        String inputString = new String(inputDatagramPacket.getData(), 0, inputDatagramPacket.getLength(), "UTF-8");
        int firstSeparatorPosition  = inputString.indexOf(">");
        // String tag     = inputString.substring(0, separatorPosition);  Debe ser login
        String username = inputString.substring(firstSeparatorPosition+1);
        String message;
        if (users.values().contains(username)) {
            message = "error>Ya existe un usuario con ese nombre (" + username + ")";
            this.app.infoController.showInfo(Color.RED, Color.RED,
                                             username,
                                             remoteSocketAddress.getAddress().toString(),
                                             remoteSocketAddress.getPort(),
                                     "error");
        }
        else {
            users.put(remoteSocketAddress, username);
            message = "welcome>Bienvenido, " + username ;
            this.app.infoController.showInfo(Color.BLUE, Color.GREEN,
                                             username,
                                             remoteSocketAddress.getAddress().toString(),
                                             remoteSocketAddress.getPort(),
                                     "se ha conectado");
        }
        DatagramPacket outputDatagramPacket = new DatagramPacket(message.getBytes("UTF-8"), message.length(), remoteSocketAddress);
        datagramSocket.send(outputDatagramPacket);
    }

    private void processMessage(InetSocketAddress remoteSocketAddress, String username) throws IOException {
        String inputString = new String(inputDatagramPacket.getData(), 0, inputDatagramPacket.getLength(), "UTF-8");
        int separatorPosition  = inputString.indexOf(">");
        String tag     = inputString.substring(0, separatorPosition);
        String payload = inputString.substring(separatorPosition + 1);
        switch (tag) {
            case "chatting":
                String outputString = tag + ">" + username + ":" + payload;
                sendMessageToOtherUsers(outputString, remoteSocketAddress);
                if (payload.equals("STOP")) {
                    sendToAllUsers("stop>:Server stops");
                    running = false;
                }
                break;
            case "login":
                LOGGER.debug("Se intenta iniciar sesión");
                String message;
                if (username.equals(payload)) {
                    message = "welcome>Bienvenido de nuevo, " + username ;
                    this.app.infoController.showInfo(Color.BLUE, Color.GREEN,
                            username,
                            remoteSocketAddress.getAddress().toString(),
                            remoteSocketAddress.getPort(),
                            "se ha reconectado");
                }
                else {
                    message = "error>Intenta reconectar con un nombre diferente (" + username + "/" + payload + ")";
                    this.app.infoController.showInfo(Color.RED, Color.RED,
                            username,
                            remoteSocketAddress.getAddress().toString(),
                            remoteSocketAddress.getPort(),
                            "error");
                }
                DatagramPacket outputDatagramPacket = new DatagramPacket(message.getBytes("UTF-8"), message.length(), remoteSocketAddress);
                datagramSocket.send(outputDatagramPacket);
                break;
        }
    }

    void sendToAllUsers(String message) throws IOException {
        byte[] messageBytes = message.getBytes("UTF-8");
        for (InetSocketAddress receiverSocketAddress : users.keySet()) {
            DatagramPacket outputDatagramPacket = new DatagramPacket(messageBytes, messageBytes.length, receiverSocketAddress);
            datagramSocket.send(outputDatagramPacket);
        }
    }

    void sendMessageToOtherUsers(String message, SocketAddress senderSocketAddress) throws IOException {
        byte[] messageBytes = message.getBytes("UTF-8");
        for (SocketAddress receiverSocketAddress : users.keySet()) {
            if (!senderSocketAddress.equals(receiverSocketAddress)) {
                DatagramPacket outputDatagramPacket = new DatagramPacket(messageBytes, messageBytes.length, receiverSocketAddress);
                datagramSocket.send(outputDatagramPacket);
            }
        }
    }
}