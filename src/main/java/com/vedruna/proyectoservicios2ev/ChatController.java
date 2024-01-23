package com.vedruna.proyectoservicios2ev;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

public class ChatController {

    App app;
    private Client client;
    @FXML
    private TextField textField;

    @FXML
    private Button sendButton;

    @FXML
    private VBox innerVBox;

    public ChatController() throws Exception {}

    public void setClient(Client client) throws Exception {
        this.client = client;
    }
    @FXML
    protected void onSendMessage() {
        String texto = textField.getText();
        if (!texto.equals("")) {
            Label nuevoLabel = new Label(texto);
            textField.setText("");
            innerVBox.getChildren().add(nuevoLabel);
        }
    }

    public void showMessage(String message) {
        if (!message.equals("")) {
            Label nuevoLabel = new Label(message);
            innerVBox.getChildren().add(nuevoLabel);
        }
    }

    public String obtenerTextoDesdeInterfaz() {
        // Obtener el texto desde el TextField en la interfaz gráfica
        return textField.getText();
    }

    public void actualizarLabel(String message) {
        Label nuevoLabel = new Label(message);
        textField.setText("");
        innerVBox.getChildren().add(nuevoLabel);
    }

    public void setApp(App app) {
        this.app = app;
    }

}