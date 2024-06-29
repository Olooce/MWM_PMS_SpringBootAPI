package oloo.mwm_pms.entinties;

import java.time.LocalDateTime;

public class ContactInfo {
    private Long contactInfoId;
    private Long employeeId;
    private String address;
    private String phoneNo;
    private String email;
    private String emergencyContactNo;
    private String emergencyContactName;
    private LocalDateTime dateCreated;
    private LocalDateTime dateModified;

    // Getters
    public Long getContactInfoId() {
        return contactInfoId;
    }

    public Long getEmployeeId() {
        return employeeId;
    }

    public String getAddress() {
        return address;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public String getEmail() {
        return email;
    }

    public String getEmergencyContactNo() {
        return emergencyContactNo;
    }

    public String getEmergencyContactName() {
        return emergencyContactName;
    }

    public LocalDateTime getDateCreated() {
        return dateCreated;
    }

    public LocalDateTime getDateModified() {
        return dateModified;
    }

    // Setters
    public void setContactInfoId(Long contactInfoId) {
        this.contactInfoId = contactInfoId;
    }

    public void setEmployeeId(Long employeeId) {
        this.employeeId = employeeId;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setEmergencyContactNo(String emergencyContactNo) {
        this.emergencyContactNo = emergencyContactNo;
    }

    public void setEmergencyContactName(String emergencyContactName) {
        this.emergencyContactName = emergencyContactName;
    }

    public void setDateCreated(LocalDateTime dateCreated) {
        this.dateCreated = dateCreated;
    }

    public void setDateModified(LocalDateTime dateModified) {
        this.dateModified = dateModified;
    }
}
