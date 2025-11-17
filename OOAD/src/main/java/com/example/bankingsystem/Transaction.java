package com.example.bankingsystem;

import java.time.LocalDateTime;

public class Transaction {
    private String accountNumber;
    private LocalDateTime date;
    private String type; // e.g., "Deposit", "Withdrawal", "Interest"
    private double amount;
    private double newBalance;

    public Transaction(String accountNumber, LocalDateTime date, String type, double amount, double newBalance) {
        this.accountNumber = accountNumber;
        this.date = date;
        this.type = type;
        this.amount = amount;
        this.newBalance = newBalance;
    }

    // Getters for TableView columns
    public LocalDateTime getDate() { return date; }
    public String getAccountNumber() { return accountNumber; }
    public String getType() { return type; }
    public double getAmount() { return amount; }
    public double getNewBalance() { return newBalance; }
}