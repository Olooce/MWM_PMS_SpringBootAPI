package oloo.mwm_pms.entinties;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;

public class SystemUser implements UserDetails {
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

    @Override
    public String getUsername() {
        return username;
    }

    public UserStatus getUserStatus() {
        return userStatus;
    }

    public String getUserStatusDescription() {
        return userStatusDescription;
    }

    @Override
    public String getPassword() {
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

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(new SimpleGrantedAuthority(role));
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return userStatus == UserStatus.ACTIVE;
    }
}
