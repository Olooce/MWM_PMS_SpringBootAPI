package oloo.mwm_pms.dtos;

import java.time.LocalDate;

public class EmployeeDTO {
    private String name;
    private LocalDate dob;
    private String gender;
    private Long departmentId;
    private String employmentType;
    private LocalDate employmentDate;
    private String status;
    private String statusDescription;
    private LocalDate terminationDate;

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

    public String getEmploymentType() {
        return employmentType;
    }

    public LocalDate getEmploymentDate() {
        return employmentDate;
    }

    public String getStatus() {
        return status;
    }

    public String getStatusDescription() {
        return statusDescription;
    }

    public LocalDate getTerminationDate() {
        return terminationDate;
    }
}
