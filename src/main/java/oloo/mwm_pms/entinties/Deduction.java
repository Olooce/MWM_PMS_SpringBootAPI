package oloo.mwm_pms.entities;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class Deduction {
    private Long deductionId;
    private Long employeeId;
    private LocalDate month;
    private String deductionName;
    private String deductionDescription;
    private String deductionType;
    private BigDecimal deductionAmount;
    private LocalDateTime dateCreated;
    private LocalDateTime dateModified;

    // Getters
    public Long getDeductionId() {
        return deductionId;
    }

    public Long getEmployeeId() {
        return employeeId;
    }

    public LocalDate getMonth() {
        return month;
    }

    public String getDeductionName() {
        return deductionName;
    }

    public String getDeductionDescription() {
        return deductionDescription;
    }

    public String getDeductionType() {
        return deductionType;
    }

    public BigDecimal getDeductionAmount() {
        return deductionAmount;
    }

    public LocalDateTime getDateCreated() {
        return dateCreated;
    }

    public LocalDateTime getDateModified() {
        return dateModified;
    }

    // Setters
    public void setDeductionId(Long deductionId) {
        this.deductionId = deductionId;
    }

    public void setEmployeeId(Long employeeId) {
        this.employeeId = employeeId;
    }

    public void setMonth(LocalDate month) {
        this.month = month;
    }

    public void setDeductionName(String deductionName) {
        this.deductionName = deductionName;
    }

    public void setDeductionDescription(String deductionDescription) {
        this.deductionDescription = deductionDescription;
    }

    public void setDeductionType(String deductionType) {
        this.deductionType = deductionType;
    }

    public void setDeductionAmount(BigDecimal deductionAmount) {
        this.deductionAmount = deductionAmount;
    }

    public void setDateCreated(LocalDateTime dateCreated) {
        this.dateCreated = dateCreated;
    }

    public void setDateModified(LocalDateTime dateModified) {
        this.dateModified = dateModified;
    }
}
