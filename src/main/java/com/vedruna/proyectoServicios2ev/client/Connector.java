package com.vedruna.proyectoServicios2ev.client;

import java.io.*;
import java.net.*;

import org.apache.logging.log4j.*;

public class Connector implements Runnable {

    private static final Logger LOGGER = LogManager.getLogger(Connector.class);

    boolean running;

    Thread receiverThread;
    DatagramSocket datagramSocket;
    SocketAddress remoteSocketAddress;

    App app;

    Connector(App app) {
        super();
        this.app = app;
    }

    void open(int localPort) throws SocketException {
        running = true;
        if ( datagramSocket != null) {
            datagramSocket.close();
        } 
        datagramSocket = new DatagramSocket(localPort);
        datagramSocket.setSoTimeout(5000);
    }

    void connect(SocketAddress remoteSocketAddress, String username) throws IOException {
        this.remoteSocketAddress = remoteSocketAddress;
        send("login>"+username);
    }

    void send(String text) throws IOException {
        byte[] outputBuffer = text.getBytes("UTF-8");
        DatagramPacket outputDatagramPacket = new DatagramPacket( outputBuffer, outputBuffer.length, remoteSocketAddress);
        datagramSocket.send(outputDatagramPacket);
    }

    String receive() throws IOException {
        byte[] inputBuffer = new byte[1024];
        DatagramPacket inputDatagramPacket = new DatagramPacket(inputBuffer, inputBuffer.length);
        datagramSocket.receive(inputDatagramPacket);
        String text = new String(inputDatagramPacket.getData(), 0, inputDatagramPacket.getLength(), "UTF-8");
        return text;
    }

    void start() {
        receiverThread = new Thread(this);
        receiverThread.start();
    }

    void stop() {
        running = false;
        receiverThread   = null;
        datagramSocket.close();
        datagramSocket = null;
    }

    @Override
    public void run() {
        try {
            while (running) {
                String text = receive();
                app.receive(text);
            }
        }
        catch (Exception ex) {
            System.out.println(ex);
        }
    }
}

