package com.vedruna.proyectoservicios2ev.client;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.net.InetAddress;

public class App extends Application {

    Stage stage;

    Scene scene;


    Parent loginView;

    Parent chatView;

    LoginController loginController;
    ChatController chatController;

    Client client;

    String username;

    String localPort;

    String ip;


    @Override
    public void start(Stage stage) throws Exception {

        this.stage = stage;

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
        this.stage.setScene(scene);
        this.stage.show();
    }

    public void login() throws Exception {
        stage.setTitle("ChatUDP: " + username + "@" + localPort);
        client = new Client(Integer.parseInt(localPort), InetAddress.getByName(ip), Client.REMOTE_PORT);
        client.setApp(this);
        client.login();
        client.start();
    }
    public static void main(String[] args) {
        Application.launch(App.class);
    }
}
