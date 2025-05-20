package com.mindease.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Manages database connections and schema creation
 */
public class DatabaseManager {
    private static final Logger logger = LoggerFactory.getLogger(DatabaseManager.class);
    private static final String DB_URL = "jdbc:sqlite:mindease.db";
    private static Connection connection;
    
    /**
     * Initializes the database connection and creates tables if they don't exist
     */
    public static void initialize() {
        try {
            // Load SQLite JDBC driver
            Class.forName("org.sqlite.JDBC");
            
            // Create connection
            connection = DriverManager.getConnection(DB_URL);
            logger.info("Database connection established");
            
            // Create tables
            createTables();
            
        } catch (ClassNotFoundException e) {
            logger.error("SQLite JDBC driver not found", e);
        } catch (SQLException e) {
            logger.error("Error initializing database", e);
        }
    }
    
    /**
     * Creates database tables if they don't exist
     */
    private static void createTables() throws SQLException {
        try (Statement stmt = connection.createStatement()) {
            // Users table
            stmt.execute("CREATE TABLE IF NOT EXISTS users (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "username TEXT UNIQUE NOT NULL," +
                    "password_hash TEXT NOT NULL," +
                    "email TEXT UNIQUE," +
                    "first_name TEXT," +
                    "last_name TEXT," +
                    "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP," +
                    "last_login TIMESTAMP)");
            
            // Mood entries table
            stmt.execute("CREATE TABLE IF NOT EXISTS mood_entries (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "user_id INTEGER NOT NULL," +
                    "mood TEXT NOT NULL," +
                    "intensity_level INTEGER NOT NULL," +
                    "notes TEXT," +
                    "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP," +
                    "FOREIGN KEY (user_id) REFERENCES users(id))");
            
            // Journal entries table
            stmt.execute("CREATE TABLE IF NOT EXISTS journal_entries (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "user_id INTEGER NOT NULL," +
                    "title TEXT NOT NULL," +
                    "content TEXT NOT NULL," +
                    "prompt_used TEXT," +
                    "sentiment_score TEXT," +
                    "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP," +
                    "updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP," +
                    "FOREIGN KEY (user_id) REFERENCES users(id))");
            
            // Thought records table
            stmt.execute("CREATE TABLE IF NOT EXISTS thought_records (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "user_id INTEGER NOT NULL," +
                    "situation TEXT NOT NULL," +
                    "automatic_thought TEXT NOT NULL," +
                    "emotions TEXT NOT NULL," +
                    "emotion_intensity INTEGER NOT NULL," +
                    "evidence_for TEXT," +
                    "evidence_against TEXT," +
                    "alternative_thought TEXT," +
                    "outcome TEXT," +
                    "new_emotion_intensity INTEGER," +
                    "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP," +
                    "updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP," +
                    "FOREIGN KEY (user_id) REFERENCES users(id))");
            
            // Goals table
            stmt.execute("CREATE TABLE IF NOT EXISTS goals (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "user_id INTEGER NOT NULL," +
                    "title TEXT NOT NULL," +
                    "description TEXT," +
                    "category TEXT NOT NULL," +
                    "frequency TEXT NOT NULL," +
                    "target_value INTEGER NOT NULL," +
                    "current_progress INTEGER DEFAULT 0," +
                    "start_date DATE NOT NULL," +
                    "end_date DATE," +
                    "completed BOOLEAN DEFAULT 0," +
                    "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP," +
                    "updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP," +
                    "FOREIGN KEY (user_id) REFERENCES users(id))");
            
            // Reminders table
            stmt.execute("CREATE TABLE IF NOT EXISTS reminders (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "user_id INTEGER NOT NULL," +
                    "title TEXT NOT NULL," +
                    "message TEXT," +
                    "type TEXT NOT NULL," +
                    "time TIME NOT NULL," +
                    "days_of_week TEXT NOT NULL," + // Stored as JSON array
                    "active BOOLEAN DEFAULT 1," +
                    "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP," +
                    "updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP," +
                    "FOREIGN KEY (user_id) REFERENCES users(id))");
            
            logger.info("Database tables created successfully");
        }
    }
    
    /**
     * Gets a database connection
     * @return Connection object
     * @throws SQLException if connection cannot be established
     */
    public static Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            initialize();
        }
        return connection;
    }
    
    /**
     * Closes the database connection
     */
    public static void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
                logger.info("Database connection closed");
            } catch (SQLException e) {
                logger.error("Error closing database connection", e);
            }
        }
    }
}