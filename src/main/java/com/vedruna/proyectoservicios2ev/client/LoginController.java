package com.vedruna.proyectoservicios2ev.client;

import com.vedruna.proyectoservicios2ev.client.App;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
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

    @FXML
    public void onLogin() throws Exception {
        app.ip = textFieldIp.getText();
        app.localPort = textFieldLocalPort.getText();
        app.username = textFieldUsername.getText();
        this.app.scene.setRoot(app.chatView);
        app.login();
    }
}
