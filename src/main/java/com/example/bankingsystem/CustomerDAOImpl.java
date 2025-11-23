package com.example.bankingsystem;

import java.sql.*;
import java.util.logging.Logger;

public class CustomerDAOImpl implements CustomerDAO {
    private static final Logger logger = Logger.getLogger(CustomerDAOImpl.class.getName());
    private final Connection connection;

    public CustomerDAOImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void create(Customer customer, int userId) throws SQLException {
        String sql = "INSERT INTO customers (user_id, national_id, full_name, date_of_birth, gender, contact_number, email_address, residential_address, next_of_kin, occupation) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, userId);
            stmt.setString(2, customer.getNationalId());
            stmt.setString(3, customer.getFullName());
            stmt.setString(4, customer.getDateOfBirth());
            stmt.setString(5, customer.getGender());
            stmt.setString(6, customer.getContactNumber());
            stmt.setString(7, customer.getEmailAddress());
            stmt.setString(8, customer.getResidentialAddress());
            stmt.setString(9, customer.getNextOfKin());
            stmt.setString(10, customer.getOccupation());

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        customer.setCustomerID(generatedKeys.getInt(1));
                    }
                }
                logger.info("New customer profile created and linked to user_id: " + userId);
            }
        }
    }

    @Override
    public Customer findByUserId(int userId) throws SQLException {
        String sql = "SELECT * FROM customers WHERE user_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new IndividualCustomer(
                            rs.getInt("customer_id"),
                            rs.getString("full_name"),
                            rs.getString("date_of_birth"),
                            rs.getString("gender"),
                            rs.getString("national_id"),
                            rs.getString("contact_number"),
                            rs.getString("email_address"),
                            rs.getString("residential_address"),
                            rs.getString("next_of_kin"),
                            rs.getString("occupation")
                    );
                }
            }
        }
        return null; 
    }

    @Override
    public boolean update(Customer customer) throws SQLException {
        String sql = "UPDATE customers SET contact_number = ?, residential_address = ?, next_of_kin = ?, occupation = ? WHERE customer_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, customer.getContactNumber());
            stmt.setString(2, customer.getResidentialAddress());
            stmt.setString(3, customer.getNextOfKin());
            stmt.setString(4, customer.getOccupation());
            stmt.setInt(5, customer.getCustomerID());

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        }
    }
}
