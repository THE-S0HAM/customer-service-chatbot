package com.mindease.controller;

import com.mindease.dao.GoalDAO;
import com.mindease.model.Goal;
import com.mindease.model.Goal.GoalCategory;
import com.mindease.model.Goal.GoalFrequency;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Controller for the goal setting and habit tracking feature
 */
public class GoalController {
    
    private GoalDAO goalDAO;
    private Long currentUserId;
    
    @FXML
    private TextField titleField;
    
    @FXML
    private TextArea descriptionArea;
    
    @FXML
    private ComboBox<GoalCategory> categoryComboBox;
    
    @FXML
    private ComboBox<GoalFrequency> frequencyComboBox;
    
    @FXML
    private Spinner<Integer> targetValueSpinner;
    
    @FXML
    private DatePicker startDatePicker;
    
    @FXML
    private DatePicker endDatePicker;
    
    @FXML
    private ListView<Goal> goalsListView;
    
    @FXML
    private VBox progressBox;
    
    /**
     * Initializes the controller
     */
    @FXML
    public void initialize() {
        goalDAO = new GoalDAO();
        
        // Set up category combo box
        categoryComboBox.getItems().setAll(GoalCategory.values());
        
        // Set up frequency combo box
        frequencyComboBox.getItems().setAll(GoalFrequency.values());
        
        // Set up target value spinner
        SpinnerValueFactory<Integer> valueFactory = 
                new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 100, 1);
        targetValueSpinner.setValueFactory(valueFactory);
        
        // Set up date pickers
        startDatePicker.setValue(LocalDate.now());
    }
    
    /**
     * Sets the current user ID
     */
    public void setCurrentUserId(Long userId) {
        this.currentUserId = userId;
        loadGoals();
    }
    
    /**
     * Loads goals for the current user
     */
    private void loadGoals() {
        if (currentUserId == null) {
            return;
        }
        
        goalsListView.getItems().setAll(goalDAO.findByUserId(currentUserId));
    }
    
    /**
     * Handles saving a new goal
     */
    @FXML
    private void handleSaveGoal() {
        if (currentUserId == null || titleField.getText().isEmpty() || 
            categoryComboBox.getValue() == null || frequencyComboBox.getValue() == null) {
            return;
        }
        
        Goal goal = new Goal();
        goal.setUserId(currentUserId);
        goal.setTitle(titleField.getText());
        goal.setDescription(descriptionArea.getText());
        goal.setCategory(categoryComboBox.getValue());
        goal.setFrequency(frequencyComboBox.getValue());
        goal.setTargetValue(targetValueSpinner.getValue());
        goal.setStartDate(startDatePicker.getValue());
        
        if (endDatePicker.getValue() != null) {
            goal.setEndDate(endDatePicker.getValue());
        }
        
        goal.setCreatedAt(LocalDateTime.now());
        goal.setUpdatedAt(LocalDateTime.now());
        
        Goal savedGoal = goalDAO.create(goal);
        
        if (savedGoal != null) {
            clearForm();
            loadGoals();
        }
    }
    
    /**
     * Handles selecting a goal from the list
     */
    @FXML
    private void handleGoalSelection() {
        Goal selectedGoal = goalsListView.getSelectionModel().getSelectedItem();
        if (selectedGoal != null) {
            titleField.setText(selectedGoal.getTitle());
            descriptionArea.setText(selectedGoal.getDescription());
            categoryComboBox.setValue(selectedGoal.getCategory());
            frequencyComboBox.setValue(selectedGoal.getFrequency());
            targetValueSpinner.getValueFactory().setValue(selectedGoal.getTargetValue());
            startDatePicker.setValue(selectedGoal.getStartDate());
            
            if (selectedGoal.getEndDate() != null) {
                endDatePicker.setValue(selectedGoal.getEndDate());
            } else {
                endDatePicker.setValue(null);
            }
            
            updateProgressView(selectedGoal);
        }
    }
    
    /**
     * Updates the progress view for the selected goal
     */
    private void updateProgressView(Goal goal) {
        progressBox.getChildren().clear();
        
        Label progressLabel = new Label(String.format("Progress: %d/%d", 
                goal.getCurrentProgress(), goal.getTargetValue()));
        
        ProgressBar progressBar = new ProgressBar();
        progressBar.setProgress((double) goal.getCurrentProgress() / goal.getTargetValue());
        progressBar.setPrefWidth(200);
        
        Button incrementButton = new Button("Log Progress");
        incrementButton.setOnAction(e -> {
            goal.updateProgress(1);
            goalDAO.update(goal);
            updateProgressView(goal);
        });
        
        progressBox.getChildren().addAll(progressLabel, progressBar, incrementButton);
        
        if (goal.isCompleted()) {
            Label completedLabel = new Label("Goal Completed!");
            completedLabel.setStyle("-fx-text-fill: green; -fx-font-weight: bold;");
            progressBox.getChildren().add(completedLabel);
        }
    }
    
    /**
     * Clears the form fields
     */
    private void clearForm() {
        titleField.clear();
        descriptionArea.clear();
        categoryComboBox.setValue(null);
        frequencyComboBox.setValue(null);
        targetValueSpinner.getValueFactory().setValue(1);
        startDatePicker.setValue(LocalDate.now());
        endDatePicker.setValue(null);
        progressBox.getChildren().clear();
    }
}