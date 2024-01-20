package com.vedruna.proyectoservicios2ev;

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
            while (true) {
                // Preparamos el Datagrama de inserción
                DatagramPacket inputDatagramPacket = new DatagramPacket(inputBuffer,inputBuffer.length);
                // Esperamos que el datagrama de recepción se rellene
                datagramSocket.receive(inputDatagramPacket);
                // Capturamos la dirección y el puerto del emisor
                InetAddress remoteInetAddress = inputDatagramPacket.getAddress();
                int remotePort = inputDatagramPacket.getPort();
                // Extraemos el mensaje
                String message = new String(inputDatagramPacket.getData(),0,inputDatagramPacket.getLength());

                // Comprobamos si la direccion y el puerto están ya registrados
                User user = searchUser(remoteInetAddress, remotePort);
                // Si no lo están, los registramos
                if (user == null) {
                    user = new User(message, remoteInetAddress, remotePort);
                    users.add(user);
                    System.out.println("Se añadió el usuario: " + message);
                    message = "[El servidor te da la bienvenida, " + message + "]";
                    DatagramPacket outputDatagramPacket = new DatagramPacket(message.getBytes(),
                            message.length(),
                            remoteInetAddress,
                            remotePort);
                    datagramSocket.send(outputDatagramPacket);
                }
                // Si están y el mensaje comienza por iam: cambiamos el nombre del usuario e
                // informamos al resto de usuarios
                else if (message.startsWith("iam:")) {
                    String name = message.substring(4);
                    message = "[" + user.getName() + " ahora se llama " + name + "]";
                    sendMessageToOtherUsers(message, user);
                    user.setName(name);
                }
                // Si no, simplemente enviamos el mensaje
                else {
                    message = user.getName() + ": " + message;
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

    public void sendMessageToOtherUsers(String message, User user) throws Exception {
        byte[] messageBytes = message.getBytes();
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
