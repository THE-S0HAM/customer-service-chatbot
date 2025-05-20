package com.mindease.service;

import com.mindease.dao.ReminderDAO;
import com.mindease.model.Reminder;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

/**
 * Service for scheduling and managing reminders
 */
public class ReminderService {
    private static final Logger logger = LoggerFactory.getLogger(ReminderService.class);
    
    private final ReminderDAO reminderDAO;
    private Scheduler scheduler;
    
    public ReminderService() {
        this.reminderDAO = new ReminderDAO();
        initializeScheduler();
    }
    
    /**
     * Initializes the Quartz scheduler
     */
    private void initializeScheduler() {
        try {
            scheduler = StdSchedulerFactory.getDefaultScheduler();
            scheduler.start();
            logger.info("Reminder scheduler started");
        } catch (SchedulerException e) {
            logger.error("Error initializing scheduler", e);
        }
    }
    
    /**
     * Schedules all active reminders for a user
     */
    public void scheduleRemindersForUser(Long userId) {
        List<Reminder> reminders = reminderDAO.findByUserId(userId);
        
        for (Reminder reminder : reminders) {
            if (reminder.isActive()) {
                scheduleReminder(reminder);
            }
        }
        
        logger.info("Scheduled {} reminders for user {}", reminders.size(), userId);
    }
    
    /**
     * Schedules a single reminder
     */
    public void scheduleReminder(Reminder reminder) {
        try {
            // Create job detail
            JobDetail jobDetail = JobBuilder.newJob(ReminderJob.class)
                    .withIdentity("reminder-" + reminder.getId())
                    .usingJobData("reminderId", reminder.getId())
                    .usingJobData("title", reminder.getTitle())
                    .usingJobData("message", reminder.getMessage() != null ? reminder.getMessage() : "")
                    .build();
            
            // Create trigger based on reminder schedule
            Trigger trigger = createTriggerForReminder(reminder);
            
            // Schedule the job
            scheduler.scheduleJob(jobDetail, trigger);
            
            logger.info("Scheduled reminder: {}", reminder.getTitle());
        } catch (SchedulerException e) {
            logger.error("Error scheduling reminder", e);
        }
    }
    
    /**
     * Creates a trigger for a reminder based on its schedule
     */
    private Trigger createTriggerForReminder(Reminder reminder) {
        TriggerBuilder<Trigger> triggerBuilder = TriggerBuilder.newTrigger()
                .withIdentity("trigger-" + reminder.getId());
        
        // Set start time to the next occurrence
        LocalTime reminderTime = reminder.getTime();
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime nextRun = LocalDateTime.of(now.toLocalDate(), reminderTime);
        
        if (nextRun.isBefore(now)) {
            nextRun = nextRun.plusDays(1);
        }
        
        // Find the next day that is active
        boolean[] daysOfWeek = reminder.getDaysOfWeek();
        int currentDayOfWeek = nextRun.getDayOfWeek().getValue() % 7; // 0-6 (Sunday-Saturday)
        
        int daysToAdd = 0;
        while (!daysOfWeek[(currentDayOfWeek + daysToAdd) % 7]) {
            daysToAdd++;
        }
        
        nextRun = nextRun.plusDays(daysToAdd);
        
        triggerBuilder.startAt(Date.from(nextRun.atZone(ZoneId.systemDefault()).toInstant()));
        
        // Set recurrence schedule
        CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(buildCronExpression(reminder));
        triggerBuilder.withSchedule(scheduleBuilder);
        
        return triggerBuilder.build();
    }
    
    /**
     * Builds a cron expression for the reminder schedule
     */
    private String buildCronExpression(Reminder reminder) {
        LocalTime time = reminder.getTime();
        boolean[] daysOfWeek = reminder.getDaysOfWeek();
        
        StringBuilder cronDays = new StringBuilder();
        for (int i = 0; i < daysOfWeek.length; i++) {
            if (daysOfWeek[i]) {
                if (cronDays.length() > 0) {
                    cronDays.append(",");
                }
                // Convert from 0-6 (Sunday-Saturday) to 1-7 (Sunday-Saturday) for cron
                cronDays.append((i + 1));
            }
        }
        
        // If no days are selected, default to every day
        if (cronDays.length() == 0) {
            cronDays.append("*");
        }
        
        // Cron format: seconds minutes hours day-of-month month day-of-week year
        return String.format("0 %d %d ? * %s", time.getMinute(), time.getHour(), cronDays);
    }
    
    /**
     * Cancels a scheduled reminder
     */
    public void cancelReminder(Long reminderId) {
        try {
            scheduler.deleteJob(new JobKey("reminder-" + reminderId));
            logger.info("Cancelled reminder: {}", reminderId);
        } catch (SchedulerException e) {
            logger.error("Error cancelling reminder", e);
        }
    }
    
    /**
     * Shuts down the scheduler
     */
    public void shutdown() {
        try {
            if (scheduler != null && !scheduler.isShutdown()) {
                scheduler.shutdown();
                logger.info("Reminder scheduler shut down");
            }
        } catch (SchedulerException e) {
            logger.error("Error shutting down scheduler", e);
        }
    }
    
    /**
     * Job class for executing reminders
     */
    public static class ReminderJob implements Job {
        @Override
        public void execute(JobExecutionContext context) {
            JobDataMap dataMap = context.getJobDetail().getJobDataMap();
            String title = dataMap.getString("title");
            String message = dataMap.getString("message");
            
            // Show reminder notification on JavaFX thread
            Platform.runLater(() -> {
                Alert alert = new Alert(Alert.AlertType.INFORMATION, message, ButtonType.OK);
                alert.setTitle("MindEase Reminder");
                alert.setHeaderText(title);
                alert.show();
            });
        }
    }
}