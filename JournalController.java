package com.mindease.controller;

import com.mindease.dao.JournalEntryDAO;
import com.mindease.model.JournalEntry;
import com.mindease.service.SentimentAnalysisService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Controller for the journaling feature
 */
public class JournalController {
    
    private JournalEntryDAO journalEntryDAO;
    private SentimentAnalysisService sentimentService;
    private Long currentUserId;
    
    @FXML
    private TextField titleField;
    
    @FXML
    private TextArea contentArea;
    
    @FXML
    private ComboBox<String> promptComboBox;
    
    @FXML
    private ListView<JournalEntry> entriesListView;
    
    @FXML
    private Label sentimentLabel;
    
    /**
     * Initializes the controller
     */
    @FXML
    public void initialize() {
        journalEntryDAO = new JournalEntryDAO();
        sentimentService = new SentimentAnalysisService();
        
        // Set up journal prompts
        ObservableList<String> prompts = FXCollections.observableArrayList(
            "What are three things you're grateful for today?",
            "Describe a challenge you faced today and how you handled it.",
            "What made you smile today?",
            "What's something you're looking forward to?",
            "Reflect on something you learned recently.",
            "Write about a goal you're working toward.",
            "How are you feeling right now, and why?",
            "What would make today great?",
            "Write about someone who inspires you.",
            "What's one thing you could have done better today?"
        );
        promptComboBox.setItems(prompts);
    }
    
    /**
     * Sets the current user ID
     */
    public void setCurrentUserId(Long userId) {
        this.currentUserId = userId;
        loadJournalEntries();
    }
    
    /**
     * Loads journal entries for the current user
     */
    private void loadJournalEntries() {
        if (currentUserId == null) {
            return;
        }
        
        List<JournalEntry> entries = journalEntryDAO.findByUserId(currentUserId);
        entriesListView.setItems(FXCollections.observableArrayList(entries));
    }
    
    /**
     * Handles saving a new journal entry
     */
    @FXML
    private void handleSaveEntry() {
        if (currentUserId == null || titleField.getText().isEmpty() || contentArea.getText().isEmpty()) {
            return;
        }
        
        JournalEntry entry = new JournalEntry();
        entry.setUserId(currentUserId);
        entry.setTitle(titleField.getText());
        entry.setContent(contentArea.getText());
        entry.setPromptUsed(promptComboBox.getValue());
        entry.setCreatedAt(LocalDateTime.now());
        
        // Analyze sentiment if service is available
        if (sentimentService != null) {
            SentimentAnalysisService.SentimentResult result = sentimentService.analyzeSentiment(contentArea.getText());
            if (result != null) {
                entry.setSentimentScore(result.getSentiment());
            }
        }
        
        JournalEntry savedEntry = journalEntryDAO.create(entry);
        
        if (savedEntry != null) {
            clearForm();
            loadJournalEntries();
        }
    }
    
    /**
     * Handles selecting a journal entry from the list
     */
    @FXML
    private void handleEntrySelection() {
        JournalEntry selectedEntry = entriesListView.getSelectionModel().getSelectedItem();
        if (selectedEntry != null) {
            titleField.setText(selectedEntry.getTitle());
            contentArea.setText(selectedEntry.getContent());
            promptComboBox.setValue(selectedEntry.getPromptUsed());
            
            if (selectedEntry.getSentimentScore() != null) {
                sentimentLabel.setText("Sentiment: " + selectedEntry.getSentimentScore());
            } else {
                sentimentLabel.setText("");
            }
        }
    }
    
    /**
     * Clears the form fields
     */
    private void clearForm() {
        titleField.clear();
        contentArea.clear();
        promptComboBox.setValue(null);
        sentimentLabel.setText("");
    }
}