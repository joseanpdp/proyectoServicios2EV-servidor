package com.vedruna.proyectoservicios2ev.server;

import java.net.*;
import java.util.*;

// FALTA QUE CUANDO SE INTRODUCZA "STOP" SE PARE EL SERVIDOR Y SE
// ENVÍE UN MENSAJE A TODOS LOS USUARIOS CONECTADOS AL SERVIDOR

public class Server implements Runnable {

    final static int LOCAL_PORT = 5010;

    List<User> users;
    DatagramSocket datagramSocket;

    public static void main(String args[]) throws Exception {
        Server server = new Server(LOCAL_PORT);
        server.run();
    }

    public Server(int localPort) throws Exception {
        users = new ArrayList<>();
        datagramSocket = new DatagramSocket(LOCAL_PORT);

    }

    @Override
    public void run() {
        byte[] inputBuffer  = new byte[1024];
        try {
            while (true) { // CAMBIAR POR BANDERA
                // Preparamos el Datagrama de inserción
                DatagramPacket inputDatagramPacket = new DatagramPacket(inputBuffer,inputBuffer.length);
                // Esperamos que el datagrama de recepción se rellene
                datagramSocket.receive(inputDatagramPacket);
                // Capturamos la dirección y el puerto del emisor
                InetAddress remoteInetAddress = inputDatagramPacket.getAddress();
                int remotePort = inputDatagramPacket.getPort();
                // Comprobamos si la direccion y el puerto están ya registrados
                User user = searchUser(remoteInetAddress, remotePort);
                // Si no lo están, los registramos
                if (user == null) {
                    String username = new String(inputDatagramPacket.getData(),0,inputDatagramPacket.getLength(), "UTF-8");
                    user = new User(username, remoteInetAddress, remotePort);
                    users.add(user);
                    // PETICION DEL NOMBRE DE USUARIO PARA EVITAR CONFUSIONES
                    System.out.println("Se añadió el usuario: " + username);
                    String info = "[El servidor te da la bienvenida, " + username + "]";
                    DatagramPacket outputDatagramPacket = new DatagramPacket(info.getBytes("UTF-8"),
                            info.length(),
                            remoteInetAddress,
                            remotePort);
                    datagramSocket.send(outputDatagramPacket);
                }
                else {
                    String message = new String(inputDatagramPacket.getData(),0,inputDatagramPacket.getLength(), "UTF-8");
                    sendMessageToOtherUsers(message, user);
                }
            }
        }
        catch (Exception ex) {
            System.out.println(ex);
        }
    }


    public User searchUser(InetAddress remoteInetAddress, int remotePort) {
        for (User user : users) {
            if (user.getInetAddress().equals(remoteInetAddress) && user.getPort() == remotePort) {
                return user;
            }
        }
        return null;
    }

    public void sendMessageToOtherUsers(String message, User user) throws Exception { // CAMBIAR POR EXCEPCION MAS CONCRETA
        byte[] messageBytes = message.getBytes("UTF-8");
        for (User otherUser : users) {
            if (otherUser != user) {
                DatagramPacket outputDatagramPacket = new DatagramPacket(messageBytes,
                        messageBytes.length,
                        otherUser.getInetAddress(),
                        otherUser.getPort());
                datagramSocket.send(outputDatagramPacket);
            }
        }
    }
}
