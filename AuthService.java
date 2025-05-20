package com.mindease.service;

import com.mindease.dao.UserDAO;
import com.mindease.model.User;
import com.mindease.util.SecurityUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * Service for user authentication and account management
 */
public class AuthService {
    private static final Logger logger = LoggerFactory.getLogger(AuthService.class);
    
    private final UserDAO userDAO;
    private User currentUser;
    
    public AuthService() {
        this.userDAO = new UserDAO();
    }
    
    /**
     * Registers a new user
     * @param username Username
     * @param password Plain text password
     * @param email Email address
     * @return The created user or null if registration failed
     */
    public User register(String username, String password, String email) {
        // Check if username already exists
        if (userDAO.findByUsername(username).isPresent()) {
            logger.warn("Registration failed: Username already exists: {}", username);
            return null;
        }
        
        // Create new user
        User user = new User();
        user.setUsername(username);
        user.setPasswordHash(SecurityUtil.hashPassword(password));
        user.setEmail(email);
        user.setCreatedAt(LocalDateTime.now());
        
        User createdUser = userDAO.create(user);
        
        if (createdUser != null) {
            logger.info("User registered successfully: {}", username);
        } else {
            logger.error("Failed to register user: {}", username);
        }
        
        return createdUser;
    }
    
    /**
     * Authenticates a user
     * @param username Username
     * @param password Plain text password
     * @return true if authentication is successful
     */
    public boolean login(String username, String password) {
        Optional<User> userOpt = userDAO.findByUsername(username);
        
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            
            if (SecurityUtil.verifyPassword(password, user.getPasswordHash())) {
                // Update last login time
                user.setLastLogin(LocalDateTime.now());
                userDAO.updateLastLogin(user.getId());
                
                // Set current user
                currentUser = user;
                
                logger.info("User logged in: {}", username);
                return true;
            }
        }
        
        logger.warn("Login failed for username: {}", username);
        return false;
    }
    
    /**
     * Logs out the current user
     */
    public void logout() {
        if (currentUser != null) {
            logger.info("User logged out: {}", currentUser.getUsername());
            currentUser = null;
        }
    }
    
    /**
     * Changes a user's password
     * @param userId User ID
     * @param currentPassword Current password
     * @param newPassword New password
     * @return true if password change is successful
     */
    public boolean changePassword(Long userId, String currentPassword, String newPassword) {
        Optional<User> userOpt = userDAO.findById(userId);
        
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            
            if (SecurityUtil.verifyPassword(currentPassword, user.getPasswordHash())) {
                String newPasswordHash = SecurityUtil.hashPassword(newPassword);
                boolean updated = userDAO.updatePassword(userId, newPasswordHash);
                
                if (updated) {
                    logger.info("Password changed for user: {}", user.getUsername());
                    return true;
                }
            }
        }
        
        logger.warn("Password change failed for user ID: {}", userId);
        return false;
    }
    
    /**
     * Updates a user's profile information
     * @param user User with updated information
     * @return true if update is successful
     */
    public boolean updateProfile(User user) {
        boolean updated = userDAO.update(user);
        
        if (updated) {
            // Update current user if it's the same user
            if (currentUser != null && currentUser.getId().equals(user.getId())) {
                currentUser = user;
            }
            
            logger.info("Profile updated for user: {}", user.getUsername());
        } else {
            logger.warn("Profile update failed for user: {}", user.getUsername());
        }
        
        return updated;
    }
    
    /**
     * Gets the current logged-in user
     * @return Current user or null if no user is logged in
     */
    public User getCurrentUser() {
        return currentUser;
    }
    
    /**
     * Checks if a user is currently logged in
     * @return true if a user is logged in
     */
    public boolean isLoggedIn() {
        return currentUser != null;
    }
}