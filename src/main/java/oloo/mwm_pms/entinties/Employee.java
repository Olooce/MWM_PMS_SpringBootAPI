package oloo.mwm_pms.entinties;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class Employee {
    private Long employeeId;
    private String name;
    private LocalDate dob;
    private String gender;
    private Long departmentId;
    private EmploymentType employmentType;
    private LocalDate employmentDate;
    private EmployeeStatus status;
    private String statusDescription;
    private LocalDate terminationDate;
    private LocalDateTime dateCreated;
    private LocalDateTime dateModified;

    // Getters
    public Long getEmployeeId() {
        return employeeId;
    }

    public String getName() {
        return name;
    }

    public LocalDate getDob() {
        return dob;
    }

    public String getGender() {
        return gender;
    }

    public Long getDepartmentId() {
        return departmentId;
    }

    public EmploymentType getEmploymentType() {
        return employmentType;
    }

    public LocalDate getEmploymentDate() {
        return employmentDate;
    }

    public EmployeeStatus getStatus() {
        return status;
    }

    public String getStatusDescription() {
        return statusDescription;
    }

    public LocalDate getTerminationDate() {
        return terminationDate;
    }

    public LocalDateTime getDateCreated() {
        return dateCreated;
    }

    public LocalDateTime getDateModified() {
        return dateModified;
    }

    // Setters
    public void setEmployeeId(Long employeeId) {
        this.employeeId = employeeId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDob(LocalDate dob) {
        this.dob = dob;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setDepartmentId(Long departmentId) {
        this.departmentId = departmentId;
    }

    public void setEmploymentType(EmploymentType employmentType) {
        this.employmentType = employmentType;
    }

    public void setEmploymentDate(LocalDate employmentDate) {
        this.employmentDate = employmentDate;
    }

    public void setStatus(EmployeeStatus status) {
        this.status = status;
    }

    public void setStatusDescription(String statusDescription) {
        this.statusDescription = statusDescription;
    }

    public void setTerminationDate(LocalDate terminationDate) {
        this.terminationDate = terminationDate;
    }

    public void setDateCreated(LocalDateTime dateCreated) {
        this.dateCreated = dateCreated;
    }

    public void setDateModified(LocalDateTime dateModified) {
        this.dateModified = dateModified;
    }
}
enum EmploymentType {
    FULL_TIME, PART_TIME, CONTRACT, TEMPORARY
}

enum EmployeeStatus {
    NEW, ACTIVE, LEAVING, TERMINATED
}