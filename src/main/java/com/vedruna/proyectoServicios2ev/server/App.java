package com.vedruna.proyectoServicios2ev.server;

// SE TIENE QUE HACER LO DEL RELOGGING

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class App extends Application {
    private static final Logger LOGGER = LogManager.getLogger(Server.class);

    public static void main(String[] args) {
        LOGGER.debug("Before");
        Application.launch(App.class);
        LOGGER.debug("After");
    }

    final static int LOCAL_PORT = 5010;

    Scene scene;
    Parent infoView;
    private Stage stage;
    InfoController infoController;
    private Server server;

    public App() throws Exception {
        super();
        server = new Server(LOCAL_PORT, this);
        Thread serverThread = new Thread(server);
        serverThread.start();
    }

    @Override
    public void start(Stage stage) throws Exception {

        this.stage = stage;
        LOGGER.debug("Stage: {}", stage);
        stage.setResizable(false);

        FXMLLoader fxmlLoader;

        fxmlLoader = new FXMLLoader(getClass().getResource("info-view.fxml"));
        infoView = fxmlLoader.load();
        infoController = fxmlLoader.getController();

        infoController.setApp(this);

        this.scene = new Scene(infoView, 800, 500);
        this.stage.setScene(scene);
        this.stage.show();

    }
}
