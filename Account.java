
import java.util.ArrayList;
import java.util.List;

interface InterestCalculation {
    double calculateInterest(double rate);
}

public abstract class Account {
    protected String accountNumber;
    protected double accountBalance;
    protected String branch;
    protected static final double MIN_INITIAL_DEPOSIT = 500.0;

    public void depositFunds(double amount) {
        accountBalance += amount;
    }

    public abstract void withdraw(double amount);

    public double getBalance() {
        return accountBalance;
    }

    public List<Transaction> viewHistory() {
        // Return transaction history
        return new ArrayList<>();
    }
}
class SavingsAccount extends Account implements InterestCalculation {
    @Override
    public void withdraw(double amount) {
        if (accountBalance >= amount) {
            accountBalance -= amount;
        }
    }

    @Override
    public double calculateInterest(double rate) {
        return accountBalance * rate;
    }
}
class InvestmentAccount extends Account implements InterestCalculation {
    @Override
    public void withdraw(double amount) {
        if (accountBalance >= amount) {
            accountBalance -= amount;
        }
    }

    @Override
    public double calculateInterest(double rate) {
        return accountBalance * rate;
    }
}
class ChequeAccount extends Account {
    private final String companyName;
    private final String companyAddress;

    public ChequeAccount(String companyName, String companyAddress) {
        this.companyName = companyName;
        this.companyAddress = companyAddress;
    }

    public String getCompanyName() { return companyName; }
    public String getCompanyAddress() { return companyAddress; }

    @Override
    public void withdraw(double amount) {
        if (accountBalance >= amount) {
            accountBalance -= amount;
        }
    }
}
