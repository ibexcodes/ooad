package com.example.bankingsystem;

public class User {
    private int id;
    private String firstName;
    private String surname;
    private String email;
    private String password;

    public User(int id, String firstName, String surname, String email, String password) {
        this.id = id;
        this.firstName = firstName;
        this.surname = surname;
        this.email = email;
        this.password = password;
    }

    public int getId() { return id; }
    public String getFirstName() { return firstName; }
    public String getSurname() { return surname; }
    public String getEmail() { return email; }
    public String getPassword() { return password; }

    public void setId(int id) { this.id = id; }
    public void setFirstName(String firstName) { this.firstName = firstName; }
    public void setSurname(String surname) { this.surname = surname; }
    public void setEmail(String email) { this.email = email; }
    public void setPassword(String password) { this.password = password; }
}