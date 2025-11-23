package com.example.bankingsystem;

public class IndividualCustomer extends Customer {
    public IndividualCustomer(int customerID, String fullName, String dateOfBirth, String gender,
                              String nationalId, String contactNumber, String emailAddress,
                              String residentialAddress, String nextOfKin, String occupation) {
        super(customerID, fullName, dateOfBirth, gender, nationalId, contactNumber, emailAddress,
                residentialAddress, nextOfKin, occupation);
    }

}