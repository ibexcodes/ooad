package com.example.bankingsystem;

import javafx.fxml.FXML;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MainController implements Initializable {

    @FXML
    private VBox vbox;

    private Parent fxml;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        animateAndLoad("SignIn.fxml", vbox.getLayoutX() * 12, Duration.seconds(0.1));
    }

    @FXML
    private void open_signin(ActionEvent event) {
        animateAndLoad("SignIn.fxml", vbox.getLayoutX() * 12);
    }

    @FXML
    private void open_signup(ActionEvent event) {
        animateAndLoad("SignUp.fxml", 0);
    }

    private void animateAndLoad(String fxmlFile, double toX) {
        animateAndLoad(fxmlFile, toX, Duration.seconds(1)); 
    }

    private void animateAndLoad(String fxmlFile, double toX, Duration duration) {
        TranslateTransition transition = new TranslateTransition(duration, vbox);
        transition.setToX(toX);
        transition.play();

        transition.setOnFinished(e -> {
            try {
                URL location = getClass().getResource("/com/example/bankingsystem/" + fxmlFile);
                if (location == null) {
                    System.err.println("FXML file not found: " + fxmlFile);
                    return;
                }

                fxml = FXMLLoader.load(location);
                vbox.getChildren().clear();
                vbox.getChildren().setAll(fxml);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });
    }
}