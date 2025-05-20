package com.mindease.dao;

import com.mindease.model.User;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Data Access Object for User entity
 */
public class UserDAO {
    private static final Logger logger = LoggerFactory.getLogger(UserDAO.class);
    
    /**
     * Creates a new user in the database
     * @param user User to create
     * @return User with ID populated
     */
    public User create(User user) {
        String sql = "INSERT INTO users (username, password_hash, email, first_name, last_name) VALUES (?, ?, ?, ?, ?)";
        
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            pstmt.setString(1, user.getUsername());
            pstmt.setString(2, user.getPasswordHash());
            pstmt.setString(3, user.getEmail());
            pstmt.setString(4, user.getFirstName());
            pstmt.setString(5, user.getLastName());
            
            int affectedRows = pstmt.executeUpdate();
            
            if (affectedRows > 0) {
                try (ResultSet rs = pstmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        user.setId(rs.getLong(1));
                    }
                }
            }
            
            logger.info("Created user: {}", user.getUsername());
            return user;
            
        } catch (SQLException e) {
            logger.error("Error creating user", e);
            return null;
        }
    }
    
    /**
     * Finds a user by ID
     * @param id User ID
     * @return Optional containing the user if found
     */
    public Optional<User> findById(Long id) {
        String sql = "SELECT * FROM users WHERE id = ?";
        
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setLong(1, id);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSetToUser(rs));
                }
            }
            
        } catch (SQLException e) {
            logger.error("Error finding user by ID", e);
        }
        
        return Optional.empty();
    }
    
    /**
     * Finds a user by username
     * @param username Username to search for
     * @return Optional containing the user if found
     */
    public Optional<User> findByUsername(String username) {
        String sql = "SELECT * FROM users WHERE username = ?";
        
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, username);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSetToUser(rs));
                }
            }
            
        } catch (SQLException e) {
            logger.error("Error finding user by username", e);
        }
        
        return Optional.empty();
    }
    
    /**
     * Updates a user's information
     * @param user User to update
     * @return true if successful
     */
    public boolean update(User user) {
        String sql = "UPDATE users SET username = ?, email = ?, first_name = ?, last_name = ? WHERE id = ?";
        
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, user.getUsername());
            pstmt.setString(2, user.getEmail());
            pstmt.setString(3, user.getFirstName());
            pstmt.setString(4, user.getLastName());
            pstmt.setLong(5, user.getId());
            
            int affectedRows = pstmt.executeUpdate();
            
            logger.info("Updated user: {}", user.getUsername());
            return affectedRows > 0;
            
        } catch (SQLException e) {
            logger.error("Error updating user", e);
            return false;
        }
    }
    
    /**
     * Updates a user's password
     * @param userId User ID
     * @param passwordHash New password hash
     * @return true if successful
     */
    public boolean updatePassword(Long userId, String passwordHash) {
        String sql = "UPDATE users SET password_hash = ? WHERE id = ?";
        
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, passwordHash);
            pstmt.setLong(2, userId);
            
            int affectedRows = pstmt.executeUpdate();
            
            logger.info("Updated password for user ID: {}", userId);
            return affectedRows > 0;
            
        } catch (SQLException e) {
            logger.error("Error updating password", e);
            return false;
        }
    }
    
    /**
     * Updates the last login time for a user
     * @param userId User ID
     * @return true if successful
     */
    public boolean updateLastLogin(Long userId) {
        String sql = "UPDATE users SET last_login = CURRENT_TIMESTAMP WHERE id = ?";
        
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setLong(1, userId);
            
            int affectedRows = pstmt.executeUpdate();
            
            logger.info("Updated last login for user ID: {}", userId);
            return affectedRows > 0;
            
        } catch (SQLException e) {
            logger.error("Error updating last login", e);
            return false;
        }
    }
    
    /**
     * Maps a ResultSet to a User object
     * @param rs ResultSet containing user data
     * @return User object
     * @throws SQLException if mapping fails
     */
    private User mapResultSetToUser(ResultSet rs) throws SQLException {
        User user = new User();
        user.setId(rs.getLong("id"));
        user.setUsername(rs.getString("username"));
        user.setPasswordHash(rs.getString("password_hash"));
        user.setEmail(rs.getString("email"));
        user.setFirstName(rs.getString("first_name"));
        user.setLastName(rs.getString("last_name"));
        
        Timestamp createdAt = rs.getTimestamp("created_at");
        if (createdAt != null) {
            user.setCreatedAt(createdAt.toLocalDateTime());
        }
        
        Timestamp lastLogin = rs.getTimestamp("last_login");
        if (lastLogin != null) {
            user.setLastLogin(lastLogin.toLocalDateTime());
        }
        
        return user;
    }
}