package com.vedruna.proyectoservicios2ev;

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

public class ChatController {

    App app;
    @FXML
    private TextField textField;

    @FXML
    private ScrollPane scrollPane;

    @FXML
    private TextFlow textFlow;

    @FXML
    private Button sendButton;

    public ChatController() throws Exception {}

    @FXML
    protected void onSendMessage() {
        String message = textField.getText().trim();
        if (!message.equals("")) {
            showUserMessage(Color.BLUE, app.username, message);
            textField.setText("");
            app.client.sendMessage(message);
        }
    }

    @FXML
    protected void onKeyPressed(KeyEvent keyEvent) {
        if( keyEvent.getCode() == KeyCode.ENTER ) {
            onSendMessage();
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
        Text text;
        text = new Text(info + "\n\n");
        textFlow.getChildren().add(text);
        scrollPane.setVvalue(scrollPane.getVmax());
    }



    public void setApp(App app) {
        this.app = app;
    }

}