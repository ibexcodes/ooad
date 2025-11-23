package com.example.bankingsystem;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;
import java.util.logging.Logger;


public class UserDAOImpl implements UserDAO {
    private static final Logger logger = Logger.getLogger(UserDAOImpl.class.getName());
    private final Connection connection;

    public UserDAOImpl(Connection connection) {
        this.connection = connection;
    }


    private String hashPasswordSHA256(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            String salt = "static_salt_for_banking_app";
            md.update(salt.getBytes());
            byte[] bytes = md.digest(password.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : bytes) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            logger.log(Level.SEVERE, "SHA-256 algorithm not found.", e);
            return null;
        }
    }

    private boolean checkPasswordSHA256(String plainPassword, String storedHash) {
        String hashedPassword = hashPasswordSHA256(plainPassword);
        return hashedPassword != null && hashedPassword.equals(storedHash);
    }


    @Override
    public void create(User user) {
        String hashedPassword = hashPasswordSHA256(user.getPassword());
        if (hashedPassword == null) {
            logger.log(Level.SEVERE, "Failed to hash password for user: {0}", user.getEmail());
            return;
        }

        String sql = "INSERT INTO users (first_name, surname, email, password) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, user.getFirstName());
            stmt.setString(2, user.getSurname());
            stmt.setString(3, user.getEmail());
            stmt.setString(4, hashedPassword);
            stmt.executeUpdate();
            logger.log(Level.INFO, "User created successfully: {0}", user.getEmail());
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error creating user: {0}", new Object[]{user.getEmail(), e});
        }
    }

    @Override
    public User findByEmailAndPassword(String email, String password) {
        String sql = "SELECT * FROM users WHERE email = ?";
        ResultSet rs = null;
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, email);
            rs = stmt.executeQuery();

            if (rs.next()) {
                String storedHash = rs.getString("password");
                if (checkPasswordSHA256(password, storedHash)) {
                    User user = new User(
                            rs.getInt("id"),
                            rs.getString("first_name"),
                            rs.getString("surname"),
                            rs.getString("email"),
                            storedHash
                    );
                    logger.log(Level.INFO, "User authenticated: {0}", email);
                    return user;
                } else {
                    logger.log(Level.WARNING, "Invalid password for email: {0}", email);
                }
            } else {
                logger.log(Level.WARNING, "No user found with email: {0}", email);
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error finding user with email: {0}", new Object[]{email, e});
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    logger.log(Level.SEVERE, "Error closing ResultSet", e);
                }
            }
        }
        return null;
    }
    public User findByEmail(String email) {
        String sql = "SELECT * FROM users WHERE email = ?";
        ResultSet rs = null;
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, email);
            rs = stmt.executeQuery();

            if (rs.next()) {
                return new User(
                        rs.getInt("id"),
                        rs.getString("first_name"),
                        rs.getString("surname"),
                        rs.getString("email"),
                        rs.getString("password")
                );
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error finding user by email for reset: {0}", new Object[]{email, e});
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    logger.log(Level.SEVERE, "Error closing ResultSet in findByEmail", e);
                }
            }
        }
        return null;
    }
    public boolean updatePassword(String email, String newPassword) {
        String hashedPassword = hashPasswordSHA256(newPassword);
        if (hashedPassword == null) {
            logger.log(Level.SEVERE, "Failed to hash new password for user: {0}", email);
            return false;
        }

        String sql = "UPDATE users SET password = ? WHERE email = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, hashedPassword);
            stmt.setString(2, email);
            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected > 0) {
                logger.log(Level.INFO, "Password updated successfully for user: {0}", email);
                return true;
            } else {
                logger.log(Level.WARNING, "Password update failed (user not found?): {0}", email);
                return false;
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error updating password for user: {0}", new Object[]{email, e});
            return false;
        }
    }

}