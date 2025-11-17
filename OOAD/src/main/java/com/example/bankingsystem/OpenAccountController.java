package com.example.bankingsystem;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.event.ActionEvent;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import java.util.logging.Level;
import java.util.logging.Logger;

public class OpenAccountController {
    private static final Logger logger = Logger.getLogger(OpenAccountController.class.getName());

    // Account and Deposit
    @FXML private ComboBox<String> accountTypeComboBox;
    @FXML private TextField initialDepositField;
    @FXML private CheckBox individualCheckBox;
    @FXML private CheckBox companyCheckBox;
    @FXML private Text statusText;

    // FXML Declarations for ALL Customer Attributes
    @FXML private TextField customerIDField;
    @FXML private TextField fullNameField;
    @FXML private TextField dateOfBirthField;
    @FXML private TextField genderField;
    @FXML private TextField contactNumberField;
    @FXML private TextField emailAddressField;
    @FXML private TextField residentialAddressField;
    @FXML private TextField nextOfKinField;
    @FXML private TextField occupationField;

    // Type Specific Fields
    @FXML private Label typeSpecificLabel1;
    @FXML private TextField typeSpecificField1;
    @FXML private Label typeSpecificLabel2;
    @FXML private TextField typeSpecificField2;

    private Customer currentCustomer;

    @FXML
    public void initialize() {
        accountTypeComboBox.getItems().addAll("Savings Account", "Cheque Account", "Investment Account");
    }

    @FXML
    private void handleIndividualCheck(ActionEvent event) {
        if (individualCheckBox.isSelected()) {
            companyCheckBox.setSelected(false);
            toggleCustomerFields(true, false);
        } else {
            companyCheckBox.setSelected(false);
            toggleCustomerFields(false, false);
        }
    }

    @FXML
    private void handleCompanyCheck(ActionEvent event) {
        if (companyCheckBox.isSelected()) {
            individualCheckBox.setSelected(false);
            toggleCustomerFields(false, true);
        } else {
            individualCheckBox.setSelected(false);
            toggleCustomerFields(false, false);
        }
    }

    public void initData(Customer customer) {
        this.currentCustomer = customer;

        // 1. Pre-fill General Customer Attributes
        customerIDField.setText(String.valueOf(customer.getCustomerID()));
        fullNameField.setText(customer.getFullName());
        dateOfBirthField.setText(customer.getDateOfBirth());
        genderField.setText(customer.getGender());
        contactNumberField.setText(customer.getContactNumber());
        emailAddressField.setText(customer.getEmailAddress());
        residentialAddressField.setText(customer.getResidentialAddress());
        nextOfKinField.setText(customer.getNextOfKin());
        occupationField.setText(customer.getOccupation());

        // 2. Set Checkbox and Pre-fill Type-Specific Attributes
        if (currentCustomer instanceof IndividualCustomer) {
            individualCheckBox.setSelected(true);
            toggleCustomerFields(true, false);

        } else if (currentCustomer instanceof CompanyCustomer) {
            companyCheckBox.setSelected(true);
            toggleCustomerFields(false, true);
        }
    }

    private void toggleCustomerFields(boolean isIndividual, boolean isCompany) {

        if (currentCustomer == null) return;

        if (isIndividual) {
            IndividualCustomer ic = (IndividualCustomer) currentCustomer;
            typeSpecificLabel1.setText("National ID:");
            typeSpecificField1.setText(ic.getNationalId());

            typeSpecificLabel2.setVisible(false);
            typeSpecificField2.setVisible(false);

        } else if (isCompany) {
            CompanyCustomer cc = (CompanyCustomer) currentCustomer;

            typeSpecificLabel1.setText("Company Name:");
            typeSpecificField1.setText(cc.getCompanyName());

            typeSpecificLabel2.setText("Registration ID/Date of Est:");
            typeSpecificField2.setText(String.format("%s / %s", cc.getCompanyRegistrationId(), cc.getDateOfEstablishment()));

            typeSpecificLabel2.setVisible(true);
            typeSpecificField2.setVisible(true);
        } else {
            typeSpecificLabel1.setText("N/A");
            typeSpecificField1.clear();
            typeSpecificLabel2.setVisible(false);
            typeSpecificField2.setVisible(false);
        }

        typeSpecificLabel1.setVisible(isIndividual || isCompany);
        typeSpecificField1.setVisible(isIndividual || isCompany);
    }

    @FXML
    private void handleOpenAccount(ActionEvent event) {
        String accountType = accountTypeComboBox.getValue();

        if (currentCustomer == null || accountType == null || (!individualCheckBox.isSelected() && !companyCheckBox.isSelected())) {
            statusText.setText("Please log in, select account type, and customer type.");
            return;
        }

        double initialDeposit;
        try {
            initialDeposit = Double.parseDouble(initialDepositField.getText());
            if (initialDeposit <= 0) {
                statusText.setText("Initial deposit must be a positive amount.");
                return;
            }
        } catch (NumberFormatException e) {
            statusText.setText("Invalid amount entered for initial deposit.");
            return;
        }

        // Conditional Minimum Deposit Check for Investment Account (using BWP currency)
        if (accountType.equals("Investment Account") && initialDeposit < InvestmentAccount.MIN_INITIAL_DEPOSIT) {
            statusText.setText(String.format("Investment Account deposit must be at least BWP%.2f", InvestmentAccount.MIN_INITIAL_DEPOSIT));
            return;
        }

        // --- ACCOUNT CREATION AND LINKING ---
        Account newAccount = null;

        try {
            if (accountType.equals("Savings Account")) {
                newAccount = new SavingsAccount(currentCustomer, initialDeposit);

            } else if (accountType.equals("Cheque Account")) {
                String nameForCheque;
                String companyAddress = residentialAddressField.getText();

                if (currentCustomer instanceof CompanyCustomer) {
                    nameForCheque = ((CompanyCustomer) currentCustomer).getCompanyName();
                } else {
                    nameForCheque = fullNameField.getText();
                }

                newAccount = new ChequeAccount(currentCustomer, initialDeposit, nameForCheque, companyAddress);

            } else if (accountType.equals("Investment Account")) {
                newAccount = new InvestmentAccount(currentCustomer, initialDeposit);
            }

            else {
                statusText.setText("Invalid account type selected.");
                return;
            }

            if (newAccount != null) {
                currentCustomer.addAccount(newAccount);

                String successMessage = String.format("%s opened successfully! Account Number: %s. Current Balance: BWP%.2f",
                        newAccount.getClass().getSimpleName(),
                        newAccount.getAccountNumber(),
                        newAccount.getBalance());
                logger.log(Level.INFO, successMessage);
                statusText.setText(successMessage);
                statusText.setFill(javafx.scene.paint.Color.GREEN);
            }

        } catch (Exception e) {
            logger.log(Level.SEVERE, "Account opening failed.", e);
            statusText.setText("An unexpected error occurred during account opening.");
        }
    }

    @FXML
    private void handleCancel(ActionEvent event) {
        Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
        stage.close();
    }
}