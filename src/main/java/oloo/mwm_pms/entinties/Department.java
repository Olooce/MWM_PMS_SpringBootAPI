package oloo.mwm_pms.entinties;

import java.time.LocalDateTime;

public class Department {
    private Long departmentId;
    private Long branchId;
    private String departmentName;
    private String departmentCode;
    private LocalDateTime dateCreated;
    private LocalDateTime dateModified;

    // Getters
    public Long getDepartmentId() {
        return departmentId;
    }

    public Long getBranchId() {
        return branchId;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public String getDepartmentCode() {
        return departmentCode;
    }

    public LocalDateTime getDateCreated() {
        return dateCreated;
    }

    public LocalDateTime getDateModified() {
        return dateModified;
    }

    // Setters
    public void setDepartmentId(Long departmentId) {
        this.departmentId = departmentId;
    }

    public void setBranchId(Long branchId) {
        this.branchId = branchId;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public void setDepartmentCode(String departmentCode) {
        this.departmentCode = departmentCode;
    }

    public void setDateCreated(LocalDateTime dateCreated) {
        this.dateCreated = dateCreated;
    }

    public void setDateModified(LocalDateTime dateModified) {
        this.dateModified = dateModified;
    }
}
