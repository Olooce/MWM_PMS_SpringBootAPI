package oloo.mwm_pms.dtos;

import java.time.LocalDate;

public class AddEmployee {
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
}
