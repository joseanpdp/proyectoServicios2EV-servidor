package com.vedruna.proyectoservicios2ev;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.net.InetAddress;

public class App extends Application {

    Scene scene;

    Parent loginView;

    Parent chatView;

    LoginController loginController;
    ChatController chatController;

    Client client;

    @Override
    public void start(Stage stage) throws Exception {

        FXMLLoader fxmlLoader;

        /*
        fxmlLoader = new FXMLLoader(getClass().getResource("login-view.fxml"));
        loginView = fxmlLoader.load();
        loginController = fxmlLoader.getController();

         */
        fxmlLoader = new FXMLLoader(getClass().getResource("chat-view.fxml"));
        chatView = fxmlLoader.load();
        chatController = fxmlLoader.getController();

        //loginController.setApp(this);
        chatController.setApp(this);

        Client client = new Client(Client.LOCAL_PORT, InetAddress.getByName("localhost"), Client.REMOTE_PORT);
        chatController.setClient(client);

        scene = new Scene(chatView, 350, 500);
        stage.setTitle("CHAT");
        stage.setScene(scene);
        stage.show();
    }
    public static void main(String[] args) {
        Application.launch(App.class);
    }
}
