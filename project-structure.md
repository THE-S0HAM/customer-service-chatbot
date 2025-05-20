# MindEase Project Structure

## Overview
MindEase is a Java-based mental health and wellness chatbot application with features for mood tracking, journaling, CBT thought reframing, meditation assistance, goal setting, and more.

## Core Components

### Models
- `User.java` - User account information
- `MoodEntry.java` - Mood tracking entries
- `JournalEntry.java` - Journal entries with sentiment analysis
- `ThoughtRecord.java` - CBT thought records for cognitive reframing
- `Goal.java` - Goals and habits for tracking
- `Reminder.java` - Reminders for wellness activities

### Data Access Objects (DAOs)
- `UserDAO.java` - User data operations
- `MoodEntryDAO.java` - Mood entry operations
- `JournalEntryDAO.java` - Journal entry operations
- `ThoughtRecordDAO.java` - Thought record operations
- `GoalDAO.java` - Goal operations
- `ReminderDAO.java` - Reminder operations

### Services
- `ChatbotService.java` - Conversational engine
- `SentimentAnalysisService.java` - NLP for journal entries
- `ExportService.java` - PDF and CSV export functionality
- `ReminderService.java` - Scheduling reminders
- `AuthService.java` - User authentication

### Controllers
- `MoodTrackerController.java` - Mood tracking UI controller
- `JournalController.java` - Journaling UI controller
- `CBTController.java` - CBT thought reframing UI controller
- `MeditationController.java` - Meditation UI controller
- `GoalController.java` - Goal setting UI controller
- `ChatController.java` - Chatbot UI controller

### UI Components
- `MainView.java` - Main application view
- `DashboardView.java` - User dashboard
- `MoodChartView.java` - Mood visualization charts
- `BreathingView.java` - Breathing visualization

### Configuration
- `DatabaseConfig.java` - Database setup and connection
- `AppConfig.java` - Application configuration

### Utilities
- `SecurityUtil.java` - Password hashing and security
- `DateTimeUtil.java` - Date and time utilities
- `ValidationUtil.java` - Input validation

## Main Application
- `MindEaseApplication.java` - JavaFX application entry point

## Resources
- `/styles/` - CSS stylesheets
- `/images/` - UI images and icons
- `/data/` - Application data files
- `/models/` - NLP models for sentiment analysis

## Database Schema
- `users` - User accounts
- `mood_entries` - Mood tracking data
- `journal_entries` - Journal entries
- `thought_records` - CBT thought records
- `goals` - User goals and habits
- `reminders` - User reminders

## Features Implemented
1. Mood Tracking & Analytics
2. Guided Journaling
3. CBT Thought Reframing Tool
4. Conversational Engine
5. Goal Setting & Habit Tracker
6. Meditation & Breathing Assistant
7. Export & Share Progress
8. Crisis Keyword Detection
9. Sentiment Analysis on Journal Text

## Future Enhancements
1. Daily Affirmations & Quotes
2. Routine Reminders & Scheduler
3. User authentication and account management
4. Cloud synchronization
5. Mobile companion app