package oloo.mwm_pms.entities;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class Allowance {
    private Long allowanceId;
    private Long employeeId;
    private LocalDate month;
    private String allowanceName;
    private String allowanceDescription;
    private BigDecimal allowanceRate;
    private String allowanceType;
    private BigDecimal allowanceAmount;
    private LocalDateTime dateCreated;
    private LocalDateTime dateModified;

    // Getters
    public Long getAllowanceId() {
        return allowanceId;
    }

    public Long getEmployeeId() {
        return employeeId;
    }

    public LocalDate getMonth() {
        return month;
    }

    public String getAllowanceName() {
        return allowanceName;
    }

    public String getAllowanceDescription() {
        return allowanceDescription;
    }

    public BigDecimal getAllowanceRate() {
        return allowanceRate;
    }

    public String getAllowanceType() {
        return allowanceType;
    }

    public BigDecimal getAllowanceAmount() {
        return allowanceAmount;
    }

    public LocalDateTime getDateCreated() {
        return dateCreated;
    }

    public LocalDateTime getDateModified() {
        return dateModified;
    }

    // Setters
    public void setAllowanceId(Long allowanceId) {
        this.allowanceId = allowanceId;
    }

    public void setEmployeeId(Long employeeId) {
        this.employeeId = employeeId;
    }

    public void setMonth(LocalDate month) {
        this.month = month;
    }

    public void setAllowanceName(String allowanceName) {
        this.allowanceName = allowanceName;
    }

    public void setAllowanceDescription(String allowanceDescription) {
        this.allowanceDescription = allowanceDescription;
    }

    public void setAllowanceRate(BigDecimal allowanceRate) {
        this.allowanceRate = allowanceRate;
    }

    public void setAllowanceType(String allowanceType) {
        this.allowanceType = allowanceType;
    }

    public void setAllowanceAmount(BigDecimal allowanceAmount) {
        this.allowanceAmount = allowanceAmount;
    }

    public void setDateCreated(LocalDateTime dateCreated) {
        this.dateCreated = dateCreated;
    }

    public void setDateModified(LocalDateTime dateModified) {
        this.dateModified = dateModified;
    }
}
