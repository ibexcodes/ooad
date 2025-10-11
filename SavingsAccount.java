public class SavingsAccount extends Account implements InterestCalculation {

    @Override
    public double calculateInterest(double rate) {
        return accountBalance * rate;
    }
}
