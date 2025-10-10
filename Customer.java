public abstract class Customer {
    protected int customerID;
    protected String address;

    public abstract boolean authenticate(String username, String password);
    public abstract void requestOpenAccount(String type, double initialDeposit);
}
class IndividualCustomer extends Customer {
    private final String firstName;
    private final String surname;
    private final String phoneNumber;

    public IndividualCustomer(String firstName, String surname, String phoneNumber) {
        this.firstName = firstName;
        this.surname = surname;
        this.phoneNumber = phoneNumber;
    }

    public String getFirstName() { return firstName; }
    public String getSurname() { return surname; }
    public String getPhoneNumber() { return phoneNumber; }

    @Override
    public boolean authenticate(String username, String password) {
        return true;
    }

    @Override
    public void requestOpenAccount(String type, double initialDeposit) {
    }
}

class CompanyCustomer extends Customer {
    private final String companyName;
    private final String companyRegistrationId;
    private final String dateOfEstablishment;

    public CompanyCustomer(String companyName, String companyRegistrationId, String dateOfEstablishment) {
        this.companyName = companyName;
        this.companyRegistrationId = companyRegistrationId;
        this.dateOfEstablishment = dateOfEstablishment;
    }

    public String getCompanyName() { return companyName; }
    public String getCompanyRegistrationId() { return companyRegistrationId; }
    public String getDateOfEstablishment() { return dateOfEstablishment; }

    @Override
    public boolean authenticate(String username, String password) {
        return true;
    }

    @Override
    public void requestOpenAccount(String type, double initialDeposit) {
    }
}