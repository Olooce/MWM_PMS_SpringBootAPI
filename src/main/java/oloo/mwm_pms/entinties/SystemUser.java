package oloo.mwm_pms.entinties;

import java.time.LocalDateTime;

public class SystemUser {
    private Long userId;
    private Long employeeId;
    private String username;
    private UserStatus userStatus;
    private String userStatusDescription;
    private String pwd;
    private PasswordStatus pwdStatus;
    private String pwdStatusDescription;
    private String pwdResetCode;
    private LocalDateTime pwdLastSet;
    private String role;
    private LocalDateTime dateCreated;
    private LocalDateTime dateModified;

    // Getters
    public Long getUserId() {
        return userId;
    }

    public Long getEmployeeId() {
        return employeeId;
    }

    public String getUsername() {
        return username;
    }

    public UserStatus getUserStatus() {
        return userStatus;
    }

    public String getUserStatusDescription() {
        return userStatusDescription;
    }

    public String getPwd() {
        return pwd;
    }

    public PasswordStatus getPwdStatus() {
        return pwdStatus;
    }

    public String getPwdStatusDescription() {
        return pwdStatusDescription;
    }

    public String getPwdResetCode() {
        return pwdResetCode;
    }

    public LocalDateTime getPwdLastSet() {
        return pwdLastSet;
    }

    public String getRole() {
        return role;
    }

    public LocalDateTime getDateCreated() {
        return dateCreated;
    }

    public LocalDateTime getDateModified() {
        return dateModified;
    }

    // Setters
    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public void setEmployeeId(Long employeeId) {
        this.employeeId = employeeId;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setUserStatus(UserStatus userStatus) {
        this.userStatus = userStatus;
    }

    public void setUserStatusDescription(String userStatusDescription) {
        this.userStatusDescription = userStatusDescription;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public void setPwdStatus(PasswordStatus pwdStatus) {
        this.pwdStatus = pwdStatus;
    }

    public void setPwdStatusDescription(String pwdStatusDescription) {
        this.pwdStatusDescription = pwdStatusDescription;
    }

    public void setPwdResetCode(String pwdResetCode) {
        this.pwdResetCode = pwdResetCode;
    }

    public void setPwdLastSet(LocalDateTime pwdLastSet) {
        this.pwdLastSet = pwdLastSet;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public void setDateCreated(LocalDateTime dateCreated) {
        this.dateCreated = dateCreated;
    }

    public void setDateModified(LocalDateTime dateModified) {
        this.dateModified = dateModified;
    }
}

