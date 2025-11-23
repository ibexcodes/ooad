package com.example.bankingsystem;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ListView;
import javafx.event.ActionEvent;
import javafx.stage.Stage;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.layout.Pane;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.paint.Color;
import java.util.List;
import javafx.scene.text.Text;

public class DashboardController {
    private static final Logger logger = Logger.getLogger(DashboardController.class.getName());

    private User currentUser;
    private Customer currentCustomer;

    @FXML private ListView<String> InvestmentAccountdetalis;
    @FXML private ListView<String> SavingsAccountdetails;
    @FXML private ListView<String> chequeAccountdetails;
    @FXML private Pane mainContentPane;
    @FXML private Text fullNameText;


    @FXML
    public void initialize() {
        loadCenterPane("DashboardDetails.fxml");
    }

    public void initData(User user, Customer customer) {
        this.currentUser = user;
        this.currentCustomer = customer;

        if (customer != null && fullNameText != null) {
            fullNameText.setText("Welcome, " + customer.getFullName() + "!");
        }
        logger.log(Level.INFO, "Dashboard initialized for user: {0}", currentUser.getEmail());

        initializeDashboard();
    }

    private void initializeDashboard() {
        if (currentCustomer != null) {
            try (Connection conn = CustomerDBConnection.getConnection()) {
                AccountDAO accountDAO = new AccountDAOImpl(conn);
                List<Account> accounts = accountDAO.findByCustomerId(currentCustomer.getCustomerID());

                currentCustomer.getAccounts().clear();
                currentCustomer.getAccounts().addAll(accounts);

                refreshAccountSummaries();

            } catch (SQLException e) {
                logger.log(Level.SEVERE, "Failed to load accounts for customer.", e);
            }
        }
    }





    public void refreshAccountSummaries() {
        if (currentCustomer == null || currentCustomer.getAccounts().isEmpty()) {
            SavingsAccountdetails.getItems().clear();
            chequeAccountdetails.getItems().clear();
            InvestmentAccountdetalis.getItems().clear();
            return;
        }

        ObservableList<String> savingsList = FXCollections.observableArrayList();
        ObservableList<String> chequeList = FXCollections.observableArrayList();
        ObservableList<String> investmentList = FXCollections.observableArrayList();

        for (Account account : currentCustomer.getAccounts()) {
            if (account instanceof SavingsAccount) {
                savingsList.add(account.getAccountSummary());
            } else if (account instanceof ChequeAccount) {
                chequeList.add(account.getAccountSummary());
            } else if (account instanceof InvestmentAccount) {
                investmentList.add(account.getAccountSummary());
            }
        }

        SavingsAccountdetails.setItems(savingsList);
        chequeAccountdetails.setItems(chequeList);
        InvestmentAccountdetalis.setItems(investmentList);
    }


    @FXML
    private void handleMenu(ActionEvent event) {
        loadCenterPane("DashboardDetails.fxml");
    }

    @FXML
    private void handleOpenAccount(ActionEvent event) {
        loadCenterPane("OpenAccount.fxml");
    }

    @FXML
    private void handleTransactionHistory(ActionEvent event) {
        loadCenterPane("Transaction.fxml");
    }

    @FXML
    private void handleCustomerProfile(ActionEvent event) {
        loadCenterPane("customerprofile.fxml");
    }

    @FXML
    private void handleSignOut(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Main1.fxml"));
            Parent loginView = loader.load();

            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();

            Scene scene = new Scene(loginView);
            scene.setFill(Color.TRANSPARENT);
            stage.setScene(scene);
            stage.setTitle("Customer Sign In");

        } catch (IOException e) {
            logger.log(Level.SEVERE, "Failed to load Main1.fxml during sign out.", e);
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

            Object controller = loader.getController();

            if (controller instanceof OpenAccountController) {
                ((OpenAccountController) controller).initData(currentUser, currentCustomer, this);
            } else if (controller instanceof CustomerProfileController) {
                ((CustomerProfileController) controller).initData(currentCustomer);
            } else if (controller instanceof TransactionController) {
                ((TransactionController) controller).initData(currentCustomer);
            } else if (controller instanceof DashboardDetailsController) {
                ((DashboardDetailsController) controller).initData(currentCustomer, this);
            }

            mainContentPane.getChildren().clear();
            mainContentPane.getChildren().add(newPane);

            logger.log(Level.INFO, "Loaded content into main pane: {0}", fxmlPath);

        } catch (IOException e) {
            logger.log(Level.SEVERE, "Failed to load FXML: " + fxmlPath, e);
        }
    }

    private void showAlert(AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}