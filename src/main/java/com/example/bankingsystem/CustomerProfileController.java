package com.example.bankingsystem;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;
import javafx.event.ActionEvent;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CustomerProfileController {
    private static final Logger logger = Logger.getLogger(CustomerProfileController.class.getName());

    @FXML private TextField fullName; 
    @FXML private TextField dateOfBirth; 
    @FXML private TextField gender; 
    @FXML private TextField nationalId; 
    @FXML private TextField emailAddress; 

    @FXML private TextField contactNumber;
    @FXML private TextField residentialAddress;
    @FXML private TextField nextOfKin;
    @FXML private TextField occupation;


    private Customer currentCustomer;

    public void initData(Customer customer) {
        this.currentCustomer = customer;
        if (currentCustomer != null) {
            loadCustomerDetails();
        }
    }

    private void loadCustomerDetails() {
        fullName.setText(currentCustomer.getFullName());
        dateOfBirth.setText(currentCustomer.getDateOfBirth());
        gender.setText(currentCustomer.getGender());
        nationalId.setText(currentCustomer.getNationalId());
        emailAddress.setText(currentCustomer.getEmailAddress());

        contactNumber.setText(currentCustomer.getContactNumber());
        residentialAddress.setText(currentCustomer.getResidentialAddress());
        nextOfKin.setText(currentCustomer.getNextOfKin());
        occupation.setText(currentCustomer.getOccupation());

        fullName.setEditable(false);
        dateOfBirth.setEditable(false);
    }

    @FXML
    private void handleUpdateProfile(ActionEvent event) {
        currentCustomer.setContactNumber(contactNumber.getText());
        currentCustomer.setResidentialAddress(residentialAddress.getText());
        currentCustomer.setNextOfKin(nextOfKin.getText());
        currentCustomer.setOccupation(occupation.getText());

        try (Connection conn = CustomerDBConnection.getConnection()) {
            CustomerDAO customerDAO = new CustomerDAOImpl(conn);
            if (customerDAO.update(currentCustomer)) {
                showAlert("Success", "Profile updated successfully! Changes will reflect after log out/in.");
            } else {
                showAlert("Error", "Failed to update profile in database. Customer ID may be missing.");
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Database error during profile update.", e);
            showAlert("Database Error", "An error occurred while communicating with the database.");
        }
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert("Success".equals(title) ? Alert.AlertType.INFORMATION : Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
