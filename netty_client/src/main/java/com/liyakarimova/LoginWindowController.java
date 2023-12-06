package com.liyakarimova;

import com.liyakarimova.AuthCommands.AuthInformation;
import com.liyakarimova.Net.Net;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

public class LoginWindowController {

    @FXML
    private Button logButton;

    @FXML
    private TextField loginField;

    @FXML
    private PasswordField passwordField;

    Net net;

    @FXML
    private void initialize () {
        logButton.setOnMouseClicked(this:: onLogButtonClicked);
        net = Net.getInstance(a -> {

        });
    }

    private void onLogButtonClicked(MouseEvent mouseEvent) {
        new AuthInformation(loginField.getText(), passwordField.getText());
    }


}
