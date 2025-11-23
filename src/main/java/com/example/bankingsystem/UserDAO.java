package com.example.bankingsystem;

public interface UserDAO {
    void create(User user);
    User findByEmailAndPassword(String email, String password);

    User findByEmail(String email);
    boolean updatePassword(String email, String newPassword);
}