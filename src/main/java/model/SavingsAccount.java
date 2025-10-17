public class SavingsAccount extends Account implements InterestCalculation {

    @Override
    public double calculateInterest(double rate) {
        return accountBalance * rate;
    }

    public double calculateInterest(Customer customer) {
        double rate = 0.0;
        if (customer instanceof IndividualCustomer) {
            rate = 0.025;
        } else if (customer instanceof CompanyCustomer) {
            rate = 0.075;
        }
        return accountBalance * rate;
    }
}
