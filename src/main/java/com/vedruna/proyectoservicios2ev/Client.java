package com.vedruna.proyectoservicios2ev;

import java.io.*;
import java.net.*;

public class Client {

    private ChatController controller = new ChatController();
    final static int LOCAL_PORT  = 6010;
    final static int REMOTE_PORT = 5010;

    public static void main(String args[]) throws Exception { // CAMBIAR POR UNA EXCEPCION MAS CONCRETA
        /*
        InetAddress remoteInetAddress = InetAddress.getByName(args[0]);
        new Client(LOCAL_PORT, remoteInetAddress, REMOTE_PORT);
         */
        InetAddress remoteInetAddress = InetAddress.getByName(args[0]);
        int localPort = Integer.parseInt(args[1]);
        new Client(localPort, remoteInetAddress, REMOTE_PORT);

    }

    ////////////////////////////////////////////////////////////////////////////////////

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
        Thread senderThread = new Thread(new Sender(this));

        receiverThread.start();
        senderThread.start();

    }

    public void alfa(String message) {
        controller.actualizarLabel(message);
    }

    public String beta() throws IOException {
        return controller.obtenerTextoDesdeInterfaz();
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
                client.alfa(message);
            }
        }
        catch (Exception ex) {
            System.out.println(ex);
        }
    }
}

class Sender implements Runnable {

    Client client;

    public Sender(Client client) {
        this.client = client;
    }

    @Override
    public void run() {
        try {
            while (true) {

                byte[] outputBuffer = client.beta().getBytes();
                DatagramPacket outputDatagramPacket = new DatagramPacket( outputBuffer, outputBuffer.length
                        , client.remoteInetAddress
                        , client.remotePort);
                client.datagramSocket.send(outputDatagramPacket);
            }
        }
        catch (Exception ex) {
            System.out.println(ex);
        }
    }
}
