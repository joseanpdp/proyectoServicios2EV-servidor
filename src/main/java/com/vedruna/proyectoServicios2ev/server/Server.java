package com.vedruna.proyectoServicios2ev.server;
import java.io.IOException;
import java.net.*;
import java.util.*;

import javafx.scene.paint.Color;
import org.apache.logging.log4j.*;

/*
<<<<<<< HEAD
=======

Al servidor le llega:
                        tag>payload
El servidor emite (el username lo obtiene del map):
                        tag>username:payload


*/
>>>>>>> 2b8701596ff9c9dfdc8926161b5c6ebcc2eb4ba8

Al servidor le llega:
                        tag>payload
El servidor emite (el username lo obtiene del map):
                        tag>username:payload


*/
/**
 * Clase que representa el servidor de la aplicación.
 * Implementa la interfaz `Runnable` para ejecutarse en un hilo separado.
 */
public class Server implements Runnable {

    /** Instancia de la aplicación principal. */
    App app;

    private static final Logger LOGGER = LogManager.getLogger(Server.class);


<<<<<<< HEAD
    /** Mapa que asocia direcciones de socket con nombres de usuario. */
    Map<InetSocketAddress, String> users;

    /** Socket de datagrama para la comunicación del servidor. */
=======
    Map<InetSocketAddress, String> users;
>>>>>>> 2b8701596ff9c9dfdc8926161b5c6ebcc2eb4ba8
    DatagramSocket datagramSocket;

    /** Búfer de entrada para recibir datos. */
    byte[] inputBuffer = new byte[1024];

    /** Paquete de datagrama para almacenar datos de entrada. */
    DatagramPacket inputDatagramPacket = new DatagramPacket(inputBuffer, inputBuffer.length);

    /** Indicador de ejecución del servidor. */
    boolean running;

    /**
     * Constructor de la clase Server.
     *
     * @param localPort Puerto local para la comunicación del servidor.
     * @param app       Instancia de la aplicación principal.
     * @throws Exception Si ocurre un error durante la inicialización del servidor.
     */
    public Server(int localPort, App app) throws Exception {
        users = new HashMap<>();
        datagramSocket = new DatagramSocket(localPort);
        running = true;
        this.app = app;
    }

    /**
     * Método principal que se ejecuta cuando se inicia el hilo del servidor.
     */
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
<<<<<<< HEAD
                } else {
=======
                }
                else {
>>>>>>> 2b8701596ff9c9dfdc8926161b5c6ebcc2eb4ba8
                    LOGGER.debug("new");
                    registerUser(remoteSocketAddress);
                    LOGGER.debug("registered");
                }
            }
        } catch (Exception ex) {
            System.out.println(ex);
        }
    }

<<<<<<< HEAD
    /**
     * Registra un nuevo usuario en el servidor.
     *
     * @param remoteSocketAddress Dirección del nuevo usuario.
     * @throws IOException Si hay un error al enviar mensajes.
     */
=======
>>>>>>> 2b8701596ff9c9dfdc8926161b5c6ebcc2eb4ba8
    private void registerUser(InetSocketAddress remoteSocketAddress) throws IOException {
        String inputString = new String(inputDatagramPacket.getData(), 0, inputDatagramPacket.getLength(), "UTF-8");
        int firstSeparatorPosition = inputString.indexOf(">");
        String username = inputString.substring(firstSeparatorPosition + 1);
        String message;
        if (users.values().contains(username)) {
            message = "error>Ya existe un usuario con ese nombre (" + username + ")";
            this.app.infoController.showInfo(Color.RED, Color.RED,
<<<<<<< HEAD
                    username,
                    remoteSocketAddress.getAddress().toString(),
                    remoteSocketAddress.getPort(),
                    "error");
        } else {
            users.put(remoteSocketAddress, username);
            message = "welcome>Bienvenido, " + username ;
            this.app.infoController.showInfo(Color.BLUE, Color.GREEN,
                    username,
                    remoteSocketAddress.getAddress().toString(),
                    remoteSocketAddress.getPort(),
                    "se ha conectado");
=======
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
>>>>>>> 2b8701596ff9c9dfdc8926161b5c6ebcc2eb4ba8
        }
        DatagramPacket outputDatagramPacket = new DatagramPacket(message.getBytes("UTF-8"), message.length(), remoteSocketAddress);
        datagramSocket.send(outputDatagramPacket);
    }

<<<<<<< HEAD
    /**
     * Procesa un mensaje recibido por el servidor.
     *
     * @param remoteSocketAddress Dirección del remitente del mensaje.
     * @param username            Nombre de usuario del remitente.
     * @throws IOException Si hay un error al enviar mensajes.
     */
    private void processMessage(InetSocketAddress remoteSocketAddress, String username) throws IOException {
        String inputString = new String(inputDatagramPacket.getData(), 0, inputDatagramPacket.getLength(), "UTF-8");
        int separatorPosition = inputString.indexOf(">");
        String tag = inputString.substring(0, separatorPosition);
=======
    private void processMessage(InetSocketAddress remoteSocketAddress, String username) throws IOException {
        String inputString = new String(inputDatagramPacket.getData(), 0, inputDatagramPacket.getLength(), "UTF-8");
        int separatorPosition  = inputString.indexOf(">");
        String tag     = inputString.substring(0, separatorPosition);
>>>>>>> 2b8701596ff9c9dfdc8926161b5c6ebcc2eb4ba8
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
<<<<<<< HEAD
                } else {
=======
                }
                else {
>>>>>>> 2b8701596ff9c9dfdc8926161b5c6ebcc2eb4ba8
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

    /**
     * Envía un mensaje a todos los usuarios conectados.
     *
     * @param message Mensaje a enviar.
     * @throws IOException Si hay un error al enviar mensajes.
     */
    void sendToAllUsers(String message) throws IOException {
        byte[] messageBytes = message.getBytes("UTF-8");
        for (InetSocketAddress receiverSocketAddress : users.keySet()) {
            DatagramPacket outputDatagramPacket = new DatagramPacket(messageBytes, messageBytes.length, receiverSocketAddress);
            datagramSocket.send(outputDatagramPacket);
        }
    }

    /**
     * Envía un mensaje a todos los usuarios conectados excepto al remitente.
     *
     * @param message             Mensaje a enviar.
     * @param senderSocketAddress Dirección del remitente del mensaje.
     * @throws IOException Si hay un error al enviar mensajes.
     */
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
