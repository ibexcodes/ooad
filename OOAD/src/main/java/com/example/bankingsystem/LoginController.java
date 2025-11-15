package com.example.bankingsystem;

import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.sql.Connection;

public class LoginController {
    @FXML private TextField emailField;
    @FXML private PasswordField passwordField;

    @FXML
    private void handleLogin() {
        String email = emailField.getText();
        String password = passwordField.getText();

        try (Connection conn = DBConnection.getConnection()) {
            UserDAO userDAO = new UserDAOImpl(conn);
            User user = userDAO.findByEmailAndPassword(email, password);
            if (user != null) {
                System.out.println("Login successful!");
                FXMLLoader loader = new FXMLLoader(getClass().getResource("Dashboard.fxml"));
                Parent dashboard = loader.load();
                Stage stage = (Stage) emailField.getScene().getWindow();
                stage.setScene(new Scene(dashboard));
            } else {
                System.out.println("Invalid credentials!");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}