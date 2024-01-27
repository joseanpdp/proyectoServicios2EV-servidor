package com.vedruna.proyectoServicios2ev.client;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

import java.io.IOException;

public class ChatController {

    @FXML
    TextField textField;
    @FXML
    Button sendButton;
    @FXML
    private ScrollPane scrollPane;
    @FXML
    private TextFlow textFlow;

    App app;

    public void setApp(App app) {
        this.app = app;
        scrollPane.setVvalue(scrollPane.getVmin());

    }

    public void onActionSendButton() throws IOException {
        if (!sendButton.getText().equals("Cerrar")) {
            sendMessage();
        }
        else {
            app.scene.setRoot(app.loginView);
        }
    }

    public void onKeyPressedTextField(KeyEvent keyEvent) throws IOException {
        if( keyEvent.getCode() == KeyCode.ENTER ) {
            sendMessage();
        }
    }

    private void sendMessage() throws IOException {
        String message = textField.getText().trim();
        if (!message.equals("")) {
            showUserMessage(Color.BLUE, app.username, message);
            textField.setText("");
            app.send(message);
        }
    }

    public void showUserMessage(Color color, String username, String message) {
        Platform.runLater(() -> {
            Text text;
            text = new Text("\n" + username + ": ");
            text.setFill(color);
            textFlow.getChildren().add(text);
            text = new Text(message);
            textFlow.getChildren().add(text);
            scrollPane.setVvalue(scrollPane.getVmax());
        });
    }

    public void showServerInfo(String info) {
        Platform.runLater(() -> {
            Text text;
            text = new Text("\n\n" + info + "\n\n");
            textFlow.getChildren().add(text);
            scrollPane.setVvalue(scrollPane.getVmax());
        });
    }

    public void stop() {
        Platform.runLater(() -> {
            showServerInfo("Server has been stopped");
            textField.setVisible(false);
            sendButton.setText("Cerrar");
        });
    }
}
