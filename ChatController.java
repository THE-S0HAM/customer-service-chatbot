package com.mindease.controller;

import com.mindease.service.ChatbotService;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

/**
 * Controller for the chatbot interface
 */
public class ChatController {
    
    private ChatbotService chatbotService;
    
    @FXML
    private ScrollPane chatScrollPane;
    
    @FXML
    private VBox chatBox;
    
    @FXML
    private TextField messageField;
    
    @FXML
    private Button sendButton;
    
    /**
     * Initializes the controller
     */
    @FXML
    public void initialize() {
        chatbotService = new ChatbotService();
        
        // Configure chat scroll pane
        chatScrollPane.setFitToWidth(true);
        chatScrollPane.vvalueProperty().bind(chatBox.heightProperty());
        
        // Add welcome message
        addBotMessage("Hello! I'm MindEase, your mental wellness companion. How are you feeling today?");
    }
    
    /**
     * Handles sending a message
     */
    @FXML
    private void handleSendMessage() {
        String message = messageField.getText().trim();
        if (message.isEmpty()) {
            return;
        }
        
        // Add user message to chat
        addUserMessage(message);
        
        // Clear input field
        messageField.clear();
        
        // Process message and get response
        String response = chatbotService.processInput(message);
        
        // Add bot response to chat
        addBotMessage(response);
    }
    
    /**
     * Adds a user message to the chat
     */
    private void addUserMessage(String message) {
        HBox messageBox = new HBox();
        messageBox.setAlignment(Pos.CENTER_RIGHT);
        messageBox.setPadding(new Insets(5, 5, 5, 10));
        
        Text text = new Text(message);
        TextFlow textFlow = new TextFlow(text);
        textFlow.setStyle(
                "-fx-background-color: #2196f3;" +
                "-fx-background-radius: 20px;" +
                "-fx-padding: 10px;"
        );
        text.setStyle("-fx-fill: white;");
        
        messageBox.getChildren().add(textFlow);
        chatBox.getChildren().add(messageBox);
    }
    
    /**
     * Adds a bot message to the chat
     */
    private void addBotMessage(String message) {
        HBox messageBox = new HBox();
        messageBox.setAlignment(Pos.CENTER_LEFT);
        messageBox.setPadding(new Insets(5, 10, 5, 5));
        
        Text text = new Text(message);
        TextFlow textFlow = new TextFlow(text);
        textFlow.setStyle(
                "-fx-background-color: #e0e0e0;" +
                "-fx-background-radius: 20px;" +
                "-fx-padding: 10px;"
        );
        
        messageBox.getChildren().add(textFlow);
        chatBox.getChildren().add(messageBox);
    }
}