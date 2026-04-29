package DataTransferObject;

import DataTransferObject.FileMetadata;
import java.io.*;
import java.nio.file.*;
import java.security.MessageDigest;
import java.time.*;
import java.util.*;
import java.util.stream.Collectors;

public class FileScanner {

    /**
     * The main entry point to scan a directory.
     * Uses Java NIO to walk the file tree.
     */
    public List<FileMetadata> scanProjectFolder(String folderPath) {
        Path root = Paths.get(folderPath);

        try (var stream = Files.walk(root)) {
            return stream
                    .filter(Files::isRegularFile) // Ignore directories, only pick files
                    .map(path -> createFileMetadata(root, path))
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());
        } catch (IOException e) {
            System.err.println("Error scanning folder: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    /**
     * Helper to transform a Path into our FileMetadata DTO.
     */
    private FileMetadata createFileMetadata(Path root, Path filePath) {
        try {
            File file = filePath.toFile();
            String name = file.getName();

            // relativePath: e.g., "src/main.java" instead of "C:/Users/Project/src/main.java"
            String relativePath = root.relativize(filePath).toString();
            long size = file.length();
            String hash = generateSHA256(file);

            LocalDateTime lastModified = LocalDateTime.ofInstant(
                    Instant.ofEpochMilli(file.lastModified()), ZoneId.systemDefault()
            );

            return new FileMetadata(name, relativePath, size, hash, lastModified);
        } catch (Exception e) {
            System.err.println("Could not process file " + filePath + ": " + e.getMessage());
            return null;
        }
    }

    /**
     * SHA-256 Hashing Algorithm
     * Reads file in 8KB chunks to stay memory-efficient.
     */
    private String generateSHA256(File file) throws Exception {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");

        // Using a buffer ensures we don't load the whole file into RAM at once
        try (InputStream fis = new FileInputStream(file)) {
            byte[] buffer = new byte[8192];
            int nread;
            while ((nread = fis.read(buffer)) != -1) {
                digest.update(buffer, 0, nread);
            }
        }

        // Convert bytes to Hexadecimal String
        StringBuilder result = new StringBuilder();
        for (byte b : digest.digest()) {
            result.append(String.format("%02x", b));
        }
        return result.toString();
    }
}