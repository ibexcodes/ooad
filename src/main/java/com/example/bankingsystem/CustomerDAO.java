package com.example.bankingsystem;

import java.sql.SQLException;

public interface CustomerDAO {
    void create(Customer customer, int userId) throws SQLException; 
    Customer findByUserId(int userId) throws SQLException;
    boolean update(Customer customer) throws SQLException; 
}
