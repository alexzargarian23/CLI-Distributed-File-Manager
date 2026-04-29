package DataTransferObject;

import java.time.LocalDateTime;
import java.util.Objects;

public class FileMetadata {

private String fileName;
private String path;
private long fileSize;
private String sha256Hash;
private LocalDateTime lastModified;

public FileMetadata(String fileName,String path, long fileSize, String sha256Hash, LocalDateTime lastModified) {

    this.fileName = fileName;
    this.path = path;
    this.fileSize = fileSize;
    this.sha256Hash = sha256Hash;
    this.lastModified = lastModified;

}

public String getFileName() {
return fileName;
}
public String getPath(){
    return path;
}
public long getFileSize() {
return fileSize;
}
public String getSha256Hash() {
    return sha256Hash;
}
public LocalDateTime getLastModified() {
    return lastModified;
}

public void setFileName(String fileName) {
this.fileName = fileName;
}
public void setPath(String path) {
    this.path = path;
}
public void setFileSize(long fileSize) {
    this.fileSize = fileSize;
}
public void setSha256Hash(String sha256Hash) {
    this.sha256Hash = sha256Hash;
}
public void setLastModified(LocalDateTime lastModified) {
    this.lastModified = lastModified;
}

    @Override
    public String toString() {
        return "File: " + path + "/" + fileName + " [Hash: " + sha256Hash + "]";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FileMetadata that = (FileMetadata) o;
        return Objects.equals(fileName, that.fileName) && Objects.equals(path, that.path);
    }
    @Override
    public int hashCode() {
        return Objects.hash(fileName, path);
    }


    public boolean hasChanged(FileMetadata other) {
        return !this.sha256Hash.equals(other.getSha256Hash());
    }

}
