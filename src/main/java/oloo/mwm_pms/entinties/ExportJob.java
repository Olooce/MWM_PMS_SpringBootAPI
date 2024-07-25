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

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setTimeInitiated(LocalDateTime timeInitiated) {
        this.timeInitiated = timeInitiated;
    }

    public void setTotalRows(long totalRows) {
        this.totalRows = totalRows;
    }

    public void setFileSize(long fileSize) {
        this.fileSize = fileSize;
    }

    public void setTimeCompleted(LocalDateTime timeCompleted) {
        this.timeCompleted = timeCompleted;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public void setDateCreated(LocalDateTime dateCreated) {
        this.dateCreated = dateCreated;
    }

    public void setDateModified(LocalDateTime dateModified) {
        this.dateModified = dateModified;
    }

    public void setExportId(long exportId) {
        this.exportId = exportId;
    }

    public void setLastAccessTime(LocalDateTime lastAccessTime) {
        this.lastAccessTime = lastAccessTime;
    }

    public String getFilePath() {
        return filePath;
    }
}
