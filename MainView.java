package com.mindease.ui;

import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.geometry.Insets;
import javafx.geometry.Pos;

/**
 * Main view for the MindEase application
 */
public class MainView {
    private BorderPane root;
    private TabPane tabPane;
    
    public MainView() {
        createUI();
    }
    
    private void createUI() {
        // Create root layout
        root = new BorderPane();
        
        // Create header
        VBox header = createHeader();
        root.setTop(header);
        
        // Create tab pane for main content
        tabPane = new TabPane();
        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
        
        // Add tabs
        tabPane.getTabs().add(createDashboardTab());
        tabPane.getTabs().add(createMoodTrackerTab());
        tabPane.getTabs().add(createJournalTab());
        tabPane.getTabs().add(createCBTTab());
        tabPane.getTabs().add(createMeditationTab());
        tabPane.getTabs().add(createGoalsTab());
        tabPane.getTabs().add(createChatbotTab());
        
        root.setCenter(tabPane);
        
        // Create footer
        HBox footer = createFooter();
        root.setBottom(footer);
    }
    
    private VBox createHeader() {
        VBox header = new VBox(10);
        header.setPadding(new Insets(15));
        header.setStyle("-fx-background-color: #6a1b9a;");
        
        Label title = new Label("MindEase");
        title.setStyle("-fx-font-size: 24px; -fx-text-fill: white;");
        
        Label subtitle = new Label("Mental Health & Wellness Companion");
        subtitle.setStyle("-fx-font-size: 14px; -fx-text-fill: white;");
        
        header.getChildren().addAll(title, subtitle);
        return header;
    }
    
    private Tab createDashboardTab() {
        Tab tab = new Tab("Dashboard");
        
        // Placeholder for dashboard content
        VBox content = new VBox(20);
        content.setPadding(new Insets(20));
        content.setAlignment(Pos.CENTER);
        
        Label welcomeLabel = new Label("Welcome to MindEase");
        welcomeLabel.setStyle("-fx-font-size: 20px;");
        
        content.getChildren().add(welcomeLabel);
        tab.setContent(content);
        
        return tab;
    }
    
    private Tab createMoodTrackerTab() {
        Tab tab = new Tab("Mood Tracker");
        
        // Placeholder for mood tracker content
        VBox content = new VBox(20);
        content.setPadding(new Insets(20));
        
        Label titleLabel = new Label("Track Your Mood");
        titleLabel.setStyle("-fx-font-size: 18px;");
        
        content.getChildren().add(titleLabel);
        tab.setContent(content);
        
        return tab;
    }
    
    private Tab createJournalTab() {
        Tab tab = new Tab("Journal");
        
        // Placeholder for journal content
        VBox content = new VBox(20);
        content.setPadding(new Insets(20));
        
        Label titleLabel = new Label("Guided Journaling");
        titleLabel.setStyle("-fx-font-size: 18px;");
        
        content.getChildren().add(titleLabel);
        tab.setContent(content);
        
        return tab;
    }
    
    private Tab createCBTTab() {
        Tab tab = new Tab("Thought Reframing");
        
        // Placeholder for CBT content
        VBox content = new VBox(20);
        content.setPadding(new Insets(20));
        
        Label titleLabel = new Label("CBT Thought Reframing Tool");
        titleLabel.setStyle("-fx-font-size: 18px;");
        
        content.getChildren().add(titleLabel);
        tab.setContent(content);
        
        return tab;
    }
    
    private Tab createMeditationTab() {
        Tab tab = new Tab("Meditation");
        
        // Placeholder for meditation content
        VBox content = new VBox(20);
        content.setPadding(new Insets(20));
        
        Label titleLabel = new Label("Meditation & Breathing Assistant");
        titleLabel.setStyle("-fx-font-size: 18px;");
        
        content.getChildren().add(titleLabel);
        tab.setContent(content);
        
        return tab;
    }
    
    private Tab createGoalsTab() {
        Tab tab = new Tab("Goals");
        
        // Placeholder for goals content
        VBox content = new VBox(20);
        content.setPadding(new Insets(20));
        
        Label titleLabel = new Label("Goal Setting & Habit Tracker");
        titleLabel.setStyle("-fx-font-size: 18px;");
        
        content.getChildren().add(titleLabel);
        tab.setContent(content);
        
        return tab;
    }
    
    private Tab createChatbotTab() {
        Tab tab = new Tab("Chat");
        
        // Placeholder for chatbot content
        VBox content = new VBox(20);
        content.setPadding(new Insets(20));
        
        Label titleLabel = new Label("MindEase Chatbot");
        titleLabel.setStyle("-fx-font-size: 18px;");
        
        content.getChildren().add(titleLabel);
        tab.setContent(content);
        
        return tab;
    }
    
    private HBox createFooter() {
        HBox footer = new HBox();
        footer.setPadding(new Insets(10));
        footer.setAlignment(Pos.CENTER_RIGHT);
        footer.setStyle("-fx-background-color: #f5f5f5;");
        
        Label versionLabel = new Label("MindEase v1.0");
        versionLabel.setStyle("-fx-text-fill: #757575;");
        
        footer.getChildren().add(versionLabel);
        return footer;
    }
    
    public BorderPane getRoot() {
        return root;
    }
}