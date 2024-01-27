package com.vedruna.proyectoServicios2ev.server;
import java.io.IOException;
import java.net.*;
import java.util.*;

import org.apache.logging.log4j.*;

/*
                        tag>username:payload
                           |        |
                           |        |
                           |        |
*/

public class Server  {

    private static final Logger LOGGER = LogManager.getLogger(Server.class);

    public static void main(String args[]) throws Exception {
        Server server = new Server(LOCAL_PORT);
        server.start();
    }

    final static int LOCAL_PORT = 5010;

    Map<SocketAddress, String> users;
    DatagramSocket datagramSocket;
    byte[] inputBuffer = new byte[1024];
    DatagramPacket inputDatagramPacket = new DatagramPacket(inputBuffer, inputBuffer.length);

    boolean running;

    public Server(int localPort) throws Exception {
        users = new HashMap<>();
        datagramSocket = new DatagramSocket(localPort);
        running = true;
    }
    public void start() {
        LOGGER.debug("Server::start");
        try {
            while (running) {
                LOGGER.debug("receiving");
                datagramSocket.receive(inputDatagramPacket);
                SocketAddress remoteSocketAddress = inputDatagramPacket.getSocketAddress();
                LOGGER.debug("received {}", remoteSocketAddress);
                if (!users.containsKey(remoteSocketAddress)) {
                    LOGGER.debug("new");
                    registerUser(remoteSocketAddress);
                    LOGGER.debug("registered");
                }
                else {
                    processMessage(remoteSocketAddress);
                }
            }
        }
        catch (Exception ex) {
            System.out.println(ex);
        }
    }

    private void registerUser(SocketAddress remoteSocketAddress) throws IOException {
        String inputString = new String(inputDatagramPacket.getData(), 0, inputDatagramPacket.getLength(), "UTF-8");
        int firstSeparatorPosition  = inputString.indexOf(">");
        // String tag     = inputString.substring(0, separatorPosition);  Debe ser login
        String username = inputString.substring(firstSeparatorPosition+1);
        String message;
        if (users.values().contains(username)) {
            message = "error>Ya existe un usuario con ese nombre (" + username + ")";
        }
        else {
            users.put(remoteSocketAddress, username);
            message = "welcome>Bienvenido, " + username ;
        }
        DatagramPacket outputDatagramPacket = new DatagramPacket(message.getBytes("UTF-8"), message.length(), remoteSocketAddress);
        datagramSocket.send(outputDatagramPacket);
    }

    private void processMessage(SocketAddress remoteSocketAddress) throws IOException {
        String inputString = new String(inputDatagramPacket.getData(), 0, inputDatagramPacket.getLength(), "UTF-8");
        int firstSeparatorPosition  = inputString.indexOf(">");
        int secondSeparatorPosition = inputString.indexOf(":");
        String tag     = inputString.substring(0, firstSeparatorPosition);
        // String username = inputString.substring(firstSeparatorPosition+1,secondSeparatorPosition); MUST BE
        String payload = inputString.substring(secondSeparatorPosition + 1);
        switch (tag) {
            case "chatting":
                // Se transmite el mensaje tal como llego: chatting>username:payload
                sendMessageToOtherUsers(inputString, remoteSocketAddress);
                if (payload.equals("STOP")) {
                    sendToAllUsers("stop>:Server stops");
                    running = false;
                }
                break;
        }
    }

    void sendToAllUsers(String message) throws IOException {
        byte[] messageBytes = message.getBytes("UTF-8");
        for (SocketAddress receiverSocketAddress : users.keySet()) {
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