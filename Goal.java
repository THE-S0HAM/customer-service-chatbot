package com.mindease.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Represents a wellness goal set by the user
 */
public class Goal {
    private Long id;
    private Long userId;
    private String title;
    private String description;
    private GoalCategory category;
    private GoalFrequency frequency;
    private int targetValue;
    private int currentProgress;
    private LocalDate startDate;
    private LocalDate endDate;
    private boolean completed;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    public enum GoalCategory {
        MEDITATION,
        EXERCISE,
        JOURNALING,
        SLEEP,
        HYDRATION,
        NUTRITION,
        SOCIAL,
        LEARNING,
        OTHER
    }
    
    public enum GoalFrequency {
        DAILY,
        WEEKLY,
        MONTHLY,
        ONE_TIME
    }
    
    public Goal() {
        LocalDateTime now = LocalDateTime.now();
        this.createdAt = now;
        this.updatedAt = now;
        this.startDate = LocalDate.now();
        this.completed = false;
        this.currentProgress = 0;
    }
    
    public Goal(Long userId, String title, GoalCategory category, GoalFrequency frequency, int targetValue) {
        this();
        this.userId = userId;
        this.title = title;
        this.category = category;
        this.frequency = frequency;
        this.targetValue = targetValue;
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public GoalCategory getCategory() {
        return category;
    }

    public void setCategory(GoalCategory category) {
        this.category = category;
    }

    public GoalFrequency getFrequency() {
        return frequency;
    }

    public void setFrequency(GoalFrequency frequency) {
        this.frequency = frequency;
    }

    public int getTargetValue() {
        return targetValue;
    }

    public void setTargetValue(int targetValue) {
        this.targetValue = targetValue;
    }

    public int getCurrentProgress() {
        return currentProgress;
    }

    public void setCurrentProgress(int currentProgress) {
        this.currentProgress = currentProgress;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
    
    /**
     * Updates the progress and checks if the goal is completed
     * @param increment The amount to increment the progress
     * @return true if the goal is completed with this update
     */
    public boolean updateProgress(int increment) {
        this.currentProgress += increment;
        if (this.currentProgress >= this.targetValue) {
            this.completed = true;
        }
        this.updatedAt = LocalDateTime.now();
        return this.completed;
    }
}