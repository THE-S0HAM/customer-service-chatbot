package com.mindease.dao;

import com.mindease.model.MoodEntry;
import com.mindease.model.MoodEntry.MoodType;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Data Access Object for MoodEntry entity
 */
public class MoodEntryDAO {
    private static final Logger logger = LoggerFactory.getLogger(MoodEntryDAO.class);
    
    /**
     * Creates a new mood entry in the database
     * @param moodEntry MoodEntry to create
     * @return MoodEntry with ID populated
     */
    public MoodEntry create(MoodEntry moodEntry) {
        String sql = "INSERT INTO mood_entries (user_id, mood, intensity_level, notes, created_at) VALUES (?, ?, ?, ?, ?)";
        
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            pstmt.setLong(1, moodEntry.getUserId());
            pstmt.setString(2, moodEntry.getMood().name());
            pstmt.setInt(3, moodEntry.getIntensityLevel());
            pstmt.setString(4, moodEntry.getNotes());
            pstmt.setTimestamp(5, Timestamp.valueOf(moodEntry.getCreatedAt()));
            
            int affectedRows = pstmt.executeUpdate();
            
            if (affectedRows > 0) {
                try (ResultSet rs = pstmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        moodEntry.setId(rs.getLong(1));
                    }
                }
            }
            
            logger.info("Created mood entry for user: {}", moodEntry.getUserId());
            return moodEntry;
            
        } catch (SQLException e) {
            logger.error("Error creating mood entry", e);
            return null;
        }
    }
    
    /**
     * Finds a mood entry by ID
     * @param id MoodEntry ID
     * @return Optional containing the mood entry if found
     */
    public Optional<MoodEntry> findById(Long id) {
        String sql = "SELECT * FROM mood_entries WHERE id = ?";
        
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setLong(1, id);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSetToMoodEntry(rs));
                }
            }
            
        } catch (SQLException e) {
            logger.error("Error finding mood entry by ID", e);
        }
        
        return Optional.empty();
    }
    
    /**
     * Finds all mood entries for a user
     * @param userId User ID
     * @return List of mood entries
     */
    public List<MoodEntry> findByUserId(Long userId) {
        String sql = "SELECT * FROM mood_entries WHERE user_id = ? ORDER BY created_at DESC";
        List<MoodEntry> entries = new ArrayList<>();
        
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setLong(1, userId);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    entries.add(mapResultSetToMoodEntry(rs));
                }
            }
            
        } catch (SQLException e) {
            logger.error("Error finding mood entries for user", e);
        }
        
        return entries;
    }
    
    /**
     * Finds mood entries for a user within a date range
     * @param userId User ID
     * @param startDate Start date (inclusive)
     * @param endDate End date (inclusive)
     * @return List of mood entries
     */
    public List<MoodEntry> findByUserIdAndDateRange(Long userId, LocalDateTime startDate, LocalDateTime endDate) {
        String sql = "SELECT * FROM mood_entries WHERE user_id = ? AND created_at BETWEEN ? AND ? ORDER BY created_at";
        List<MoodEntry> entries = new ArrayList<>();
        
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setLong(1, userId);
            pstmt.setTimestamp(2, Timestamp.valueOf(startDate));
            pstmt.setTimestamp(3, Timestamp.valueOf(endDate));
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    entries.add(mapResultSetToMoodEntry(rs));
                }
            }
            
        } catch (SQLException e) {
            logger.error("Error finding mood entries by date range", e);
        }
        
        return entries;
    }
    
    /**
     * Updates a mood entry
     * @param moodEntry MoodEntry to update
     * @return true if successful
     */
    public boolean update(MoodEntry moodEntry) {
        String sql = "UPDATE mood_entries SET mood = ?, intensity_level = ?, notes = ? WHERE id = ?";
        
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, moodEntry.getMood().name());
            pstmt.setInt(2, moodEntry.getIntensityLevel());
            pstmt.setString(3, moodEntry.getNotes());
            pstmt.setLong(4, moodEntry.getId());
            
            int affectedRows = pstmt.executeUpdate();
            
            logger.info("Updated mood entry: {}", moodEntry.getId());
            return affectedRows > 0;
            
        } catch (SQLException e) {
            logger.error("Error updating mood entry", e);
            return false;
        }
    }
    
    /**
     * Deletes a mood entry
     * @param id MoodEntry ID
     * @return true if successful
     */
    public boolean delete(Long id) {
        String sql = "DELETE FROM mood_entries WHERE id = ?";
        
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setLong(1, id);
            
            int affectedRows = pstmt.executeUpdate();
            
            logger.info("Deleted mood entry: {}", id);
            return affectedRows > 0;
            
        } catch (SQLException e) {
            logger.error("Error deleting mood entry", e);
            return false;
        }
    }
    
    /**
     * Maps a ResultSet to a MoodEntry object
     * @param rs ResultSet containing mood entry data
     * @return MoodEntry object
     * @throws SQLException if mapping fails
     */
    private MoodEntry mapResultSetToMoodEntry(ResultSet rs) throws SQLException {
        MoodEntry moodEntry = new MoodEntry();
        moodEntry.setId(rs.getLong("id"));
        moodEntry.setUserId(rs.getLong("user_id"));
        moodEntry.setMood(MoodType.valueOf(rs.getString("mood")));
        moodEntry.setIntensityLevel(rs.getInt("intensity_level"));
        moodEntry.setNotes(rs.getString("notes"));
        
        Timestamp createdAt = rs.getTimestamp("created_at");
        if (createdAt != null) {
            moodEntry.setCreatedAt(createdAt.toLocalDateTime());
        }
        
        return moodEntry;
    }
}