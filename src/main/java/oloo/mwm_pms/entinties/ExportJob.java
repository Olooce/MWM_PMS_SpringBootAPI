package oloo.mwm_pms.entinties;

import java.time.LocalDateTime;
import java.util.Date;

public class ExportJob {
    private Date timeInitiated;
    private String fileId;
    private String fileName;
    private long size;
    private long totalRows;
    private String errorMessage;
    private String status;
    private Date timeCompleted;


    public void setFileId(String fileId) {
        this.fileId = fileId;
    }

    public void setFilePath(String string) {
        this.fileName = string;
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
        this.size = fileSize;
    }

    public void setTimeCompleted(LocalDateTime timeCompleted) {
        this.timeCompleted = timeCompleted;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public void setDateCreated(LocalDateTime dateCreated) {
    }

    public void setDateModified(LocalDateTime dateModified) {
    }

    public void setExportId(long exportId) {
    }
}
