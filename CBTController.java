package com.mindease.controller;

import com.mindease.dao.ThoughtRecordDAO;
import com.mindease.model.ThoughtRecord;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import java.time.LocalDateTime;

/**
 * Controller for the CBT thought reframing tool
 */
public class CBTController {
    
    private ThoughtRecordDAO thoughtRecordDAO;
    private Long currentUserId;
    
    @FXML
    private TextField situationField;
    
    @FXML
    private TextArea thoughtArea;
    
    @FXML
    private TextField emotionsField;
    
    @FXML
    private Slider intensitySlider;
    
    @FXML
    private TextArea evidenceForArea;
    
    @FXML
    private TextArea evidenceAgainstArea;
    
    @FXML
    private TextArea alternativeThoughtArea;
    
    @FXML
    private TextArea outcomeArea;
    
    @FXML
    private Slider newIntensitySlider;
    
    @FXML
    private ListView<ThoughtRecord> recordsListView;
    
    /**
     * Initializes the controller
     */
    @FXML
    public void initialize() {
        thoughtRecordDAO = new ThoughtRecordDAO();
        
        // Set up intensity sliders
        intensitySlider.setMin(1);
        intensitySlider.setMax(10);
        intensitySlider.setValue(5);
        intensitySlider.setShowTickLabels(true);
        intensitySlider.setShowTickMarks(true);
        
        newIntensitySlider.setMin(1);
        newIntensitySlider.setMax(10);
        newIntensitySlider.setValue(5);
        newIntensitySlider.setShowTickLabels(true);
        newIntensitySlider.setShowTickMarks(true);
    }
    
    /**
     * Sets the current user ID
     */
    public void setCurrentUserId(Long userId) {
        this.currentUserId = userId;
        loadThoughtRecords();
    }
    
    /**
     * Loads thought records for the current user
     */
    private void loadThoughtRecords() {
        if (currentUserId == null) {
            return;
        }
        
        recordsListView.getItems().setAll(thoughtRecordDAO.findByUserId(currentUserId));
    }
    
    /**
     * Handles saving a thought record
     */
    @FXML
    private void handleSaveRecord() {
        if (currentUserId == null || situationField.getText().isEmpty() || 
            thoughtArea.getText().isEmpty() || emotionsField.getText().isEmpty()) {
            return;
        }
        
        ThoughtRecord record = new ThoughtRecord();
        record.setUserId(currentUserId);
        record.setSituation(situationField.getText());
        record.setAutomaticThought(thoughtArea.getText());
        record.setEmotions(emotionsField.getText());
        record.setEmotionIntensity((int) intensitySlider.getValue());
        record.setEvidenceFor(evidenceForArea.getText());
        record.setEvidenceAgainst(evidenceAgainstArea.getText());
        record.setAlternativeThought(alternativeThoughtArea.getText());
        record.setOutcome(outcomeArea.getText());
        record.setNewEmotionIntensity((int) newIntensitySlider.getValue());
        record.setCreatedAt(LocalDateTime.now());
        record.setUpdatedAt(LocalDateTime.now());
        
        ThoughtRecord savedRecord = thoughtRecordDAO.create(record);
        
        if (savedRecord != null) {
            clearForm();
            loadThoughtRecords();
        }
    }
    
    /**
     * Handles selecting a thought record from the list
     */
    @FXML
    private void handleRecordSelection() {
        ThoughtRecord selectedRecord = recordsListView.getSelectionModel().getSelectedItem();
        if (selectedRecord != null) {
            situationField.setText(selectedRecord.getSituation());
            thoughtArea.setText(selectedRecord.getAutomaticThought());
            emotionsField.setText(selectedRecord.getEmotions());
            intensitySlider.setValue(selectedRecord.getEmotionIntensity());
            evidenceForArea.setText(selectedRecord.getEvidenceFor());
            evidenceAgainstArea.setText(selectedRecord.getEvidenceAgainst());
            alternativeThoughtArea.setText(selectedRecord.getAlternativeThought());
            outcomeArea.setText(selectedRecord.getOutcome());
            newIntensitySlider.setValue(selectedRecord.getNewEmotionIntensity());
        }
    }
    
    /**
     * Clears the form fields
     */
    private void clearForm() {
        situationField.clear();
        thoughtArea.clear();
        emotionsField.clear();
        intensitySlider.setValue(5);
        evidenceForArea.clear();
        evidenceAgainstArea.clear();
        alternativeThoughtArea.clear();
        outcomeArea.clear();
        newIntensitySlider.setValue(5);
    }
}