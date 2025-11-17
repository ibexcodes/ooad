package com.example.bankingsystem;

public class InvestmentAccount extends Account implements InterestCalculation, Withdrawal {

    // Constant for the minimum deposit check (used by the controller)
    public static final double MIN_INITIAL_DEPOSIT = 500.00;

    // NEW CONSTRUCTOR: Calls the Account base constructor
    public InvestmentAccount(Customer customer, double initialDeposit) {
        super(customer, initialDeposit);
    }

    @Override
    public void withdraw(double amount) {
        if (accountBalance >= amount) {
            accountBalance -= amount;
        }
    }

    @Override
    public double calculateInterest(double rate) {
        return accountBalance * 0.05;
    }
}