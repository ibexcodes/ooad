package com.example.bankingsystem;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SignUpController {
    // Replacing System.out.println with standard Java Logger
    private static final Logger logger = Logger.getLogger(SignUpController.class.getName());

    // These fields will be assigned when fx:id is added to the FXML
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

        // Input validation checks
        if (firstName.isEmpty() || surname.isEmpty() || email.isEmpty() || password.isEmpty()) {
            showAlert("Error", "All fields are required.");
            return;
        }


        if (!validateEmail(email)) {
            showAlert("Error", "Please enter a valid email address.");
            return;
        }

        if (!validatePassword(password)) {
            showAlert("Error", "Password must be at least 8 characters long.");
            return;
        }

        try (Connection conn = DBConnection.getConnection()) {
            UserDAO userDAO = new UserDAOImpl(conn);
            User newUser = new User(0, firstName, surname, email, password);
            userDAO.create(newUser);
            showAlert("Success", "User signed up successfully! You can now log in.");


            firstNameField.clear();
            surnameField.clear();
            emailField.clear();
            passwordField.clear();

        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Database error during user sign up. Email might already be registered.", e);
            showAlert("Database Error", "An error occurred during sign up. Email might already be registered.");
        }
    }

    private boolean validateEmail(String email) {
        return email.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$");
    }

    private boolean validatePassword(String password) {

        return password.length() >= 8;
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}