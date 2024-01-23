package com.vedruna.proyectoservicios2ev;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class LoginController {
    @FXML
    TextField textFieldUser;

    @FXML
    TextField textFieldPassword;

    @FXML
    Button buttonLogin;

    @FXML
    Label accessError;

    App app;

    void setApp(App app) {
        this.app = app;
    }

    public void onTextFieldUser() {
        textFieldPassword.requestFocus();
    }

    public void onTextFieldPassword() {
        buttonLogin.requestFocus();
    }

    public void onLogin() {
        // Logica de identificaciñon ausente
        this.app.scene.setRoot(app.chatView);
        accessError.setText("");

        textFieldUser.setText("");
        textFieldPassword.setText("");
    }
}
