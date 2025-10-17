package model;

public class Teller {
    private String employeeID;
    private String name;

    public Teller(String employeeID, String name) {
        this.employeeID = employeeID;
        this.name = name;
    }

    public String getEmployeeID() { return employeeID; }
    public String getName() { return name; }

    public Account openAccount(Customer customer, String type, double initialDeposit) {
        Account account = null;
        if (type.equalsIgnoreCase("savings")) {
            account = new SavingsAccount();
        } else if (type.equalsIgnoreCase("investment")) {
            account = new InvestmentAccount();
        } else if (type.equalsIgnoreCase("cheque")) {
            account = new ChequeAccount("CompanyName", "CompanyAddress");
        }
        if (account != null && initialDeposit >= Account.MIN_INITIAL_DEPOSIT) {
            account.depositFunds(initialDeposit);
        }
        return account;
    }
}
