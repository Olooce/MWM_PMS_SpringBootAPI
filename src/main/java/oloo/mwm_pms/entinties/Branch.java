package oloo.mwm_pms.entinties;

import java.time.LocalDateTime;

public class Branch {
    private Long branchId;
    private String branchName;
    private String branchCode;
    private Long noEmployees;
    private String description;
    private LocalDateTime dateCreated;
    private LocalDateTime dateModified;

    // Getters
    public Long getBranchId() {
        return branchId;
    }

    public String getBranchName() {
        return branchName;
    }

    public String getBranchCode() {
        return branchCode;
    }

    public Long getNoEmployees() {
        return noEmployees;
    }

    public String getDescription() {
        return description;
    }

    public LocalDateTime getDateCreated() {
        return dateCreated;
    }

    public LocalDateTime getDateModified() {
        return dateModified;
    }

    // Setters
    public void setBranchId(Long branchId) {
        this.branchId = branchId;
    }

    public void setBranchName(String branchName) {
        this.branchName = branchName;
    }

    public void setBranchCode(String branchCode) {
        this.branchCode = branchCode;
    }

    public void setNoEmployees(Long noEmployees) {
        this.noEmployees = noEmployees;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setDateCreated(LocalDateTime dateCreated) {
        this.dateCreated = dateCreated;
    }

    public void setDateModified(LocalDateTime dateModified) {
        this.dateModified = dateModified;
    }
}
