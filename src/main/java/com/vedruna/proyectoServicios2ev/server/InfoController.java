package com.vedruna.proyectoServicios2ev.server;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.ScrollPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
/**
 * Controlador para la vista de información del servidor.
 */
public class InfoController {

    /** Área desplazable para el texto en la interfaz gráfica. */
    @FXML
    private ScrollPane scrollPane;

    /** Contenedor de texto con formato en la interfaz gráfica. */
    @FXML
    private TextFlow textFlow;

    /** Instancia de la aplicación principal. */
    App app;

<<<<<<< HEAD
    /**
     * Muestra información en la interfaz gráfica de manera asíncrona.
     *
     * @param colorName Color del nombre de usuario.
     * @param colorPort Color del puerto.
     * @param username Nombre de usuario.
     * @param address Dirección IP.
     * @param port Puerto de conexión.
     * @param message Mensaje a mostrar.
     */
    public void showInfo(Color colorName, Color colorPort, String username, String address, int port, String message) {
=======
    public void showInfo(Color colorName, Color colorPort, String username, String address, int port, String message) {//
>>>>>>> 2b8701596ff9c9dfdc8926161b5c6ebcc2eb4ba8
        Platform.runLater(() -> {
            Text text;
            text = new Text("\n" + username);
            text.setFill(colorName);
            textFlow.getChildren().add(text);
            text = new Text("(");
            textFlow.getChildren().add(text);
            text = new Text(address + ":" + port);
            text.setFill(colorPort);
            textFlow.getChildren().add(text);
            text = new Text("): ");
            textFlow.getChildren().add(text);
            text = new Text(message + "\n");
            textFlow.getChildren().add(text);
            scrollPane.applyCss();
            scrollPane.layout();
            scrollPane.setVvalue(scrollPane.getVmax());
        });
    }

    /**
     * Establece la instancia de la aplicación principal.
     *
     * @param app Instancia de la aplicación principal.
     */
    public void setApp(App app) {
        this.app = app;
    }
}