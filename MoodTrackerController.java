package com.mindease.controller;

import com.mindease.dao.MoodEntryDAO;
import com.mindease.model.MoodEntry;
import com.mindease.model.MoodEntry.MoodType;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Slider;
import javafx.scene.control.TextArea;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Controller for the mood tracking feature
 */
public class MoodTrackerController {
    
    private MoodEntryDAO moodEntryDAO;
    private Long currentUserId;
    
    @FXML
    private ComboBox<MoodType> moodComboBox;
    
    @FXML
    private Slider intensitySlider;
    
    @FXML
    private TextArea notesTextArea;
    
    @FXML
    private LineChart<String, Number> moodChart;
    
    /**
     * Initializes the controller
     */
    @FXML
    public void initialize() {
        moodEntryDAO = new MoodEntryDAO();
        
        // Set up mood combo box
        moodComboBox.setItems(FXCollections.observableArrayList(MoodType.values()));
        
        // Set up intensity slider
        intensitySlider.setMin(1);
        intensitySlider.setMax(10);
        intensitySlider.setValue(5);
        intensitySlider.setShowTickLabels(true);
        intensitySlider.setShowTickMarks(true);
        intensitySlider.setMajorTickUnit(1);
        intensitySlider.setMinorTickCount(0);
        intensitySlider.setSnapToTicks(true);
    }
    
    /**
     * Sets the current user ID
     */
    public void setCurrentUserId(Long userId) {
        this.currentUserId = userId;
        loadMoodData();
    }
    
    /**
     * Loads mood data for the current user
     */
    private void loadMoodData() {
        if (currentUserId == null) {
            return;
        }
        
        List<MoodEntry> entries = moodEntryDAO.findByUserId(currentUserId);
        updateMoodChart(entries);
    }
    
    /**
     * Updates the mood chart with the provided entries
     */
    private void updateMoodChart(List<MoodEntry> entries) {
        // Implementation for updating the chart would go here
    }
    
    /**
     * Handles saving a new mood entry
     */
    @FXML
    private void handleSaveMood() {
        if (currentUserId == null || moodComboBox.getValue() == null) {
            return;
        }
        
        MoodEntry entry = new MoodEntry();
        entry.setUserId(currentUserId);
        entry.setMood(moodComboBox.getValue());
        entry.setIntensityLevel((int) intensitySlider.getValue());
        entry.setNotes(notesTextArea.getText());
        entry.setCreatedAt(LocalDateTime.now());
        
        MoodEntry savedEntry = moodEntryDAO.create(entry);
        
        if (savedEntry != null) {
            clearForm();
            loadMoodData();
        }
    }
    
    /**
     * Clears the form fields
     */
    private void clearForm() {
        moodComboBox.setValue(null);
        intensitySlider.setValue(5);
        notesTextArea.clear();
    }
}