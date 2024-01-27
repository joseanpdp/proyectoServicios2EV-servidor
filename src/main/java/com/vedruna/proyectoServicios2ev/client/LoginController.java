package com.vedruna.proyectoServicios2ev.client;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class LoginController {

    @FXML
    TextField textFieldIp;
    @FXML
    TextField textFieldLocalPort;
    @FXML
    TextField textFieldUsername;
    @FXML
    Button buttonLogin;
    @FXML
    Label labelError;

    App app;

    void setApp(App app) {
        this.app = app;
    }

    public void onActionTextFieldIp() {
        textFieldLocalPort.requestFocus();
    }

    public void onActionTextFieldLocalPort() {
        textFieldUsername.requestFocus();
    }

    public void onActionTextFieldusername() {
        buttonLogin.requestFocus();
    }

    public void onActionButtonLogin() {
        setError("");
        app.login(Integer.parseInt(textFieldLocalPort.getText()), textFieldIp.getText(), textFieldUsername.getText());
    }

    public void setError(String message) {
        labelError.setText(message);
    }

}
