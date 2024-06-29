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

    public void setBranchId(long branchId) {
        this.branchId = branchId;
    }

    public void setBranchName(String branchName) {
        this.branchName = branchName;
    }

    public void setBranchCode(String branchCode) {
        this.branchCode = branchCode;
    }

    public void setNoEmployees(long noEmployees) {
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
