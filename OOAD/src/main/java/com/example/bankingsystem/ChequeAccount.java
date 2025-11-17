package com.example.bankingsystem;

public class ChequeAccount extends Account implements Withdrawal {
    private String nameForCheque;
    private String companyAddress;

    // NEW CONSTRUCTOR: Calls the Account base constructor
    public ChequeAccount(Customer customer, double initialDeposit, String nameForCheque, String companyAddress) {
        super(customer, initialDeposit);
        this.nameForCheque = nameForCheque;
        this.companyAddress = companyAddress;
    }

    public String getNameForCheque() { return nameForCheque; }
    public String getCompanyAddress() { return companyAddress; }

    @Override
    public void withdraw(double amount) {
        if (accountBalance >= amount) {
            accountBalance -= amount;
        }
    }
}