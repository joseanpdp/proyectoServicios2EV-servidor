package com.vedruna.proyectoservicios2ev.client;

import javafx.scene.paint.Color;

import java.io.*;
import java.net.*;

public class Client {
    final static int REMOTE_PORT = 5010;

    App app;

    DatagramSocket datagramSocket;

    InetAddress remoteInetAddress;
    int remotePort;

    public Client(int localPort, InetAddress remoteInetAddresss, int remotePort) throws Exception {
        this.remoteInetAddress = remoteInetAddresss;
        this.remotePort = remotePort;
        this.datagramSocket = new DatagramSocket(localPort);

    }

    public void setApp(App app) {
        this.app = app;
    }

    public void login() {
        try {
            byte[] outputBuffer =app.username.getBytes("UTF-8");
            DatagramPacket outputDatagramPacket = new DatagramPacket(outputBuffer, outputBuffer.length
                    , remoteInetAddress
                    , remotePort);
            datagramSocket.send(outputDatagramPacket);
            System.out.println(app.username);
        }
        catch (IOException ioex) {
            ioex.printStackTrace();
        }
        finally {

        }
    }

    public void sendMessage(String message) {
        try {
            message = app.username + ":" + message;
            byte[] outputBuffer = message.getBytes("UTF-8");
            DatagramPacket outputDatagramPacket = new DatagramPacket(outputBuffer, outputBuffer.length
                    , remoteInetAddress
                    , remotePort);
            datagramSocket.send(outputDatagramPacket);
System.out.println(message);
        }
        catch (IOException ioex) {
            ioex.printStackTrace();
        }
        finally {

        }
    }

    public void start() {
        Thread receiverThread = new Thread(new Receiver(this));
        receiverThread.start();
    }

}

class Receiver implements Runnable {

    Client client;

    public Receiver(Client client) {
        this.client = client;
    }

    @Override
    public void run() {
        try {
            byte[] inputBuffer = new byte[1024];
            DatagramPacket inputDatagramPacket = new DatagramPacket(inputBuffer, inputBuffer.length);

            client.datagramSocket.receive(inputDatagramPacket);
            String message = new String(inputDatagramPacket.getData(), 0, inputDatagramPacket.getLength(), "UTF-8");
            client.app.chatController.showServerInfo(message);

            while (true) {
                client.datagramSocket.receive(inputDatagramPacket);
                message = new String(inputDatagramPacket.getData(), 0, inputDatagramPacket.getLength(), "UTF-8");

                int separatorPosition = message.indexOf(":");
                String username = message.substring(0, separatorPosition);
                message = message.substring(separatorPosition);

                client.app.chatController.showUserMessage(Color.GREEN, username, message);
            }
        }
        catch (Exception ex) {
            System.out.println(ex);
        }
    }
}
