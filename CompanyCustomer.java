public class CompanyCustomer extends Customer {
    private String companyName;
    private String companyRegistrationId;
    private String dateOfEstablishment;

    public CompanyCustomer(String companyName, String companyRegistrationId, String dateOfEstablishment) {
        this.companyName = companyName;
        this.companyRegistrationId = companyRegistrationId;
        this.dateOfEstablishment = dateOfEstablishment;
    }

    public String getCompanyName() { 
        return companyName; }

    public String getCompanyRegistrationId() { 
        return companyRegistrationId; }

    public String getDateOfEstablishment() { 
        return dateOfEstablishment; }

    @Override
    public boolean authenticate(String username, String password) {
        return true;
    }

    @Override
    public void requestOpenAccount(String type, double initialDeposit) {
    }
}
