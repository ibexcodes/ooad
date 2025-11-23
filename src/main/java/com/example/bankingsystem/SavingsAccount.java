package com.example.bankingsystem;

public class SavingsAccount extends Account implements InterestCalculation {
    private static final double INDIVIDUAL_RATE = 0.025;
    private static final double COMPANY_RATE = 0.075;

    public SavingsAccount(Customer customer, double initialDeposit) {
        super(customer, initialDeposit);
    }

    private double getRateBasedOnCustomer(Customer customer) {
        if (customer instanceof IndividualCustomer) {
            return INDIVIDUAL_RATE;
        } else if (customer instanceof CompanyCustomer) {
            return COMPANY_RATE;
        }
        return 0.0;
    }

    @Override
    public double calculateInterest(double rate) {
        return accountBalance * getRateBasedOnCustomer(getCustomer());
    }

    @Override
    public double getInterestRate() {
        return getRateBasedOnCustomer(getCustomer());
    }

    @Override
    public double getAppliedInterest() {
        return getBalance() + calculateInterest(0.0);
    }

    @Override
    public String getAccountSummary() {
        double interest = calculateInterest(0.0);
        return String.format(
                "• Acct No: %s\n• Balance: BWP%.2f\n• Rate: %.1f%%\n• Est. Interest: BWP%.2f\n• Applied Bal: BWP%.2f",
                getAccountNumber(),
                getBalance(),
                getInterestRate() * 100,
                interest,
                getAppliedInterest()
        );
    }
}