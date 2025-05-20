package com.mindease.controller;

import com.mindease.service.AuthService;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Controller for the login view
 */
public class LoginController {
    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);
    
    private final AuthService authService;
    private LoginCallback callback;
    
    @FXML
    private TextField usernameField;
    
    @FXML
    private PasswordField passwordField;
    
    @FXML
    private Button loginButton;
    
    @FXML
    private Button registerButton;
    
    @FXML
    private Label messageLabel;
    
    public LoginController() {
        this.authService = new AuthService();
    }
    
    /**
     * Initializes the controller
     */
    @FXML
    public void initialize() {
        // Clear any previous messages
        messageLabel.setText("");
        
        // Add listeners to enable/disable login button
        usernameField.textProperty().addListener((observable, oldValue, newValue) -> validateForm());
        passwordField.textProperty().addListener((observable, oldValue, newValue) -> validateForm());
        
        // Initially disable login button
        loginButton.setDisable(true);
    }
    
    /**
     * Validates the form and enables/disables the login button
     */
    private void validateForm() {
        boolean valid = !usernameField.getText().trim().isEmpty() && 
                        !passwordField.getText().trim().isEmpty();
        loginButton.setDisable(!valid);
    }
    
    /**
     * Handles the login button click
     */
    @FXML
    private void handleLogin() {
        String username = usernameField.getText().trim();
        String password = passwordField.getText();
        
        if (authService.login(username, password)) {
            // Login successful
            logger.info("Login successful for user: {}", username);
            showMessage("Login successful", false);
            
            // Notify callback
            if (callback != null) {
                callback.onLoginSuccess();
            }
        } else {
            // Login failed
            logger.warn("Login failed for user: {}", username);
            showMessage("Invalid username or password", true);
            passwordField.clear();
        }
    }
    
    /**
     * Handles the register button click
     */
    @FXML
    private void handleRegister() {
        // Notify callback to switch to registration view
        if (callback != null) {
            callback.onRegisterRequest();
        }
    }
    
    /**
     * Shows a message to the user
     * @param message Message to show
     * @param isError Whether the message is an error
     */
    private void showMessage(String message, boolean isError) {
        messageLabel.setText(message);
        messageLabel.setTextFill(isError ? Color.RED : Color.GREEN);
    }
    
    /**
     * Sets the callback for login events
     * @param callback Callback to set
     */
    public void setCallback(LoginCallback callback) {
        this.callback = callback;
    }
    
    /**
     * Callback interface for login events
     */
    public interface LoginCallback {
        void onLoginSuccess();
        void onRegisterRequest();
    }
}