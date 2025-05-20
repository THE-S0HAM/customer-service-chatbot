package com.mindease.controller;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.util.Duration;
import java.io.File;

/**
 * Controller for the meditation and breathing assistant
 */
public class MeditationController {
    
    @FXML
    private Canvas breathingCanvas;
    
    @FXML
    private Button startButton;
    
    @FXML
    private Button stopButton;
    
    @FXML
    private Label timerLabel;
    
    @FXML
    private ComboBox<String> exerciseComboBox;
    
    @FXML
    private Slider durationSlider;
    
    private GraphicsContext gc;
    private Timeline breathingAnimation;
    private Timeline timerTimeline;
    private MediaPlayer mediaPlayer;
    private int remainingSeconds;
    private boolean isBreathingIn = true;
    private double circleSize = 50;
    
    /**
     * Initializes the controller
     */
    @FXML
    public void initialize() {
        gc = breathingCanvas.getGraphicsContext2D();
        
        // Set up exercise types
        exerciseComboBox.getItems().addAll(
            "4-7-8 Breathing",
            "Box Breathing",
            "Deep Breathing",
            "Guided Meditation"
        );
        exerciseComboBox.setValue("Deep Breathing");
        
        // Set up duration slider
        durationSlider.setMin(1);
        durationSlider.setMax(15);
        durationSlider.setValue(5);
        durationSlider.setShowTickLabels(true);
        durationSlider.setShowTickMarks(true);
        durationSlider.setMajorTickUnit(1);
        
        // Initial UI state
        stopButton.setDisable(true);
        updateTimerLabel((int) durationSlider.getValue() * 60);
        drawBreathingCircle();
    }
    
    /**
     * Handles starting the meditation exercise
     */
    @FXML
    private void handleStart() {
        startButton.setDisable(true);
        stopButton.setDisable(false);
        exerciseComboBox.setDisable(true);
        durationSlider.setDisable(true);
        
        // Set up timer
        remainingSeconds = (int) durationSlider.getValue() * 60;
        updateTimerLabel(remainingSeconds);
        
        // Start timer countdown
        timerTimeline = new Timeline(
            new KeyFrame(Duration.seconds(1), e -> {
                remainingSeconds--;
                updateTimerLabel(remainingSeconds);
                
                if (remainingSeconds <= 0) {
                    handleStop();
                }
            })
        );
        timerTimeline.setCycleCount(remainingSeconds);
        timerTimeline.play();
        
        // Start breathing animation
        startBreathingAnimation();
        
        // Play background sound if it's a guided meditation
        if (exerciseComboBox.getValue().equals("Guided Meditation")) {
            playMeditationAudio();
        }
    }
    
    /**
     * Handles stopping the meditation exercise
     */
    @FXML
    private void handleStop() {
        if (breathingAnimation != null) {
            breathingAnimation.stop();
        }
        
        if (timerTimeline != null) {
            timerTimeline.stop();
        }
        
        if (mediaPlayer != null) {
            mediaPlayer.stop();
        }
        
        startButton.setDisable(false);
        stopButton.setDisable(true);
        exerciseComboBox.setDisable(false);
        durationSlider.setDisable(false);
        
        // Reset breathing circle
        circleSize = 50;
        drawBreathingCircle();
    }
    
    /**
     * Starts the breathing animation based on selected exercise
     */
    private void startBreathingAnimation() {
        String exercise = exerciseComboBox.getValue();
        
        switch (exercise) {
            case "4-7-8 Breathing":
                start478Breathing();
                break;
            case "Box Breathing":
                startBoxBreathing();
                break;
            case "Deep Breathing":
            case "Guided Meditation":
            default:
                startDeepBreathing();
                break;
        }
    }
    
    /**
     * Starts 4-7-8 breathing animation (4s inhale, 7s hold, 8s exhale)
     */
    private void start478Breathing() {
        // Implementation would go here
    }
    
    /**
     * Starts box breathing animation (4s inhale, 4s hold, 4s exhale, 4s hold)
     */
    private void startBoxBreathing() {
        // Implementation would go here
    }
    
    /**
     * Starts deep breathing animation (simple inhale/exhale)
     */
    private void startDeepBreathing() {
        breathingAnimation = new Timeline(
            new KeyFrame(Duration.millis(50), e -> {
                if (isBreathingIn) {
                    circleSize += 1;
                    if (circleSize >= 150) {
                        isBreathingIn = false;
                    }
                } else {
                    circleSize -= 1;
                    if (circleSize <= 50) {
                        isBreathingIn = true;
                    }
                }
                drawBreathingCircle();
            })
        );
        breathingAnimation.setCycleCount(Timeline.INDEFINITE);
        breathingAnimation.play();
    }
    
    /**
     * Draws the breathing circle on the canvas
     */
    private void drawBreathingCircle() {
        double centerX = breathingCanvas.getWidth() / 2;
        double centerY = breathingCanvas.getHeight() / 2;
        
        // Clear canvas
        gc.clearRect(0, 0, breathingCanvas.getWidth(), breathingCanvas.getHeight());
        
        // Draw circle
        gc.setFill(Color.LIGHTBLUE);
        gc.fillOval(centerX - circleSize/2, centerY - circleSize/2, circleSize, circleSize);
        
        // Draw text
        gc.setFill(Color.BLACK);
        gc.fillText(isBreathingIn ? "Breathe In" : "Breathe Out", centerX - 30, centerY);
    }
    
    /**
     * Updates the timer label
     */
    private void updateTimerLabel(int seconds) {
        int minutes = seconds / 60;
        int remainingSeconds = seconds % 60;
        timerLabel.setText(String.format("%02d:%02d", minutes, remainingSeconds));
    }
    
    /**
     * Plays meditation audio
     */
    private void playMeditationAudio() {
        try {
            File audioFile = new File("resources/audio/meditation.mp3");
            Media media = new Media(audioFile.toURI().toString());
            mediaPlayer = new MediaPlayer(media);
            mediaPlayer.play();
        } catch (Exception e) {
            System.err.println("Error playing meditation audio: " + e.getMessage());
        }
    }
}