package com.example.bankingsystem;

public class InvestmentAccount extends Account implements InterestCalculation, Withdrawal {

    public static final double MIN_INITIAL_DEPOSIT = 500.00;
    private static final double INVESTMENT_RATE = 0.05;

    public InvestmentAccount(Customer customer, double initialDeposit) {
        super(customer, initialDeposit);
    }

    @Override
    public double getInterestRate() {
        return INVESTMENT_RATE;
    }

    @Override
    public double calculateInterest(double rate) {
        return accountBalance * INVESTMENT_RATE;
    }

    @Override
    public double getAppliedInterest() {
        return getBalance() + calculateInterest(INVESTMENT_RATE);
    }

    @Override
    public String getAccountSummary() {
        double interest = calculateInterest(INVESTMENT_RATE);
        return String.format(
                "• Acct No: %s\n• Balance: BWP%.2f\n• Rate: %.1f%%\n• Est. Interest: BWP%.2f\n• Applied Bal: BWP%.2f",
                getAccountNumber(),
                getBalance(),
                INVESTMENT_RATE * 100,
                interest,
                getAppliedInterest()
        );
    }

    @Override
    public void withdraw(double amount) {
        if (accountBalance >= amount) {
            accountBalance -= amount;
        }
    }
}