package com.vedruna.proyectoServicios2ev.server;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.ScrollPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

public class InfoController {

    @FXML
    private ScrollPane scrollPane;

    @FXML
    private TextFlow textFlow;

    App app;

    public void showInfo(Color color, String username, String message) {//
        Platform.runLater(() -> {
            Text text;
            text = new Text("\n" + username + ": ");
            text.setFill(color);
            textFlow.getChildren().add(text);
            text = new Text(message);
            textFlow.getChildren().add(text);
            scrollPane.applyCss();
            scrollPane.layout();
            scrollPane.setVvalue(scrollPane.getVmax());
        });
    }


    public void setApp(App app) {
        this.app = app;
    }
}
