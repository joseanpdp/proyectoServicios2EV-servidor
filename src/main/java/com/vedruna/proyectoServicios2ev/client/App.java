package com.vedruna.proyectoServicios2ev.client;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.*;

public class App extends Application {

    private static final Logger LOGGER = LogManager.getLogger(Connector.class);

    public static void main(String[] args) {
        Application.launch(App.class);
    }

    final static int REMOTE_PORT = 5010;

    Scene scene;
    Parent loginView;
    Parent chatView;
    private Stage stage;
    private LoginController loginController;
    private ChatController chatController;
    private Connector connector;
    String username;

    public App() {
        super();
        connector = new Connector(this);
    }

    @Override
    public void start(Stage stage) throws Exception {

        this.stage = stage;
        stage.setResizable(false);

        FXMLLoader fxmlLoader;

        fxmlLoader = new FXMLLoader(getClass().getResource("login-view.fxml"));
        loginView = fxmlLoader.load();
        loginController = fxmlLoader.getController();

        fxmlLoader = new FXMLLoader(getClass().getResource("chat-view.fxml"));
        chatView = fxmlLoader.load();
        chatController = fxmlLoader.getController();

        loginController.setApp(this);
        chatController.setApp(this);

        this.scene = new Scene(loginView, 350, 500);
        scene.getStylesheets().add(App.class.getResource("/com/vedruna/proyectoServicios2ev/Styles.css").toExternalForm());
        this.stage.setScene(scene);
        this.stage.show();

    }

    /**
     * Método de la clase Application que es llamado cuando la aplicación termina.
     * Según la documentación de JavaFX es el lugar conveniente para liberar los recursos
     * al finalizar la aplicación.
     */
    @Override
    public void stop(){
        LOGGER.debug("Cerrando aplicación");
    }

    // Llamado por connector
    public void receive(String inputString) {
        int firstSeparatorPosition = inputString.indexOf(">");
        int secondSeparatorPosition = inputString.indexOf(":");
        String tag      = inputString.substring(0, firstSeparatorPosition);
        String username = inputString.substring(firstSeparatorPosition + 1, secondSeparatorPosition);
        String message  = inputString.substring(secondSeparatorPosition + 1);
        switch (tag) {
            case "chatting":
                chatController.showUserMessage(Color.GREEN, username, message);
                break;
            case "stop":
                connector.stop();
                chatController.stop();
                break;
        }
    }

    // Llamado por chatController
    public void login(int localPort, String remoteAddress, String username) {
        try {
            connector.open(localPort);
            connector.connect(new InetSocketAddress(remoteAddress, REMOTE_PORT), username);
            LOGGER.info("Antes de recibir");
            String response = connector.receive();
            connector.datagramSocket.setSoTimeout(0);
            int firstSeparatorPosition = response.indexOf(">");
            String tag = response.substring(0, firstSeparatorPosition);
            switch (tag) {
                case "welcome":
                    this.username = username;
                    stage.setTitle("ChatUDP: " + username + "@" + localPort);
                    connector.start();
                    chatController.textField.setVisible(true);
                    chatController.sendButton.setText("Enviar");
                    scene.setRoot(chatView);
                    chatController.showServerInfo("El servidor le da la bienvenida");
                    break;
                case "error":
                    loginController.setError("Nombre de usuario no válido");
            }
        } catch (BindException ex) {
            loginController.setError("Ese puerto ya está en uso");
        } catch (SocketTimeoutException ex) {
            loginController.setError("El servidor no responde");
        } catch (SocketException ex ) {
            loginController.setError("Error de conexión (" + ex + ")");
        } catch (IOException ex) {
        loginController.setError("Error de conexión (" + ex + ")");
        }
    }

    // Llamado por loginController
    public void send(String message) throws IOException {
        message =  "chatting>" + username + ":" + message;
        connector.send(message);
    }



}
