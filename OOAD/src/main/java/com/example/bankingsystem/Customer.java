package com.example.bankingsystem;

public abstract class Customer {
    private int customerID;
    private String fullName;
    private String dateOfBirth;
    private String gender;
    private String nationalId;
    private String contactNumber;
    private String emailAddress;
    private String residentialAddress;
    private String nextOfKin;
    private String occupation;

    public Customer(int customerID, String fullName, String dateOfBirth, String gender,
                    String nationalId, String contactNumber, String emailAddress,
                    String residentialAddress, String nextOfKin, String occupation) {
        this.customerID = customerID;
        this.fullName = fullName;
        this.dateOfBirth = dateOfBirth;
        this.gender = gender;
        this.nationalId = nationalId;
        this.contactNumber = contactNumber;
        this.emailAddress = emailAddress;
        this.residentialAddress = residentialAddress;
        this.nextOfKin = nextOfKin;
        this.occupation = occupation;
    }

    // Getters
    public int getCustomerID() { return customerID; }
    public String getFullName() { return fullName; }
    public String getDateOfBirth() { return dateOfBirth; }
    public String getGender() { return gender; }
    public String getNationalId() { return nationalId; }
    public String getContactNumber() { return contactNumber; }
    public String getEmailAddress() { return emailAddress; }
    public String getResidentialAddress() { return residentialAddress; }
    public String getNextOfKin() { return nextOfKin; }
    public String getOccupation() { return occupation; }

    // Abstract methods
    public abstract boolean authenticate(String username, String password);
    public abstract void requestOpenAccount(String type, double initialDeposit);
}
