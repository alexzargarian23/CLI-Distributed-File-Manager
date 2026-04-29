package dataBase;

import DataTransferObject.FileMetadata; // Adjust this import based on your package for FileMetadata
import userRegistration.User;
import java.sql.*;
import java.util.List;

public class DatabaseManager {

    /**
     * Creates the tables if they don't exist.
     * Run this at the start of your Main method.
     */
    public void setupDatabase() {
        String createUsers = """
            CREATE TABLE IF NOT EXISTS users (
                id VARCHAR(255) PRIMARY KEY,
                username VARCHAR(100) UNIQUE,
                password_hash VARCHAR(255)
            );
            """;

        String createFiles = """
            CREATE TABLE IF NOT EXISTS files (
                id IDENTITY PRIMARY KEY,
                project_id VARCHAR(255),
                file_name VARCHAR(255),
                relative_path TEXT,
                file_size BIGINT,
                sha256_hash VARCHAR(64),
                last_modified TIMESTAMP
            );
            """;

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement()) {

            stmt.execute(createUsers);
            stmt.execute(createFiles);
            System.out.println("✅ Database schema initialized.");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Saves a list of files to the database.
     * This uses Batch Processing for better performance.
     */
    public void saveFiles(List<FileMetadata> files, String projectId) {
        // We use MERGE so we don't get duplicates like in your current DB
        String sql = """
        MERGE INTO files (project_id, file_name, relative_path, file_size, sha256_hash, last_modified) 
        KEY(relative_path) 
        VALUES (?, ?, ?, ?, ?, ?)
        """;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            for (FileMetadata file : files) {
                pstmt.setString(1, projectId);
                pstmt.setString(2, file.getFileName());
                pstmt.setString(3, file.getPath());
                pstmt.setLong(4, file.getFileSize());
                pstmt.setString(5, file.getSha256Hash());
                pstmt.setTimestamp(6, Timestamp.valueOf(file.getLastModified()));
                pstmt.addBatch();
            }

            pstmt.executeBatch();
            System.out.println("✅ Database synced (" + files.size() + " files processed).");

        } catch (SQLException e) {
            System.err.println("❌ Error syncing files: " + e.getMessage());
        }
    }
    public List<FileMetadata> getAllStoredFiles() {
        List<FileMetadata> storedFiles = new java.util.ArrayList<>();
        String sql = "SELECT * FROM files";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                // Reconstructing the FileMetadata object from the database row
                FileMetadata meta = new FileMetadata(
                        rs.getString("file_name"),
                        rs.getString("relative_path"),
                        rs.getLong("file_size"),
                        rs.getString("sha256_hash"),
                        rs.getTimestamp("last_modified").toLocalDateTime()
                );
                storedFiles.add(meta);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return storedFiles;
    }
}