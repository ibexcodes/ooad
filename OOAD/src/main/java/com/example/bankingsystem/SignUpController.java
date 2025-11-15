package com.example.bankingsystem;

import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import java.sql.Connection;
import java.sql.SQLException;

public class SignUpController {
    @FXML private TextField firstNameField;
    @FXML private TextField surnameField;
    @FXML private TextField emailField;
    @FXML private PasswordField passwordField;

    @FXML
    private void handleSignUp() {
        String firstName = firstNameField.getText();
        String surname = surnameField.getText();
        String email = emailField.getText();
        String password = passwordField.getText();

        if (!validateEmail(email) || !validatePassword(password)) {
            System.out.println("Invalid email or password format!");
            return;
        }

        try (Connection conn = DBConnection.getConnection()) {
            UserDAO userDAO = new UserDAOImpl(conn);
            User newUser = new User(0, firstName, surname, email, password);
            userDAO.create(newUser);
            System.out.println("User signed up successfully!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private boolean validateEmail(String email) {
        return email.matches("%@%.%");
    }

    private boolean validatePassword(String password) {
        return password.length() >= 8;
    }
}