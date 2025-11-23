package com.example.bankingsystem;

import java.sql.SQLException;
import java.util.List;

public interface AccountDAO {
    void create(Account account) throws SQLException;
    List<Account> findByCustomerId(int customerId) throws SQLException; 
    void recordTransaction(Transaction transaction) throws SQLException; 
    List<Transaction> getTransactions(String accountNumber) throws SQLException; 
    boolean updateBalance(String accountNumber, double newBalance) throws SQLException;
    Account findByAccountNumber(String accountNumber) throws SQLException;
}