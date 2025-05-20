package com.mindease.dao;

import com.mindease.model.Goal;
import com.mindease.model.Goal.GoalCategory;
import com.mindease.model.Goal.GoalFrequency;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Data Access Object for Goal entity
 */
public class GoalDAO {
    private static final Logger logger = LoggerFactory.getLogger(GoalDAO.class);
    
    /**
     * Creates a new goal
     */
    public Goal create(Goal goal) {
        String sql = "INSERT INTO goals (user_id, title, description, category, frequency, target_value, " +
                "current_progress, start_date, end_date, completed, created_at, updated_at) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            pstmt.setLong(1, goal.getUserId());
            pstmt.setString(2, goal.getTitle());
            pstmt.setString(3, goal.getDescription());
            pstmt.setString(4, goal.getCategory().name());
            pstmt.setString(5, goal.getFrequency().name());
            pstmt.setInt(6, goal.getTargetValue());
            pstmt.setInt(7, goal.getCurrentProgress());
            pstmt.setDate(8, Date.valueOf(goal.getStartDate()));
            
            if (goal.getEndDate() != null) {
                pstmt.setDate(9, Date.valueOf(goal.getEndDate()));
            } else {
                pstmt.setNull(9, Types.DATE);
            }
            
            pstmt.setBoolean(10, goal.isCompleted());
            pstmt.setTimestamp(11, Timestamp.valueOf(goal.getCreatedAt()));
            pstmt.setTimestamp(12, Timestamp.valueOf(goal.getUpdatedAt()));
            
            int affectedRows = pstmt.executeUpdate();
            
            if (affectedRows > 0) {
                try (ResultSet rs = pstmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        goal.setId(rs.getLong(1));
                    }
                }
            }
            
            logger.info("Created goal: {}", goal.getTitle());
            return goal;
            
        } catch (SQLException e) {
            logger.error("Error creating goal", e);
            return null;
        }
    }
    
    /**
     * Finds a goal by ID
     */
    public Optional<Goal> findById(Long id) {
        String sql = "SELECT * FROM goals WHERE id = ?";
        
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setLong(1, id);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSetToGoal(rs));
                }
            }
            
        } catch (SQLException e) {
            logger.error("Error finding goal by ID", e);
        }
        
        return Optional.empty();
    }
    
    /**
     * Finds all goals for a user
     */
    public List<Goal> findByUserId(Long userId) {
        String sql = "SELECT * FROM goals WHERE user_id = ? ORDER BY created_at DESC";
        List<Goal> goals = new ArrayList<>();
        
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setLong(1, userId);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    goals.add(mapResultSetToGoal(rs));
                }
            }
            
        } catch (SQLException e) {
            logger.error("Error finding goals for user", e);
        }
        
        return goals;
    }
    
    /**
     * Updates a goal
     */
    public boolean update(Goal goal) {
        String sql = "UPDATE goals SET title = ?, description = ?, category = ?, frequency = ?, " +
                "target_value = ?, current_progress = ?, start_date = ?, end_date = ?, " +
                "completed = ?, updated_at = ? WHERE id = ?";
        
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, goal.getTitle());
            pstmt.setString(2, goal.getDescription());
            pstmt.setString(3, goal.getCategory().name());
            pstmt.setString(4, goal.getFrequency().name());
            pstmt.setInt(5, goal.getTargetValue());
            pstmt.setInt(6, goal.getCurrentProgress());
            pstmt.setDate(7, Date.valueOf(goal.getStartDate()));
            
            if (goal.getEndDate() != null) {
                pstmt.setDate(8, Date.valueOf(goal.getEndDate()));
            } else {
                pstmt.setNull(8, Types.DATE);
            }
            
            pstmt.setBoolean(9, goal.isCompleted());
            pstmt.setTimestamp(10, Timestamp.valueOf(LocalDateTime.now()));
            pstmt.setLong(11, goal.getId());
            
            int affectedRows = pstmt.executeUpdate();
            
            logger.info("Updated goal: {}", goal.getId());
            return affectedRows > 0;
            
        } catch (SQLException e) {
            logger.error("Error updating goal", e);
            return false;
        }
    }
    
    /**
     * Maps a ResultSet to a Goal object
     */
    private Goal mapResultSetToGoal(ResultSet rs) throws SQLException {
        Goal goal = new Goal();
        goal.setId(rs.getLong("id"));
        goal.setUserId(rs.getLong("user_id"));
        goal.setTitle(rs.getString("title"));
        goal.setDescription(rs.getString("description"));
        goal.setCategory(GoalCategory.valueOf(rs.getString("category")));
        goal.setFrequency(GoalFrequency.valueOf(rs.getString("frequency")));
        goal.setTargetValue(rs.getInt("target_value"));
        goal.setCurrentProgress(rs.getInt("current_progress"));
        
        Date startDate = rs.getDate("start_date");
        if (startDate != null) {
            goal.setStartDate(startDate.toLocalDate());
        }
        
        Date endDate = rs.getDate("end_date");
        if (endDate != null) {
            goal.setEndDate(endDate.toLocalDate());
        }
        
        goal.setCompleted(rs.getBoolean("completed"));
        
        Timestamp createdAt = rs.getTimestamp("created_at");
        if (createdAt != null) {
            goal.setCreatedAt(createdAt.toLocalDateTime());
        }
        
        Timestamp updatedAt = rs.getTimestamp("updated_at");
        if (updatedAt != null) {
            goal.setUpdatedAt(updatedAt.toLocalDateTime());
        }
        
        return goal;
    }
}