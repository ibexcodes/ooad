public abstract class Customer {
    protected int customerID;
    protected String address;

// constructor
public abstract boolean authenticate(String username, String password);

public abstract void requestOpenAccount(String type, double initialDeposit);
}