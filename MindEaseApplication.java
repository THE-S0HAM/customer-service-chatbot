package com.mindease;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Main application class for MindEase Mental Health & Wellness Chatbot
 */
public class MindEaseApplication extends Application {
    
    private static final Logger logger = LoggerFactory.getLogger(MindEaseApplication.class);
    
    @Override
    public void start(Stage primaryStage) {
        try {
            // Initialize database
            initializeDatabase();
            
            // Set up main view
            BorderPane mainView = createMainView();
            Scene scene = new Scene(mainView, 1024, 768);
            
            // Configure primary stage
            primaryStage.setTitle("MindEase - Mental Health & Wellness Companion");
            primaryStage.setScene(scene);
            primaryStage.setMinWidth(800);
            primaryStage.setMinHeight(600);
            primaryStage.show();
            
            logger.info("MindEase application started successfully");
        } catch (Exception e) {
            logger.error("Failed to start MindEase application", e);
        }
    }
    
    private BorderPane createMainView() {
        BorderPane root = new BorderPane();
        // TODO: Implement main view components
        return root;
    }
    
    private void initializeDatabase() {
        // TODO: Implement database initialization
    }
    
    @Override
    public void stop() {
        // Clean up resources
        logger.info("MindEase application stopped");
    }

    /**
     * Main method to launch the application
     * @param args command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
}