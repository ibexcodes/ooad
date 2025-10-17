public class IndividualCustomer extends Customer {
    private String firstName;
    private String surname;
    private String phoneNumber;

    public IndividualCustomer(String firstName, String surname, String phoneNumber) {
        this.firstName = firstName;
        this.surname = surname;
        this.phoneNumber = phoneNumber;
    }

    public String getFirstName() { 
        return firstName; }

    public String getSurname() { 
        return surname; }

    public String getPhoneNumber() { 
        return phoneNumber; }

    @Override
    public boolean authenticate(String username, String password) {
        return true;
    }

    @Override
    public void requestOpenAccount(String type, double initialDeposit) {
    }
}
