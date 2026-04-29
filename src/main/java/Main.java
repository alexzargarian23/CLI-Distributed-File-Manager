import DataTransferObject.FileMetadata;
import DataTransferObject.FileScanner;
import dataBase.DatabaseManager;

import java.util.List;

public static void main(String[] args) {
    DatabaseManager db = new DatabaseManager();
    db.setupDatabase();

    FileScanner scanner = new FileScanner();
    String myFolderPath = "/Users/alex/Desktop/demo111";

    // 1. Fetch what we already know from SQL
    List<FileMetadata> dbFiles = db.getAllStoredFiles();

    // 2. Scan the physical folder for current state
    List<FileMetadata> currentFiles = scanner.scanProjectFolder(myFolderPath);

    // 3. Compare and Act
    java.util.List<FileMetadata> newFilesToSave = new java.util.ArrayList<>();

    for (FileMetadata current : currentFiles) {
        // Find if this file path exists in our database list
        FileMetadata existing = dbFiles.stream()
                .filter(f -> f.getPath().equals(current.getPath()))
                .findFirst()
                .orElse(null);

        if (existing == null) {
            System.out.println("🆕 NEW FILE: " + current.getFileName());
            newFilesToSave.add(current);
        } else if (!existing.getSha256Hash().equals(current.getSha256Hash())) {
            System.out.println("⚠️ MODIFIED: " + current.getFileName());
            // In a full sync tool, you'd perform an UPDATE here.
            // For now, let's just mark it for re-saving.
            newFilesToSave.add(current);
        } else {
            System.out.println("✅ UNCHANGED: " + current.getFileName());
        }
    }

    // 4. Save only what is actually new or changed
    if (!newFilesToSave.isEmpty()) {
        db.saveFiles(newFilesToSave, "project-alpha");
    } else {
        System.out.println("🚀 Everything is up to date!");
    }
}