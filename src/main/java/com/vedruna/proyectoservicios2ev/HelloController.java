package com.vedruna.proyectoservicios2ev;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.net.InetAddress;

public class HelloController {
    Client client = new Client(Client.LOCAL_PORT, InetAddress.getByName("localhost"), Client.REMOTE_PORT);
    @FXML
    private TextField textField;

    @FXML
    private Button sendButton;

    @FXML
    private VBox innerVBox;

    public HelloController() throws Exception {
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

    public String obtenerTextoDesdeInterfaz() {
        // Obtener el texto desde el TextField en la interfaz gráfica
        return textField.getText();
    }

    public void actualizarLabel(String message) {
        Label nuevoLabel = new Label(message);
        textField.setText("");
        innerVBox.getChildren().add(nuevoLabel);
    }
}