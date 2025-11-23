package com.example.bankingsystem;

import java.util.UUID;

public abstract class Account {
    private String accountNumber;
    protected double accountBalance;
    private Customer customer;

    public Account(Customer customer, double initialDeposit) {
        this.accountNumber = UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        this.accountBalance = initialDeposit;
        this.customer = customer;
    }

    public abstract String getAccountSummary();
    public abstract double getInterestRate(); 

    public double getAppliedInterest() {
        return accountBalance;
    }

    public String getAccountNumber() { return accountNumber; }
    public double getBalance() { return accountBalance; }
    public Customer getCustomer() { return customer; }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }
    public void setAccountBalance(double newBalance) {
        this.accountBalance = newBalance;
    }
}