package com.vedruna.proyectoservicios2ev;

import java.io.*;
import java.net.*;

public class Client {

    final static int LOCAL_PORT  = 6010;
    final static int REMOTE_PORT = 5010;

    public static void main(String args[]) throws Exception {
        /*
        InetAddress remoteInetAddress = InetAddress.getByName(args[0]);
        new Client(LOCAL_PORT, remoteInetAddress, REMOTE_PORT);
         */
        InetAddress remoteInetAddress = InetAddress.getByName(args[0]);
        int localPort = Integer.parseInt(args[1]);
        new Client(localPort, remoteInetAddress, REMOTE_PORT);

    }

    ////////////////////////////////////////////////////////////////////////////////////

    App app;
    BufferedReader inputFromUser;
    DatagramSocket datagramSocket;

    InetAddress remoteInetAddress;
    int remotePort;

    public Client(int localPort, InetAddress remoteInetAddresss, int remotePort) throws Exception {
        this.remoteInetAddress = remoteInetAddresss;
        this.remotePort = remotePort;

        inputFromUser  = new BufferedReader(new InputStreamReader(System.in));
        datagramSocket = new DatagramSocket(localPort);

        Thread receiverThread = new Thread(new Receiver(this));

        receiverThread.start();
    }

    public void setApp(App app) {
        this.app = app;
    }

    public void sendMessage(String sentence) throws IOException {
        byte[] outputBuffer = sentence.getBytes();
        DatagramPacket outputDatagramPacket = new DatagramPacket( outputBuffer, outputBuffer.length
                , remoteInetAddress
                , remotePort);
        datagramSocket.send(outputDatagramPacket);
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
            while (true) {
                byte[] inputBuffer = new byte[1024];
                DatagramPacket inputDatagramPacket = new DatagramPacket(inputBuffer, inputBuffer.length);
                client.datagramSocket.receive(inputDatagramPacket);
                String message = new String(inputDatagramPacket.getData(), 0, inputDatagramPacket.getLength());
                client.app.chatController.showMessage(message);
            }
        }
        catch (Exception ex) {
            System.out.println(ex);
        }
    }
}
