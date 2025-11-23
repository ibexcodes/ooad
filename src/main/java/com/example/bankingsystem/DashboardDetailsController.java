package com.example.bankingsystem;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.event.ActionEvent;
import javafx.scene.paint.Color;
import javafx.scene.Parent;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DashboardDetailsController {
    private static final Logger logger = Logger.getLogger(DashboardDetailsController.class.getName());

    @FXML private ComboBox<String> depositAccountComboBox;
    @FXML private TextField depositAmountField;

    @FXML private ComboBox<String> withdrawAccountComboBox;
    @FXML private TextField withdrawAmountField;

    @FXML private Text statusText;

    private Customer currentCustomer;
    private DashboardController parentController;

    @FXML
    public void initialize() {
        if (depositAccountComboBox != null) {
            depositAccountComboBox.sceneProperty().addListener((obs, oldScene, newScene) -> {
                if (newScene != null) {
                    refreshAccounts();
                }
            });
        }
    }

    public void initData(Customer customer, DashboardController parentController) {
        this.currentCustomer = customer;
        this.parentController = parentController;
        refreshAccounts();
    }

    private void refreshAccounts() {
        if (currentCustomer != null) {
            try (Connection conn = CustomerDBConnection.getConnection()) {
                AccountDAO accountDAO = new AccountDAOImpl(conn);
                List<Account> accounts = accountDAO.findByCustomerId(currentCustomer.getCustomerID());
                
                currentCustomer.getAccounts().clear();
                currentCustomer.getAccounts().addAll(accounts);
                
                populateAccountComboBoxes();
                
            } catch (SQLException e) {
                logger.log(Level.SEVERE, "Failed to refresh accounts", e);
                updateStatus(Color.RED, "Error loading accounts. Please try again.");
            }
        }
    }

    private void populateAccountComboBoxes() {
        depositAccountComboBox.getItems().clear();
        withdrawAccountComboBox.getItems().clear();
        
        if (currentCustomer != null && !currentCustomer.getAccounts().isEmpty()) {
            for (Account account : currentCustomer.getAccounts()) {
                String type = account.getClass().getSimpleName().replace("Account", "");
                String item = String.format("%s - %s", type, account.getAccountNumber());
                depositAccountComboBox.getItems().add(item);
                withdrawAccountComboBox.getItems().add(item);
            }
            if (!depositAccountComboBox.getItems().isEmpty()) {
                depositAccountComboBox.getSelectionModel().selectFirst();
                withdrawAccountComboBox.getSelectionModel().selectFirst();
            }
        }
    }

    @FXML
    private void handleDeposit(ActionEvent event) {
        processTransaction("Deposit");
    }

    @FXML
    private void handleWithdrawal(ActionEvent event) {
        processTransaction("Withdrawal");
    }

    private void processTransaction(String type) {
        statusText.setText("");

        ComboBox<String> accountComboBox = (type.equals("Deposit")) ? depositAccountComboBox : withdrawAccountComboBox;
        TextField amountField = (type.equals("Deposit")) ? depositAmountField : withdrawAmountField;

        String selectedAccountSummary = accountComboBox.getValue();

        double amount = 0.0;

        if (selectedAccountSummary == null) {
            updateStatus(Color.RED, "Please select an account.");
            return;
        }

        try {
            amount = Double.parseDouble(amountField.getText());
            if (amount <= 0) {
                updateStatus(Color.RED, "Amount must be greater than zero.");
                return;
            }
        } catch (NumberFormatException e) {
            updateStatus(Color.RED, "Please enter a valid number for the amount.");
            return;
        }

        String accountNumber = selectedAccountSummary.split(" - ")[1].trim();

        Account account = currentCustomer.getAccounts().stream()
                .filter(a -> a.getAccountNumber().equals(accountNumber))
                .findFirst()
                .orElse(null);

        if (account == null) {
            updateStatus(Color.RED, "Error: Account not found in customer profile.");
            return;
        }

        try (Connection conn = CustomerDBConnection.getConnection()) {
            AccountDAO accountDAO = new AccountDAOImpl(conn);
            double newBalance;
            String message;

            if (type.equals("Deposit")) {
                newBalance = account.getBalance() + amount;
                account.setAccountBalance(newBalance);
                message = String.format("Deposit successful. Account %s. New Balance: BWP%.2f", accountNumber, newBalance);

            } else { 
                if (account instanceof Withdrawal) {
                    double oldBalance = account.getBalance();
                    ((Withdrawal) account).withdraw(amount);
                    newBalance = account.getBalance();

                    if (newBalance == oldBalance) {
                        updateStatus(Color.RED, "Withdrawal failed. Insufficient funds or limit reached.");
                        return;
                    }
                    message = String.format("Withdrawal successful. Account %s. New Balance: BWP%.2f", accountNumber, newBalance);
                } else {
                    updateStatus(Color.RED, "This account type does not support withdrawal.");
                    return;
                }
            }

            accountDAO.updateBalance(account.getAccountNumber(), newBalance);

            Transaction tx = new Transaction(account.getAccountNumber(), LocalDateTime.now(), type, amount, newBalance);
            accountDAO.recordTransaction(tx);

            amountField.clear();
            updateStatus(Color.GREEN, message);

            if (parentController != null) {
                parentController.refreshAccountSummaries();
            }

        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Database error during transaction.", e);
            updateStatus(Color.RED, "Database Error: " + e.getMessage());
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Transaction failed.", e);
            updateStatus(Color.RED, "An unexpected error occurred: " + e.getMessage());
        }
    }

    private void updateStatus(Color color, String message) {
        statusText.setFill(color);
        statusText.setText(message);
    }
}