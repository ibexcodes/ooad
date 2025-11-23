package com.example.bankingsystem;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TextField;
import javafx.scene.control.PasswordField;
import javafx.event.ActionEvent;
import javafx.stage.Stage;
import javafx.scene.paint.Color;
import java.io.IOException;
import java.sql.Connection;
import java.util.logging.Level;
import java.util.logging.Logger;


public class ForgotPasswordController {
    private static final Logger logger = Logger.getLogger(ForgotPasswordController.class.getName());

    @FXML private TextField emailField;
    @FXML private PasswordField newPasswordField;
    @FXML private PasswordField confirmPasswordField;

    @FXML
    private void handleResetPassword(ActionEvent event) {
        String email = emailField.getText();
        String newPassword = newPasswordField.getText();
        String confirmPassword = confirmPasswordField.getText();

        if (email.isEmpty() || !email.contains("@")) {
            showAlert(AlertType.ERROR, "Input Error", "Please enter a valid email address.");
            return;
        }
        if (newPassword.isEmpty() || confirmPassword.isEmpty()) {
            showAlert(AlertType.ERROR, "Input Error", "Password fields cannot be empty.");
            return;
        }
        if (!newPassword.equals(confirmPassword)) {
            showAlert(AlertType.ERROR, "Input Error", "The new passwords do not match.");
            return;
        }
        if (newPassword.length() < 6) {
            showAlert(AlertType.ERROR, "Input Error", "Password must be at least 6 characters long.");
            return;
        }

        try (Connection conn = DBConnection.getConnection()) {
            UserDAOImpl userDAO = new UserDAOImpl(conn);


            if (userDAO.findByEmail(email) == null) {
                showAlert(AlertType.ERROR, "Reset Failed", "Password reset failed. Please check your email.");
                logger.warning("Attempted password reset for non-existent email: " + email);
                return;
            }

            if (userDAO.updatePassword(email, newPassword)) {
                showAlert(AlertType.INFORMATION, "Success", "Your password has been successfully reset. You can now sign in.");
                handleBackToLogin(event);
            } else {
                showAlert(AlertType.ERROR, "Reset Failed", "A database error occurred during password update. Please try again.");
            }

        } catch (Exception e) {
            logger.log(Level.SEVERE, "Unexpected error during password reset process.", e);
            showAlert(AlertType.ERROR, "System Error", "A critical system error occurred. Please try again later.");
        }
    }

    @FXML
    public void handleBackToLogin(ActionEvent event) {
        try {
            logger.info("Navigating back to Login screen (Main1.fxml).");

            FXMLLoader loader = new FXMLLoader(getClass().getResource("Main1.fxml"));
            Parent loginView = loader.load();

            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();

            Scene scene = new Scene(loginView);
            scene.setFill(Color.TRANSPARENT);
            stage.setScene(scene);
            stage.setTitle("Customer Sign In");

        } catch (IOException e) {
            logger.log(Level.SEVERE, "Failed to load Main1.fxml during back navigation.", e);
            showAlert(AlertType.ERROR, "Navigation Error", "Could not load the Sign In screen (Main1.fxml).");
        }
    }

    private void showAlert(AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}