package com.mindease.model;

import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * Represents a reminder for wellness activities
 */
public class Reminder {
    private Long id;
    private Long userId;
    private String title;
    private String message;
    private ReminderType type;
    private LocalTime time;
    private boolean[] daysOfWeek; // Sunday = 0, Monday = 1, etc.
    private boolean active;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    public enum ReminderType {
        MOOD_CHECK,
        JOURNAL,
        MEDITATION,
        BREATHING,
        GOAL_UPDATE,
        MEDICATION,
        WATER,
        CUSTOM
    }
    
    public Reminder() {
        LocalDateTime now = LocalDateTime.now();
        this.createdAt = now;
        this.updatedAt = now;
        this.active = true;
        this.daysOfWeek = new boolean[7];
    }
    
    public Reminder(Long userId, String title, ReminderType type, LocalTime time) {
        this();
        this.userId = userId;
        this.title = title;
        this.type = type;
        this.time = time;
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

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ReminderType getType() {
        return type;
    }

    public void setType(ReminderType type) {
        this.type = type;
    }

    public LocalTime getTime() {
        return time;
    }

    public void setTime(LocalTime time) {
        this.time = time;
    }

    public boolean[] getDaysOfWeek() {
        return daysOfWeek;
    }

    public void setDaysOfWeek(boolean[] daysOfWeek) {
        this.daysOfWeek = daysOfWeek;
    }
    
    public void setDayActive(int day, boolean active) {
        if (day >= 0 && day < 7) {
            this.daysOfWeek[day] = active;
        }
    }
    
    public boolean isDayActive(int day) {
        if (day >= 0 && day < 7) {
            return this.daysOfWeek[day];
        }
        return false;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
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
}