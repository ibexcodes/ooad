package com.example.bankingsystem;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.mindrot.jbcrypt.BCrypt;

public class UserDAOImpl implements UserDAO {
    private static final Logger logger = LoggerFactory.getLogger(UserDAOImpl.class);
    private final Connection connection;

    public UserDAOImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void create(User user) {
        String hashedPassword = BCrypt.hashpw(user.getPassword(), BCrypt.gensalt());
        String sql = "INSERT INTO users (first_name, surname, email, password) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, user.getFirstName());
            stmt.setString(2, user.getSurname());
            stmt.setString(3, user.getEmail());
            stmt.setString(4, hashedPassword);
            stmt.executeUpdate();
            logger.info("User created successfully: {}", user.getEmail());
        } catch (SQLException e) {
            logger.error("Error creating user: {}", user.getEmail(), e);
        }
    }

    @Override
    public User findByEmailAndPassword(String email, String password) {
        String sql = "SELECT * FROM users WHERE email = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                String storedHash = rs.getString("password");
                if (BCrypt.checkpw(password, storedHash)) {
                    User user = new User(
                            rs.getInt("id"),
                            rs.getString("first_name"),
                            rs.getString("surname"),
                            rs.getString("email"),
                            storedHash // Do NOT return plain password
                    );
                    logger.info("User authenticated: {}", email);
                    return user;
                } else {
                    logger.warn("Invalid password for email: {}", email);
                }
            } else {
                logger.warn("No user found with email: {}", email);
            }
        } catch (SQLException e) {
            logger.error("Error finding user with email: {}", email, e);
        }
        return null;
    }
}