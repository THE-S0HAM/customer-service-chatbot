package com.mindease.model;

import java.time.LocalDateTime;

/**
 * Represents a CBT thought record for cognitive reframing
 */
public class ThoughtRecord {
    private Long id;
    private Long userId;
    private String situation;
    private String automaticThought;
    private String emotions;
    private int emotionIntensity; // 1-10 scale
    private String evidenceFor;
    private String evidenceAgainst;
    private String alternativeThought;
    private String outcome;
    private int newEmotionIntensity; // 1-10 scale
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    public ThoughtRecord() {
        LocalDateTime now = LocalDateTime.now();
        this.createdAt = now;
        this.updatedAt = now;
    }
    
    public ThoughtRecord(Long userId, String situation, String automaticThought, String emotions, int emotionIntensity) {
        this();
        this.userId = userId;
        this.situation = situation;
        this.automaticThought = automaticThought;
        this.emotions = emotions;
        this.emotionIntensity = emotionIntensity;
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

    public String getSituation() {
        return situation;
    }

    public void setSituation(String situation) {
        this.situation = situation;
    }

    public String getAutomaticThought() {
        return automaticThought;
    }

    public void setAutomaticThought(String automaticThought) {
        this.automaticThought = automaticThought;
    }

    public String getEmotions() {
        return emotions;
    }

    public void setEmotions(String emotions) {
        this.emotions = emotions;
    }

    public int getEmotionIntensity() {
        return emotionIntensity;
    }

    public void setEmotionIntensity(int emotionIntensity) {
        this.emotionIntensity = emotionIntensity;
    }

    public String getEvidenceFor() {
        return evidenceFor;
    }

    public void setEvidenceFor(String evidenceFor) {
        this.evidenceFor = evidenceFor;
    }

    public String getEvidenceAgainst() {
        return evidenceAgainst;
    }

    public void setEvidenceAgainst(String evidenceAgainst) {
        this.evidenceAgainst = evidenceAgainst;
    }

    public String getAlternativeThought() {
        return alternativeThought;
    }

    public void setAlternativeThought(String alternativeThought) {
        this.alternativeThought = alternativeThought;
    }

    public String getOutcome() {
        return outcome;
    }

    public void setOutcome(String outcome) {
        this.outcome = outcome;
    }

    public int getNewEmotionIntensity() {
        return newEmotionIntensity;
    }

    public void setNewEmotionIntensity(int newEmotionIntensity) {
        this.newEmotionIntensity = newEmotionIntensity;
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