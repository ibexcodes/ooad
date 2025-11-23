package com.example.bankingsystem;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class AccountDAOImpl implements AccountDAO {
    private static final Logger logger = Logger.getLogger(AccountDAOImpl.class.getName());
    private final Connection connection;
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    public AccountDAOImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void create(Account account) throws SQLException {
        String sql = "INSERT INTO accounts (account_number, customer_id, account_type, balance, interest_rate, overdraft_limit, cheque_name, company_address) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, account.getAccountNumber());
            stmt.setInt(2, account.getCustomer().getCustomerID());
            stmt.setString(3, account.getClass().getSimpleName());
            stmt.setDouble(4, account.getBalance());

            if (account instanceof SavingsAccount) {
                stmt.setDouble(5, ((SavingsAccount) account).getInterestRate()); 
                stmt.setNull(6, Types.REAL); 
                stmt.setNull(7, Types.VARCHAR); 
                stmt.setNull(8, Types.VARCHAR); 
            } else if (account instanceof InvestmentAccount) {
                stmt.setDouble(5, ((InvestmentAccount) account).getInterestRate());
                stmt.setNull(6, Types.REAL);
                stmt.setNull(7, Types.VARCHAR);
                stmt.setNull(8, Types.VARCHAR);
            } else if (account instanceof ChequeAccount) {
                stmt.setNull(5, Types.REAL);
                stmt.setDouble(6, ChequeAccount.OVERDRAFT_LIMIT); 
                stmt.setString(7, ((ChequeAccount) account).getNameForCheque());
                stmt.setString(8, ((ChequeAccount) account).getCompanyAddress());
            } else {
                stmt.setNull(5, Types.REAL);
                stmt.setNull(6, Types.REAL);
                stmt.setNull(7, Types.VARCHAR);
                stmt.setNull(8, Types.VARCHAR);
            }

            stmt.executeUpdate();
            logger.info("Account created: " + account.getAccountNumber());
        }
    }

    @Override
    public List<Account> findByCustomerId(int customerId) throws SQLException {
        List<Account> accounts = new ArrayList<>();
        String sql = "SELECT * FROM accounts WHERE customer_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, customerId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Customer customer = new IndividualCustomer(customerId, "", "", "", "", "", "", "", "", "");

                    String type = rs.getString("account_type");
                    Account account = null;

                    if (type.equals("SavingsAccount")) {
                        account = new SavingsAccount(customer, rs.getDouble("balance"));
                    } else if (type.equals("ChequeAccount")) {
                        account = new ChequeAccount(customer, rs.getDouble("balance"), rs.getString("cheque_name"), rs.getString("company_address"));
                    } else if (type.equals("InvestmentAccount")) {
                        account = new InvestmentAccount(customer, rs.getDouble("balance"));
                    }

                    if (account != null) {
                        account.setAccountNumber(rs.getString("account_number"));
                        accounts.add(account);
                    }
                }
            }
        }
        return accounts;
    }

    @Override
    public void recordTransaction(Transaction transaction) throws SQLException {
        String sql = "INSERT INTO transactions (account_number, transaction_date, type, amount, new_balance) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, transaction.getAccountNumber());
            stmt.setString(2, transaction.getDate().format(DATE_FORMATTER));
            stmt.setString(3, transaction.getType());
            stmt.setDouble(4, transaction.getAmount());
            stmt.setDouble(5, transaction.getNewBalance());
            stmt.executeUpdate();
            logger.info("Transaction recorded for account: " + transaction.getAccountNumber());
        }
    }

    @Override
    public List<Transaction> getTransactions(String accountNumber) throws SQLException {
        List<Transaction> transactions = new ArrayList<>();
        String sql = "SELECT * FROM transactions WHERE account_number = ? ORDER BY transaction_date DESC";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, accountNumber);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    transactions.add(new Transaction(
                            rs.getString("account_number"),
                            LocalDateTime.parse(rs.getString("transaction_date"), DATE_FORMATTER),
                            rs.getString("type"),
                            rs.getDouble("amount"),
                            rs.getDouble("new_balance")
                    ));
                }
            }
        }
        return transactions;
    }

    @Override
    public boolean updateBalance(String accountNumber, double newBalance) throws SQLException {
        String sql = "UPDATE accounts SET balance = ? WHERE account_number = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setDouble(1, newBalance);
            stmt.setString(2, accountNumber);
            return stmt.executeUpdate() > 0;
        }
    }

    @Override
    public Account findByAccountNumber(String accountNumber) throws SQLException {
        return null;
    }
}