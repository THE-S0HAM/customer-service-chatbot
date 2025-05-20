package com.mindease.controller;

import com.mindease.model.User;
import com.mindease.service.AuthService;
import javafx.fxml.FXML;
import javafx.scene.control.TabPane;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Controller for the main application view
 */
public class MainController {
    private static final Logger logger = LoggerFactory.getLogger(MainController.class);
    
    private final AuthService authService;
    
    @FXML
    private TabPane tabPane;
    
    public MainController() {
        this.authService = new AuthService();
    }
    
    /**
     * Initializes the controller
     */
    @FXML
    public void initialize() {
        logger.info("Initializing MainController");
        
        // Initialize child controllers
        initializeControllers();
    }
    
    /**
     * Initializes child controllers with the current user
     */
    private void initializeControllers() {
        User currentUser = authService.getCurrentUser();
        if (currentUser == null) {
            logger.warn("No user is currently logged in");
            return;
        }
        
        Long userId = currentUser.getId();
        
        // Get controllers from child views and set the current user ID
        MoodTrackerController moodController = getMoodTrackerController();
        if (moodController != null) {
            moodController.setCurrentUserId(userId);
        }
        
        JournalController journalController = getJournalController();
        if (journalController != null) {
            journalController.setCurrentUserId(userId);
        }
        
        CBTController cbtController = getCBTController();
        if (cbtController != null) {
            cbtController.setCurrentUserId(userId);
        }
        
        GoalController goalController = getGoalController();
        if (goalController != null) {
            goalController.setCurrentUserId(userId);
        }
    }
    
    /**
     * Gets the mood tracker controller
     */
    private MoodTrackerController getMoodTrackerController() {
        return (MoodTrackerController) tabPane.getTabs().get(1).getContent().getProperties().get("controller");
    }
    
    /**
     * Gets the journal controller
     */
    private JournalController getJournalController() {
        return (JournalController) tabPane.getTabs().get(2).getContent().getProperties().get("controller");
    }
    
    /**
     * Gets the CBT controller
     */
    private CBTController getCBTController() {
        return (CBTController) tabPane.getTabs().get(3).getContent().getProperties().get("controller");
    }
    
    /**
     * Gets the goal controller
     */
    private GoalController getGoalController() {
        return (GoalController) tabPane.getTabs().get(5).getContent().getProperties().get("controller");
    }
    
    /**
     * Sets the current user and initializes controllers
     */
    public void setCurrentUser(User user) {
        // Initialize controllers with the user
        initializeControllers();
    }
}