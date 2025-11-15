package com.example.bankingsystem;

public interface UserDAO {
    void create(User user);
    User findByEmailAndPassword(String email, String password);
}