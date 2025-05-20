package com.mindease.dao;

import com.mindease.model.ThoughtRecord;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Data Access Object for ThoughtRecord entity
 */
public class ThoughtRecordDAO {
    private static final Logger logger = LoggerFactory.getLogger(ThoughtRecordDAO.class);
    
    /**
     * Creates a new thought record
     */
    public ThoughtRecord create(ThoughtRecord record) {
        String sql = "INSERT INTO thought_records (user_id, situation, automatic_thought, emotions, emotion_intensity, " +
                "evidence_for, evidence_against, alternative_thought, outcome, new_emotion_intensity, created_at, updated_at) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            pstmt.setLong(1, record.getUserId());
            pstmt.setString(2, record.getSituation());
            pstmt.setString(3, record.getAutomaticThought());
            pstmt.setString(4, record.getEmotions());
            pstmt.setInt(5, record.getEmotionIntensity());
            pstmt.setString(6, record.getEvidenceFor());
            pstmt.setString(7, record.getEvidenceAgainst());
            pstmt.setString(8, record.getAlternativeThought());
            pstmt.setString(9, record.getOutcome());
            pstmt.setInt(10, record.getNewEmotionIntensity());
            pstmt.setTimestamp(11, Timestamp.valueOf(record.getCreatedAt()));
            pstmt.setTimestamp(12, Timestamp.valueOf(record.getUpdatedAt()));
            
            int affectedRows = pstmt.executeUpdate();
            
            if (affectedRows > 0) {
                try (ResultSet rs = pstmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        record.setId(rs.getLong(1));
                    }
                }
            }
            
            logger.info("Created thought record: {}", record.getId());
            return record;
            
        } catch (SQLException e) {
            logger.error("Error creating thought record", e);
            return null;
        }
    }
    
    /**
     * Finds a thought record by ID
     */
    public Optional<ThoughtRecord> findById(Long id) {
        String sql = "SELECT * FROM thought_records WHERE id = ?";
        
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setLong(1, id);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSetToThoughtRecord(rs));
                }
            }
            
        } catch (SQLException e) {
            logger.error("Error finding thought record by ID", e);
        }
        
        return Optional.empty();
    }
    
    /**
     * Finds all thought records for a user
     */
    public List<ThoughtRecord> findByUserId(Long userId) {
        String sql = "SELECT * FROM thought_records WHERE user_id = ? ORDER BY created_at DESC";
        List<ThoughtRecord> records = new ArrayList<>();
        
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setLong(1, userId);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    records.add(mapResultSetToThoughtRecord(rs));
                }
            }
            
        } catch (SQLException e) {
            logger.error("Error finding thought records for user", e);
        }
        
        return records;
    }
    
    /**
     * Updates a thought record
     */
    public boolean update(ThoughtRecord record) {
        String sql = "UPDATE thought_records SET situation = ?, automatic_thought = ?, emotions = ?, " +
                "emotion_intensity = ?, evidence_for = ?, evidence_against = ?, alternative_thought = ?, " +
                "outcome = ?, new_emotion_intensity = ?, updated_at = ? WHERE id = ?";
        
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, record.getSituation());
            pstmt.setString(2, record.getAutomaticThought());
            pstmt.setString(3, record.getEmotions());
            pstmt.setInt(4, record.getEmotionIntensity());
            pstmt.setString(5, record.getEvidenceFor());
            pstmt.setString(6, record.getEvidenceAgainst());
            pstmt.setString(7, record.getAlternativeThought());
            pstmt.setString(8, record.getOutcome());
            pstmt.setInt(9, record.getNewEmotionIntensity());
            pstmt.setTimestamp(10, Timestamp.valueOf(LocalDateTime.now()));
            pstmt.setLong(11, record.getId());
            
            int affectedRows = pstmt.executeUpdate();
            
            logger.info("Updated thought record: {}", record.getId());
            return affectedRows > 0;
            
        } catch (SQLException e) {
            logger.error("Error updating thought record", e);
            return false;
        }
    }
    
    /**
     * Maps a ResultSet to a ThoughtRecord object
     */
    private ThoughtRecord mapResultSetToThoughtRecord(ResultSet rs) throws SQLException {
        ThoughtRecord record = new ThoughtRecord();
        record.setId(rs.getLong("id"));
        record.setUserId(rs.getLong("user_id"));
        record.setSituation(rs.getString("situation"));
        record.setAutomaticThought(rs.getString("automatic_thought"));
        record.setEmotions(rs.getString("emotions"));
        record.setEmotionIntensity(rs.getInt("emotion_intensity"));
        record.setEvidenceFor(rs.getString("evidence_for"));
        record.setEvidenceAgainst(rs.getString("evidence_against"));
        record.setAlternativeThought(rs.getString("alternative_thought"));
        record.setOutcome(rs.getString("outcome"));
        record.setNewEmotionIntensity(rs.getInt("new_emotion_intensity"));
        
        Timestamp createdAt = rs.getTimestamp("created_at");
        if (createdAt != null) {
            record.setCreatedAt(createdAt.toLocalDateTime());
        }
        
        Timestamp updatedAt = rs.getTimestamp("updated_at");
        if (updatedAt != null) {
            record.setUpdatedAt(updatedAt.toLocalDateTime());
        }
        
        return record;
    }
}