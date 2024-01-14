package it.unipi.chesspuzzles.client.controllers;

import it.unipi.chesspuzzles.client.App;
import it.unipi.chesspuzzles.client.ChessPuzzlesApi;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

public class AuthenticationController {
    private static final Logger logger = LogManager.getLogger(AuthenticationController.class);

    @FXML
    private Button loginBtn;
    @FXML
    private Button registerBtn;
    @FXML
    private Label authenticationErrorLabel;
    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;


    @FXML
    private void loginBtnClick() {
        startAuthentication(ChessPuzzlesApi.AUTH_TYPE.LOGIN);
    }

    @FXML
    private void registerBtnClick() {
        startAuthentication(ChessPuzzlesApi.AUTH_TYPE.REGISTER);
    }

    private void showErrorLabel(String message) {
        authenticationErrorLabel.setText(message);
        authenticationErrorLabel.setVisible(true);
    }

    private void startAuthentication(ChessPuzzlesApi.AUTH_TYPE operation) {
        authenticationErrorLabel.setVisible(false);
        String username = usernameField.getText();
        String password = passwordField.getText();

        if (username.isEmpty() || password.isEmpty()) {
            showErrorLabel("Username and password fields can't be empty");
            return;
        }

        disableControls();
        Task<Void> task = new Task<>() {
            @Override
            public Void call() {
                try {
                    int userId = ChessPuzzlesApi.authenticate(username, password, operation);
                    if (userId == 0) {
                        switch (operation) {
                            case LOGIN:
                                Platform.runLater(() -> showErrorLabel("Wrong username or password"));
                                break;
                            case REGISTER:
                                Platform.runLater(() -> showErrorLabel("Username already taken"));
                                break;
                        }
                    } else {
                        Platform.runLater(() -> {
                            App.setUsername(username);
                            App.setUserId(userId);
                            App.setRoot("main");
                        });
                    }
                } catch (IOException | InterruptedException e) {
                    Platform.runLater(() -> showErrorLabel("Something went wrong, try again later"));
                    logger.fatal("Unable to authenticate user -> {}", e.getMessage());
                }
                finally {
                    Platform.runLater(() -> enableControls());
                }
                return null;
            }
        };
        new Thread(task).start();
    }

    private void disableControls() {
        loginBtn.setDisable(true);
        registerBtn.setDisable(true);
    }

    private void enableControls() {
        loginBtn.setDisable(false);
        registerBtn.setDisable(false);
    }


}
