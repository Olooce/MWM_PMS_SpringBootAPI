package oloo.mwm_pms.entinties;

import java.time.LocalDateTime;

public class ExportJob {
    private LocalDateTime timeInitiated;
    private String fileId;
    private String fileName;
    private long fileSize;
    private long totalRows;
    private String errorMessage;
    private String status;
    private LocalDateTime timeCompleted;
    private String filePath;
    private LocalDateTime lastAccessTime;
    private LocalDateTime dateCreated;
    private LocalDateTime dateModified;
    private long exportId;


    public void setFileId(String fileId) {
        this.fileId = fileId;
    }
    public String getFileId() {
        return fileId;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }
    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public void setStatus(String status) {
        this.status = status;
    }
    public String getStatus() {
        return status;
    }

    public void setTimeInitiated(LocalDateTime timeInitiated) {
        this.timeInitiated = timeInitiated;
    }
    public LocalDateTime getTimeInitiated() {
        return timeInitiated;
    }

    public void setTotalRows(long totalRows) {
        this.totalRows = totalRows;
    }
    public long getTotalRows() {
        return totalRows;
    }

    public void setFileSize(long fileSize) {
        this.fileSize = fileSize;
    }
    public long getFileSize() {
        return fileSize;
    }

    public void setTimeCompleted(LocalDateTime timeCompleted) {
        this.timeCompleted = timeCompleted;
    }
    public LocalDateTime getTimeCompleted() {
        return timeCompleted;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
    public String getErrorMessage() {
        return errorMessage;
    }

    public void setDateCreated(LocalDateTime dateCreated) {
        this.dateCreated = dateCreated;
    }

    public void setDateModified(LocalDateTime dateModified) {
        this.dateModified = dateModified;
    }
    public LocalDateTime getDateModified() {
        return dateModified;
    }

    public void setExportId(long exportId) {
        this.exportId = exportId;
    }
    public long getExportId() {
        return exportId;
    }

    public void setLastAccessTime(LocalDateTime lastAccessTime) {
        this.lastAccessTime = lastAccessTime;
    }
    public LocalDateTime getLastAccessTime() {
        return lastAccessTime;
    }

    public String getFilePath() {
        return filePath;
    }


    public LocalDateTime getDateCreated() {
        return dateCreated;
    }

}
