package com.mindease.controller;

import com.mindease.dao.GoalDAO;
import com.mindease.dao.MoodEntryDAO;
import com.mindease.model.Goal;
import com.mindease.model.MoodEntry;
import com.mindease.model.User;
import com.mindease.service.AuthService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Controller for the dashboard view
 */
public class DashboardController {
    private static final Logger logger = LoggerFactory.getLogger(DashboardController.class);
    
    private final AuthService authService;
    private final MoodEntryDAO moodEntryDAO;
    private final GoalDAO goalDAO;
    
    @FXML
    private Label welcomeLabel;
    
    @FXML
    private Label quoteLabel;
    
    @FXML
    private Label quoteAuthorLabel;
    
    @FXML
    private PieChart moodPieChart;
    
    @FXML
    private VBox goalProgressBox;
    
    public DashboardController() {
        this.authService = new AuthService();
        this.moodEntryDAO = new MoodEntryDAO();
        this.goalDAO = new GoalDAO();
    }
    
    /**
     * Initializes the controller
     */
    @FXML
    public void initialize() {
        logger.info("Initializing DashboardController");
        
        // Set welcome message
        updateWelcomeMessage();
        
        // Load data if user is logged in
        User currentUser = authService.getCurrentUser();
        if (currentUser != null) {
            loadUserData(currentUser.getId());
        }
    }
    
    /**
     * Updates the welcome message based on the current user
     */
    private void updateWelcomeMessage() {
        User currentUser = authService.getCurrentUser();
        if (currentUser != null) {
            String firstName = currentUser.getFirstName();
            if (firstName != null && !firstName.isEmpty()) {
                welcomeLabel.setText("Welcome back, " + firstName + "!");
            } else {
                welcomeLabel.setText("Welcome back, " + currentUser.getUsername() + "!");
            }
        } else {
            welcomeLabel.setText("Welcome to MindEase!");
        }
    }
    
    /**
     * Loads user data for the dashboard
     */
    public void loadUserData(Long userId) {
        // Load mood data
        loadMoodData(userId);
        
        // Load goal data
        loadGoalData(userId);
    }
    
    /**
     * Loads mood data for the pie chart
     */
    private void loadMoodData(Long userId) {
        // Get mood entries from the last 30 days
        LocalDateTime thirtyDaysAgo = LocalDateTime.now().minusDays(30);
        List<MoodEntry> entries = moodEntryDAO.findByUserIdAndDateRange(userId, thirtyDaysAgo, LocalDateTime.now());
        
        // Count occurrences of each mood
        Map<String, Integer> moodCounts = new HashMap<>();
        for (MoodEntry entry : entries) {
            String mood = entry.getMood().toString();
            moodCounts.put(mood, moodCounts.getOrDefault(mood, 0) + 1);
        }
        
        // Create pie chart data
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();
        for (Map.Entry<String, Integer> entry : moodCounts.entrySet()) {
            pieChartData.add(new PieChart.Data(entry.getKey(), entry.getValue()));
        }
        
        moodPieChart.setData(pieChartData);
        moodPieChart.setTitle("Mood Distribution (Last 30 Days)");
    }
    
    /**
     * Loads goal data for the progress display
     */
    private void loadGoalData(Long userId) {
        List<Goal> goals = goalDAO.findByUserId(userId);
        
        // Clear existing content
        goalProgressBox.getChildren().clear();
        
        // Add up to 5 active goals
        int count = 0;
        for (Goal goal : goals) {
            if (count >= 5) break;
            
            // Create goal progress display
            HBox goalBox = new HBox(10);
            
            Label titleLabel = new Label(goal.getTitle());
            titleLabel.setPrefWidth(150);
            
            ProgressBar progressBar = new ProgressBar();
            progressBar.setPrefWidth(150);
            progressBar.setProgress((double) goal.getCurrentProgress() / goal.getTargetValue());
            progressBar.getStyleClass().add("goal-progress-bar");
            
            Label progressLabel = new Label(goal.getCurrentProgress() + "/" + goal.getTargetValue());
            
            goalBox.getChildren().addAll(titleLabel, progressBar, progressLabel);
            
            if (goal.isCompleted()) {
                Label completedLabel = new Label("✓");
                completedLabel.getStyleClass().add("goal-complete");
                goalBox.getChildren().add(completedLabel);
            }
            
            goalProgressBox.getChildren().add(goalBox);
            count++;
        }
        
        // Add message if no goals
        if (goals.isEmpty()) {
            Label noGoalsLabel = new Label("No goals set. Create some goals to track your progress!");
            goalProgressBox.getChildren().add(noGoalsLabel);
        }
    }
    
    /**
     * Sets the current user and refreshes the dashboard
     */
    public void setCurrentUser(User user) {
        updateWelcomeMessage();
        if (user != null) {
            loadUserData(user.getId());
        }
    }
}