
import java.util.ArrayList;
import java.util.List;


public abstract class Account {
    protected String accountNumber;
    protected double accountBalance;
    protected String branch;
    protected static final double MIN_INITIAL_DEPOSIT = 500.0;

    public void depositFunds(double amount) {
        accountBalance += amount;
    }


    public double getBalance() {
        return accountBalance;
    }

    public List<Transaction> viewHistory() {
        // Return transaction history
        return new ArrayList<>();
    }
}
