package com.vedruna.proyectoServicios2ev.server;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
/**
 * Clase principal de la aplicación del servidor.
 * Extiende la clase `Application` de JavaFX para gestionar la interfaz gráfica.
 */
public class App extends Application {
    private static final Logger LOGGER = LogManager.getLogger(Server.class);

    /**
     * Método principal que inicia la aplicación.
     * @param args Argumentos de la línea de comandos.
     */
    public static void main(String[] args) {
        LOGGER.debug("Before");
        Application.launch(App.class);
        LOGGER.debug("After");
    }

<<<<<<< HEAD
    /** Puerto local para la comunicación del servidor. */
=======
>>>>>>> 2b8701596ff9c9dfdc8926161b5c6ebcc2eb4ba8
    final static int LOCAL_PORT = 5010;

    /** Escena de la interfaz gráfica. */
    Scene scene;

    /** Vista principal que muestra la información del servidor. */
    Parent infoView;

    /** Instancia del objeto `Stage` de JavaFX. */
    private Stage stage;

    /** Instancia del controlador de información del servidor. */
    InfoController infoController;

    /** Instancia del servidor. */
    private Server server;

    /**
     * Constructor de la clase App.
     * Inicializa el servidor en un hilo separado al crear una instancia de la clase.
     * @throws Exception Sí ocurre algún error durante la inicialización del servidor.
     */
    public App() throws Exception {
        super();
        server = new Server(LOCAL_PORT, this);
        Thread serverThread = new Thread(server);
        serverThread.start();
    }

    /**
     * Método de inicio de la aplicación.
     * @param stage Instancia del objeto `Stage` de JavaFX.
     * @throws Exception Si ocurre algún error durante la inicialización de la interfaz gráfica.
     */
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
