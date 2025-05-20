package com.mindease.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Database configuration and initialization
 */
public class DatabaseConfig {
    private static final Logger logger = LoggerFactory.getLogger(DatabaseConfig.class);
    private static final String DB_URL = "jdbc:sqlite:mindease.db";
    private static Connection connection;
    
    /**
     * Initializes the database connection and schema
     */
    public static void initialize() {
        try {
            // Create connection
            connection = DriverManager.getConnection(DB_URL);
            logger.info("Database connection established");
            
            // Create tables
            createTables();
            
        } catch (SQLException e) {
            logger.error("Error initializing database", e);
        }
    }
    
    /**
     * Creates database tables
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
            
            logger.info("Database tables created successfully");
        }
    }
    
    /**
     * Gets the database connection
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
    public static void shutdown() {
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