package com.mindease.dao;

import com.mindease.model.JournalEntry;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Data Access Object for JournalEntry entity
 */
public class JournalEntryDAO {
    private static final Logger logger = LoggerFactory.getLogger(JournalEntryDAO.class);
    
    /**
     * Creates a new journal entry
     */
    public JournalEntry create(JournalEntry entry) {
        String sql = "INSERT INTO journal_entries (user_id, title, content, prompt_used, sentiment_score, created_at, updated_at) VALUES (?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            pstmt.setLong(1, entry.getUserId());
            pstmt.setString(2, entry.getTitle());
            pstmt.setString(3, entry.getContent());
            pstmt.setString(4, entry.getPromptUsed());
            pstmt.setString(5, entry.getSentimentScore());
            pstmt.setTimestamp(6, Timestamp.valueOf(entry.getCreatedAt()));
            pstmt.setTimestamp(7, Timestamp.valueOf(entry.getUpdatedAt()));
            
            int affectedRows = pstmt.executeUpdate();
            
            if (affectedRows > 0) {
                try (ResultSet rs = pstmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        entry.setId(rs.getLong(1));
                    }
                }
            }
            
            logger.info("Created journal entry: {}", entry.getTitle());
            return entry;
            
        } catch (SQLException e) {
            logger.error("Error creating journal entry", e);
            return null;
        }
    }
    
    /**
     * Finds a journal entry by ID
     */
    public Optional<JournalEntry> findById(Long id) {
        String sql = "SELECT * FROM journal_entries WHERE id = ?";
        
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setLong(1, id);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSetToJournalEntry(rs));
                }
            }
            
        } catch (SQLException e) {
            logger.error("Error finding journal entry by ID", e);
        }
        
        return Optional.empty();
    }
    
    /**
     * Finds all journal entries for a user
     */
    public List<JournalEntry> findByUserId(Long userId) {
        String sql = "SELECT * FROM journal_entries WHERE user_id = ? ORDER BY created_at DESC";
        List<JournalEntry> entries = new ArrayList<>();
        
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setLong(1, userId);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    entries.add(mapResultSetToJournalEntry(rs));
                }
            }
            
        } catch (SQLException e) {
            logger.error("Error finding journal entries for user", e);
        }
        
        return entries;
    }
    
    /**
     * Updates a journal entry
     */
    public boolean update(JournalEntry entry) {
        String sql = "UPDATE journal_entries SET title = ?, content = ?, prompt_used = ?, sentiment_score = ?, updated_at = ? WHERE id = ?";
        
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, entry.getTitle());
            pstmt.setString(2, entry.getContent());
            pstmt.setString(3, entry.getPromptUsed());
            pstmt.setString(4, entry.getSentimentScore());
            pstmt.setTimestamp(5, Timestamp.valueOf(LocalDateTime.now()));
            pstmt.setLong(6, entry.getId());
            
            int affectedRows = pstmt.executeUpdate();
            
            logger.info("Updated journal entry: {}", entry.getId());
            return affectedRows > 0;
            
        } catch (SQLException e) {
            logger.error("Error updating journal entry", e);
            return false;
        }
    }
    
    /**
     * Deletes a journal entry
     */
    public boolean delete(Long id) {
        String sql = "DELETE FROM journal_entries WHERE id = ?";
        
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setLong(1, id);
            
            int affectedRows = pstmt.executeUpdate();
            
            logger.info("Deleted journal entry: {}", id);
            return affectedRows > 0;
            
        } catch (SQLException e) {
            logger.error("Error deleting journal entry", e);
            return false;
        }
    }
    
    /**
     * Maps a ResultSet to a JournalEntry object
     */
    private JournalEntry mapResultSetToJournalEntry(ResultSet rs) throws SQLException {
        JournalEntry entry = new JournalEntry();
        entry.setId(rs.getLong("id"));
        entry.setUserId(rs.getLong("user_id"));
        entry.setTitle(rs.getString("title"));
        entry.setContent(rs.getString("content"));
        entry.setPromptUsed(rs.getString("prompt_used"));
        entry.setSentimentScore(rs.getString("sentiment_score"));
        
        Timestamp createdAt = rs.getTimestamp("created_at");
        if (createdAt != null) {
            entry.setCreatedAt(createdAt.toLocalDateTime());
        }
        
        Timestamp updatedAt = rs.getTimestamp("updated_at");
        if (updatedAt != null) {
            entry.setUpdatedAt(updatedAt.toLocalDateTime());
        }
        
        return entry;
    }
}