package com.example.bankingsystem;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.event.ActionEvent;
import javafx.scene.text.Text;
import javafx.scene.paint.Color;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;

public class OpenAccountController {
    private static final Logger logger = Logger.getLogger(OpenAccountController.class.getName());
    private DashboardController dashboardController;

    @FXML private ComboBox<String> accountTypeComboBox;
    @FXML private TextField initialDepositField;
    @FXML private CheckBox individualCheckBox;
    @FXML private CheckBox companyCheckBox;
    @FXML private Text statusText;

    @FXML private TextField fullNameField;
    @FXML private TextField dateOfBirthField;
    @FXML private TextField genderField;
    @FXML private TextField contactNumberField;
    @FXML private TextField emailAddressField;
    @FXML private TextField residentialAddressField;
    @FXML private TextField nextOfKinField;
    @FXML private TextField occupationField;

    @FXML private Label typeSpecificLabel1; 
    @FXML private TextField typeSpecificField1;
    @FXML private Label typeSpecificLabel2; 
    @FXML private TextField typeSpecificField2;

    private User currentUser;
    private Customer currentCustomer;

    @FXML
    public void initialize() {
        accountTypeComboBox.getItems().addAll("Savings Account", "Cheque Account", "Investment Account");
        individualCheckBox.setSelected(true);
        handleIndividualCheck(null);
    }

    public void initData(User user, Customer customer, DashboardController dashboardController) {
        this.currentUser = user;
        this.currentCustomer = customer;
        this.dashboardController = dashboardController;

        if (user != null) {
            fullNameField.setText(user.getFirstName() + " " + user.getSurname());
            emailAddressField.setText(user.getEmail());
        }

        if (currentCustomer != null) {
            fullNameField.setEditable(false);
            emailAddressField.setEditable(false);
            dateOfBirthField.setEditable(true);
            typeSpecificField1.setEditable(true);
        }
    }


    @FXML
    private void handleIndividualCheck(ActionEvent event) {
        if (individualCheckBox.isSelected()) {
            companyCheckBox.setSelected(false);
            typeSpecificLabel1.setText("National ID:");
            typeSpecificField1.setPromptText("Enter National ID");
            typeSpecificField1.setVisible(true);

            typeSpecificLabel2.setVisible(false);
            typeSpecificField2.setVisible(false);
        } else {
            companyCheckBox.setSelected(true);
            handleCompanyCheck(null);
        }
    }

    @FXML
    private void handleCompanyCheck(ActionEvent event) {
        if (companyCheckBox.isSelected()) {
            individualCheckBox.setSelected(false);
            typeSpecificLabel1.setText("Tax/Reg ID:");
            typeSpecificField1.setPromptText("Enter Tax ID");

            typeSpecificLabel2.setVisible(true);
            typeSpecificLabel2.setText("Date Est.:");
            typeSpecificField2.setVisible(true);
            typeSpecificField2.setPromptText("YYYY-MM-DD");
        } else {
            individualCheckBox.setSelected(true);
            handleIndividualCheck(null);
        }
    }


    @FXML
    private void handleOpenAccount(ActionEvent event) {
        String accountType = accountTypeComboBox.getValue();
        String depositText = initialDepositField.getText();

        if (accountType == null || depositText.isEmpty()) {
            statusText.setText("Please select an account type and enter a deposit.");
            statusText.setFill(Color.RED);
            return;
        }

        double initialDeposit;
        try {
            initialDeposit = Double.parseDouble(depositText);
        } catch (NumberFormatException e) {
            statusText.setText("Invalid deposit amount.");
            statusText.setFill(Color.RED);
            return;
        }

        try (Connection conn = CustomerDBConnection.getConnection()) {
            CustomerDAO customerDAO = new CustomerDAOImpl(conn);
            AccountDAO accountDAO = new AccountDAOImpl(conn);

            if (currentCustomer == null) {
                if (contactNumberField.getText().isEmpty() || residentialAddressField.getText().isEmpty() || typeSpecificField1.getText().isEmpty()) {
                    statusText.setText("Please fill in all address/contact details and ID fields.");
                    statusText.setFill(Color.RED);
                    return;
                }

                if (currentUser == null) {
                    statusText.setText("System Error: User data missing.");
                    statusText.setFill(Color.RED);
                    return;
                }


                if (individualCheckBox.isSelected()) {
                    currentCustomer = new IndividualCustomer(
                            0, fullNameField.getText(), dateOfBirthField.getText(), genderField.getText(),
                            typeSpecificField1.getText(), 
                            contactNumberField.getText(), emailAddressField.getText(),
                            residentialAddressField.getText(), nextOfKinField.getText(), occupationField.getText()
                    );
                } else {
                    currentCustomer = new CompanyCustomer(
                            0, fullNameField.getText(), dateOfBirthField.getText(), genderField.getText(),
                            "N/A", 
                            contactNumberField.getText(), emailAddressField.getText(),
                            residentialAddressField.getText(), nextOfKinField.getText(), occupationField.getText(),
                            fullNameField.getText(), 
                            typeSpecificField1.getText(), 
                            typeSpecificField2.getText()  
                    );
                }
                customerDAO.create(currentCustomer, currentUser.getId());
            }

            Account newAccount = null;

            if (accountType.equals("Savings Account")) {
                newAccount = new SavingsAccount(currentCustomer, initialDeposit);
            } else if (accountType.equals("Cheque Account")) {
                newAccount = new ChequeAccount(currentCustomer, initialDeposit, currentCustomer.getFullName(), currentCustomer.getResidentialAddress());
            } else if (accountType.equals("Investment Account")) {
                if (initialDeposit < InvestmentAccount.MIN_INITIAL_DEPOSIT) {
                    statusText.setText(String.format("Investment requires minimum BWP %.2f", InvestmentAccount.MIN_INITIAL_DEPOSIT));
                    statusText.setFill(Color.RED);
                    return;
                }
                newAccount = new InvestmentAccount(currentCustomer, initialDeposit);
            }

            if (newAccount != null) {
                accountDAO.create(newAccount);

                Transaction tx = new Transaction(newAccount.getAccountNumber(), LocalDateTime.now(), "Initial Deposit", initialDeposit, newAccount.getBalance());
                accountDAO.recordTransaction(tx);

                currentCustomer.addAccount(newAccount);

                String successMessage = String.format("%s opened successfully! Account Number: %s. Current Balance: BWP%.2f",
                        newAccount.getClass().getSimpleName(),
                        newAccount.getAccountNumber(),
                        newAccount.getBalance());
                statusText.setText(successMessage);
                statusText.setFill(Color.GREEN);

                initialDepositField.clear();
                
                if (dashboardController != null) {
                    dashboardController.refreshAccountSummaries();
                }
            }

        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Database error during account opening.", e);
            statusText.setText("Database Error: " + e.getMessage());
            statusText.setFill(Color.RED);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Account opening failed.", e);
            statusText.setText("An unexpected error occurred: " + e.getMessage());
            statusText.setFill(Color.RED);
        }
    }
}