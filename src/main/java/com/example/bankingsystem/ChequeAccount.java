package com.example.bankingsystem;

public class ChequeAccount extends Account implements Withdrawal {
    public static final double OVERDRAFT_LIMIT = 5000.00;
    private String nameForCheque;
    private String companyAddress;

    public ChequeAccount(Customer customer, double initialDeposit, String nameForCheque, String companyAddress) {
        super(customer, initialDeposit);
        this.nameForCheque = nameForCheque;
        this.companyAddress = companyAddress;
    }

    public String getNameForCheque() { return nameForCheque; }
    public String getCompanyAddress() { return companyAddress; }

    @Override
    public double getInterestRate() {
        return 0.0; 
    }

    @Override
    public String getAccountSummary() {
        return String.format(
                "• Acct No: %s\n• Balance: BWP%.2f\n• Overdraft Limit: BWP%.2f\n• Name on Cheque: %s",
                getAccountNumber(),
                getBalance(),
                OVERDRAFT_LIMIT,
                nameForCheque
        );
    }

    @Override
    public void withdraw(double amount) {
        if (accountBalance - amount >= -OVERDRAFT_LIMIT) {
            accountBalance -= amount;
        }

    }
}