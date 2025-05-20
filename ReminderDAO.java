package com.mindease.dao;

import com.mindease.model.Reminder;
import com.mindease.model.Reminder.ReminderType;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Data Access Object for Reminder entity
 */
public class ReminderDAO {
    private static final Logger logger = LoggerFactory.getLogger(ReminderDAO.class);
    
    /**
     * Creates a new reminder
     */
    public Reminder create(Reminder reminder) {
        String sql = "INSERT INTO reminders (user_id, title, message, type, time, days_of_week, active, created_at, updated_at) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            pstmt.setLong(1, reminder.getUserId());
            pstmt.setString(2, reminder.getTitle());
            pstmt.setString(3, reminder.getMessage());
            pstmt.setString(4, reminder.getType().name());
            pstmt.setTime(5, Time.valueOf(reminder.getTime()));
            pstmt.setString(6, booleanArrayToString(reminder.getDaysOfWeek()));
            pstmt.setBoolean(7, reminder.isActive());
            pstmt.setTimestamp(8, Timestamp.valueOf(reminder.getCreatedAt()));
            pstmt.setTimestamp(9, Timestamp.valueOf(reminder.getUpdatedAt()));
            
            int affectedRows = pstmt.executeUpdate();
            
            if (affectedRows > 0) {
                try (ResultSet rs = pstmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        reminder.setId(rs.getLong(1));
                    }
                }
            }
            
            logger.info("Created reminder: {}", reminder.getTitle());
            return reminder;
            
        } catch (SQLException e) {
            logger.error("Error creating reminder", e);
            return null;
        }
    }
    
    /**
     * Finds a reminder by ID
     */
    public Optional<Reminder> findById(Long id) {
        String sql = "SELECT * FROM reminders WHERE id = ?";
        
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setLong(1, id);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSetToReminder(rs));
                }
            }
            
        } catch (SQLException e) {
            logger.error("Error finding reminder by ID", e);
        }
        
        return Optional.empty();
    }
    
    /**
     * Finds all reminders for a user
     */
    public List<Reminder> findByUserId(Long userId) {
        String sql = "SELECT * FROM reminders WHERE user_id = ? ORDER BY time";
        List<Reminder> reminders = new ArrayList<>();
        
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setLong(1, userId);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    reminders.add(mapResultSetToReminder(rs));
                }
            }
            
        } catch (SQLException e) {
            logger.error("Error finding reminders for user", e);
        }
        
        return reminders;
    }
    
    /**
     * Finds active reminders for a specific day of week
     */
    public List<Reminder> findActiveRemindersByDayOfWeek(Long userId, int dayOfWeek) {
        String sql = "SELECT * FROM reminders WHERE user_id = ? AND active = 1 ORDER BY time";
        List<Reminder> reminders = new ArrayList<>();
        
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setLong(1, userId);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Reminder reminder = mapResultSetToReminder(rs);
                    if (reminder.isDayActive(dayOfWeek)) {
                        reminders.add(reminder);
                    }
                }
            }
            
        } catch (SQLException e) {
            logger.error("Error finding active reminders for day of week", e);
        }
        
        return reminders;
    }
    
    /**
     * Updates a reminder
     */
    public boolean update(Reminder reminder) {
        String sql = "UPDATE reminders SET title = ?, message = ?, type = ?, time = ?, " +
                "days_of_week = ?, active = ?, updated_at = ? WHERE id = ?";
        
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, reminder.getTitle());
            pstmt.setString(2, reminder.getMessage());
            pstmt.setString(3, reminder.getType().name());
            pstmt.setTime(4, Time.valueOf(reminder.getTime()));
            pstmt.setString(5, booleanArrayToString(reminder.getDaysOfWeek()));
            pstmt.setBoolean(6, reminder.isActive());
            pstmt.setTimestamp(7, Timestamp.valueOf(LocalDateTime.now()));
            pstmt.setLong(8, reminder.getId());
            
            int affectedRows = pstmt.executeUpdate();
            
            logger.info("Updated reminder: {}", reminder.getId());
            return affectedRows > 0;
            
        } catch (SQLException e) {
            logger.error("Error updating reminder", e);
            return false;
        }
    }
    
    /**
     * Deletes a reminder
     */
    public boolean delete(Long id) {
        String sql = "DELETE FROM reminders WHERE id = ?";
        
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setLong(1, id);
            
            int affectedRows = pstmt.executeUpdate();
            
            logger.info("Deleted reminder: {}", id);
            return affectedRows > 0;
            
        } catch (SQLException e) {
            logger.error("Error deleting reminder", e);
            return false;
        }
    }
    
    /**
     * Maps a ResultSet to a Reminder object
     */
    private Reminder mapResultSetToReminder(ResultSet rs) throws SQLException {
        Reminder reminder = new Reminder();
        reminder.setId(rs.getLong("id"));
        reminder.setUserId(rs.getLong("user_id"));
        reminder.setTitle(rs.getString("title"));
        reminder.setMessage(rs.getString("message"));
        reminder.setType(ReminderType.valueOf(rs.getString("type")));
        
        Time time = rs.getTime("time");
        if (time != null) {
            reminder.setTime(time.toLocalTime());
        }
        
        String daysOfWeekStr = rs.getString("days_of_week");
        reminder.setDaysOfWeek(stringToBooleanArray(daysOfWeekStr));
        
        reminder.setActive(rs.getBoolean("active"));
        
        Timestamp createdAt = rs.getTimestamp("created_at");
        if (createdAt != null) {
            reminder.setCreatedAt(createdAt.toLocalDateTime());
        }
        
        Timestamp updatedAt = rs.getTimestamp("updated_at");
        if (updatedAt != null) {
            reminder.setUpdatedAt(updatedAt.toLocalDateTime());
        }
        
        return reminder;
    }
    
    /**
     * Converts a boolean array to a string representation
     */
    private String booleanArrayToString(boolean[] array) {
        return Arrays.stream(array)
                .mapToObj(b -> b ? "1" : "0")
                .collect(Collectors.joining(","));
    }
    
    /**
     * Converts a string representation to a boolean array
     */
    private boolean[] stringToBooleanArray(String str) {
        boolean[] result = new boolean[7];
        if (str != null && !str.isEmpty()) {
            String[] parts = str.split(",");
            for (int i = 0; i < Math.min(parts.length, 7); i++) {
                result[i] = "1".equals(parts[i]);
            }
        }
        return result;
    }
}