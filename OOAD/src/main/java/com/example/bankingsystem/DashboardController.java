package com.example.bankingsystem;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.event.ActionEvent;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.layout.Pane; // REQUIRED: Import for the Pane element
import javafx.scene.paint.Color;

public class DashboardController {
    private static final Logger logger = Logger.getLogger(DashboardController.class.getName());

    private User currentUser;

    // FXML elements from Dashboard.fxml
    @FXML private TextField depositField;
    @FXML private TextField withdrawField;
    @FXML private ListView<String> InvestmentAccountdetalis;
    @FXML private ListView<String> SavingsAccountdetails;
    @FXML private ListView<String> chequeAccountdetails;

    // NEW: Inject the main content pane using its fx:id from Dashboard.fxml
    @FXML private Pane mainContentPane;

    public void initData(User user) {
        this.currentUser = user;
        logger.log(Level.INFO, "Dashboard initialized for user: {0}", currentUser.getEmail());
        initializeDashboard();
    }

    private void initializeDashboard() {
        // Load initial content or data here
    }

    // ----------------------------------------------------------------------
    // --- NAVIGATION HANDLER METHODS ---
    // ----------------------------------------------------------------------

    @FXML
    private void handleOpenAccount(ActionEvent event) {
        // Loads OpenAccount.fxml content
        loadCenterPane("OpenAccount.fxml");
    }

    @FXML
    private void handleTransactionHistory(ActionEvent event) {
        // Loads Transaction.fxml content
        loadCenterPane("Transaction.fxml");
    }

    @FXML
    private void handleCustomerProfile(ActionEvent event) {

        loadCenterPane("customerprofile.fxml");
    }

    @FXML
    private void handleSignOut(ActionEvent event) {
        try {
            // Assumes MAI1.fxml is your login screen
            FXMLLoader loader = new FXMLLoader(getClass().getResource("MAI1.fxml"));
            Parent mainScreen = loader.load();

            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(mainScreen);
            scene.setFill(Color.TRANSPARENT);

            stage.setScene(scene);
            logger.info("User signed out successfully");
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Failed to load MAI1.fxml during sign out.", e);
        }
    }

    /**
     * Helper method to load FXML content into the main content area (mainContentPane).
     * @param fxmlPath The path to the FXML file to load.
     */
    private void loadCenterPane(String fxmlPath) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent newPane = loader.load();

            // 1. Clear the existing content of the main pane
            mainContentPane.getChildren().clear();

            // 2. Add the new content to the pane
            mainContentPane.getChildren().add(newPane);

            logger.log(Level.INFO, "Loaded content into main pane: {0}", fxmlPath);

        } catch (IOException e) {
            // Logs a severe error if the FXML file is not found or has a structural error inside it
            logger.log(Level.SEVERE, "Failed to load FXML: " + fxmlPath, e);
        }
    }
}