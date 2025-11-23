package com.example.bankingsystem;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CustomerDBConnection {
    private static final Logger LOGGER = Logger.getLogger(CustomerDBConnection.class.getName());
    private static final String URL = "jdbc:sqlite:customer.db";

    static {
        try (Connection conn = DriverManager.getConnection(URL);
             Statement stmt = conn.createStatement()) {

            String createCustomersTable = "CREATE TABLE IF NOT EXISTS customers (" +
                    "customer_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "user_id INTEGER UNIQUE NOT NULL," + 
                    "national_id VARCHAR UNIQUE NOT NULL," +
                    "full_name TEXT NOT NULL," +
                    "date_of_birth DATE," +
                    "gender TEXT," +
                    "contact_number VARCHAR," +
                    "email_address TEXT NOT NULL," +
                    "residential_address TEXT," +
                    "next_of_kin TEXT," +
                    "occupation TEXT" +
                    ")";
            stmt.execute(createCustomersTable);

            String createAccountsTable = "CREATE TABLE IF NOT EXISTS accounts (" +
                    "account_number TEXT PRIMARY KEY," +
                    "customer_id INTEGER NOT NULL," +
                    "account_type TEXT NOT NULL," +
                    "balance REAL NOT NULL," +
                    "interest_rate REAL," +
                    "overdraft_limit REAL," +
                    "cheque_name TEXT," +
                    "company_address TEXT," +
                    "FOREIGN KEY (customer_id) REFERENCES customers(customer_id)" +
                    ")";
            stmt.execute(createAccountsTable);

            String createTransactionsTable = "CREATE TABLE IF NOT EXISTS transactions (" +
                    "transaction_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "account_number TEXT NOT NULL," +
                    "transaction_date TEXT NOT NULL," +
                    "type TEXT NOT NULL," +
                    "amount REAL NOT NULL," +
                    "new_balance REAL NOT NULL," +
                    "FOREIGN KEY (account_number) REFERENCES accounts(account_number)" +
                    ")";
            stmt.execute(createTransactionsTable);

            LOGGER.info("Database 'customer.db' tables ensured to exist.");

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "FATAL: Could not initialize customer database.", e);
        }
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL);
    }
}