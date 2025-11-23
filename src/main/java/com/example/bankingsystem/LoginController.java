package com.example.bankingsystem;

import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.sql.Connection;
import javafx.event.ActionEvent;
import java.io.IOException;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import java.sql.SQLException; 

public class LoginController {

    @FXML private TextField emailField;
    @FXML private PasswordField passwordField;

    @FXML
    private void handleLogin() {
        String email = emailField.getText();
        String password = passwordField.getText();
        User user = null;
        Customer customer = null;

        try (Connection userConn = DBConnection.getConnection()) {
            UserDAO userDAO = new UserDAOImpl(userConn);
            user = userDAO.findByEmailAndPassword(email, password);

            if (user != null) {
                try (Connection custConn = CustomerDBConnection.getConnection()) {
                    CustomerDAO customerDAO = new CustomerDAOImpl(custConn);
                    customer = customerDAO.findByUserId(user.getId());
                } catch (SQLException e) {
                    e.printStackTrace();
                }

                FXMLLoader loader = new FXMLLoader(getClass().getResource("Dashboard.fxml"));
                Parent dashboard = loader.load();
                DashboardController dashboardController = loader.getController();

                dashboardController.initData(user, customer);

                Stage stage = (Stage) emailField.getScene().getWindow();
                stage.setScene(new Scene(dashboard));
            } else {

                Alert alert = new Alert(AlertType.ERROR);
                alert.setTitle("Login Failed");
                alert.setHeaderText("Invalid Credentials");
                alert.setContentText("The email or password you entered is incorrect. Please try again.");
                alert.showAndWait();
            }
        } catch (Exception e) {

            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("System Error");
            alert.setHeaderText("An unexpected error occurred.");
            alert.setContentText("Could not connect to the database or an internal error occurred: " + e.getMessage());
            alert.showAndWait();
        }
    }
    @FXML
    private void handleForgotPassword(ActionEvent event) {
        try {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("ForgotPassword.fxml"));
            Parent forgotPasswordView = loader.load();
            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(forgotPasswordView));
            stage.setTitle("Password Reset");

        } catch (IOException e) {

            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Navigation Failed");
            alert.setContentText("Could not load the password reset screen.");
            alert.showAndWait();
        }
    }
}