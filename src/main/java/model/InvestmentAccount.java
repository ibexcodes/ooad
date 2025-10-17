public class InvestmentAccount extends Account implements InterestCalculation, Withdrawal {
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
