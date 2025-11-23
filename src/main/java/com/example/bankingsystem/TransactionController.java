package com.example.bankingsystem;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TransactionController {
    private static final Logger logger = Logger.getLogger(TransactionController.class.getName());

    @FXML private TableView<Transaction> transactionTable;
    @FXML private TableColumn<Transaction, LocalDateTime> dateColumn;
    @FXML private TableColumn<Transaction, String> accountNumberColumn; 
    @FXML private TableColumn<Transaction, String> typeColumn;
    @FXML private TableColumn<Transaction, Double> amountColumn;
    @FXML private TableColumn<Transaction, Double> newBalanceColumn; 

    private Customer currentCustomer;

    @FXML
    public void initialize() {
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        accountNumberColumn.setCellValueFactory(new PropertyValueFactory<>("accountNumber"));
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        amountColumn.setCellValueFactory(new PropertyValueFactory<>("amount"));
        newBalanceColumn.setCellValueFactory(new PropertyValueFactory<>("newBalance"));
    }

    public void initData(Customer customer) {
        this.currentCustomer = customer;
        if (currentCustomer != null) {
            loadAllTransactions();
        } else {
            transactionTable.setPlaceholder(new Label("Customer profile not loaded."));
        }
    }

    private void loadAllTransactions() {
        if (currentCustomer.getAccounts().isEmpty()) {
            transactionTable.setPlaceholder(new Label("No accounts available to view transactions."));
            return;
        }

        ObservableList<Transaction> allTransactions = FXCollections.observableArrayList();

        try (Connection conn = CustomerDBConnection.getConnection()) {
            AccountDAO accountDAO = new AccountDAOImpl(conn);

            for (Account account : currentCustomer.getAccounts()) {
                List<Transaction> accountTxs = accountDAO.getTransactions(account.getAccountNumber());
                allTransactions.addAll(accountTxs);
            }

            allTransactions.sort((t1, t2) -> t2.getDate().compareTo(t1.getDate()));

            transactionTable.setItems(allTransactions);

        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Failed to load transactions from database.", e);
            transactionTable.setPlaceholder(new Label("Error loading transactions from database."));
        }
    }
}