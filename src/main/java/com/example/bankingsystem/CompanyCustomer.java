package com.example.bankingsystem;

public class CompanyCustomer extends Customer {
    private String companyName;
    private String companyRegistrationId;
    private String dateOfEstablishment;

    public CompanyCustomer(int customerID, String fullName, String dateOfBirth, String gender,
                           String nationalId, String contactNumber, String emailAddress,
                           String residentialAddress, String nextOfKin, String occupation,
                           String companyName, String companyRegistrationId, String dateOfEstablishment) {
        super(customerID, fullName, dateOfBirth, gender, nationalId, contactNumber, emailAddress,
                residentialAddress, nextOfKin, occupation);
        this.companyName = companyName;
        this.companyRegistrationId = companyRegistrationId;
        this.dateOfEstablishment = dateOfEstablishment;
    }

    public String getCompanyName() { return companyName; }
    public String getCompanyRegistrationId() { return companyRegistrationId; }
    public String getDateOfEstablishment() { return dateOfEstablishment; }

}