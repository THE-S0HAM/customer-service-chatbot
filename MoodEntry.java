package com.mindease.model;

import java.time.LocalDateTime;

/**
 * Represents a mood entry logged by the user
 */
public class MoodEntry {
    private Long id;
    private Long userId;
    private MoodType mood;
    private int intensityLevel; // 1-10 scale
    private String notes;
    private LocalDateTime createdAt;
    
    public enum MoodType {
        HAPPY,
        CONTENT,
        NEUTRAL,
        SAD,
        ANXIOUS,
        ANGRY,
        STRESSED,
        TIRED,
        ENERGETIC,
        CALM
    }
    
    public MoodEntry() {
        this.createdAt = LocalDateTime.now();
    }
    
    public MoodEntry(Long userId, MoodType mood, int intensityLevel) {
        this();
        this.userId = userId;
        this.mood = mood;
        this.intensityLevel = intensityLevel;
    }

    // Getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public MoodType getMood() {
        return mood;
    }

    public void setMood(MoodType mood) {
        this.mood = mood;
    }

    public int getIntensityLevel() {
        return intensityLevel;
    }

    public void setIntensityLevel(int intensityLevel) {
        this.intensityLevel = intensityLevel;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}