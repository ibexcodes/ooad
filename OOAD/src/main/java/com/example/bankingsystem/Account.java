package com.example.bankingsystem;

import java.util.ArrayList;
import java.util.List;

public abstract class Account {
    protected Customer customer;
    protected String accountNumber;
    protected double accountBalance;
    protected String branch;

    // Constant is now defined here for use in InvestmentAccount logic
    // InvestmentAccount can also define it for local use or clarity if needed
    // private static final double MIN_INITIAL_DEPOSIT = 500.0;

    // NEW CONSTRUCTOR: Requires Customer and initial deposit
    public Account(Customer customer, double initialDeposit) {
        this.customer = customer;
        this.accountBalance = initialDeposit;
        this.accountNumber = generateUniqueAccountNumber();
    }

    public void depositFunds(double amount) {
        accountBalance += amount;
    }

    public double getBalance() {
        return accountBalance;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public List<Transaction> viewHistory() {
        // Return transaction history
        return new ArrayList<>();
    }

    private String generateUniqueAccountNumber() {
        // Simple mock implementation for a unique account number
        return "ACC" + System.currentTimeMillis() + "-" + (int)(Math.random() * 1000);
    }
}