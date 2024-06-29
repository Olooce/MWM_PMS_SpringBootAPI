package oloo.mwm_pms.entinties;

import java.time.LocalDateTime;

public class BankDetails {
    private Long bankDetailId;
    private Long employeeId;
    private String accountNo;
    private String bankName;
    private String branchCode;
    private LocalDateTime dateCreated;
    private LocalDateTime dateModified;

    // Getters
    public Long getBankDetailId() {
        return bankDetailId;
    }

    public Long getEmployeeId() {
        return employeeId;
    }

    public String getAccountNo() {
        return accountNo;
    }

    public String getBankName() {
        return bankName;
    }

    public String getBranchCode() {
        return branchCode;
    }

    public LocalDateTime getDateCreated() {
        return dateCreated;
    }

    public LocalDateTime getDateModified() {
        return dateModified;
    }

    // Setters
    public void setBankDetailId(Long bankDetailId) {
        this.bankDetailId = bankDetailId;
    }

    public void setEmployeeId(Long employeeId) {
        this.employeeId = employeeId;
    }

    public void setAccountNo(String accountNo) {
        this.accountNo = accountNo;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public void setBranchCode(String branchCode) {
        this.branchCode = branchCode;
    }

    public void setDateCreated(LocalDateTime dateCreated) {
        this.dateCreated = dateCreated;
    }

    public void setDateModified(LocalDateTime dateModified) {
        this.dateModified = dateModified;
    }
}
