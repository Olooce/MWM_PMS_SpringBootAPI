package oloo.mwm_pms.entinties;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class Tax {
    private Long taxId;
    private Long employeeId;
    private LocalDate month;
    private BigDecimal grossSalary;
    private String taxName;
    private BigDecimal taxRate;
    private String taxType;
    private BigDecimal taxAmount;
    private LocalDateTime dateCreated;
    private LocalDateTime dateModified;

    // Getters
    public Long getTaxId() {
        return taxId;
    }

    public Long getEmployeeId() {
        return employeeId;
    }

    public LocalDate getMonth() {
        return month;
    }

    public BigDecimal getGrossSalary() {
        return grossSalary;
    }

    public String getTaxName() {
        return taxName;
    }

    public BigDecimal getTaxRate() {
        return taxRate;
    }

    public String getTaxType() {
        return taxType;
    }

    public BigDecimal getTaxAmount() {
        return taxAmount;
    }

    public LocalDateTime getDateCreated() {
        return dateCreated;
    }

    public LocalDateTime getDateModified() {
        return dateModified;
    }

    // Setters
    public void setTaxId(Long taxId) {
        this.taxId = taxId;
    }

    public void setEmployeeId(Long employeeId) {
        this.employeeId = employeeId;
    }

    public void setMonth(LocalDate month) {
        this.month = month;
    }

    public void setGrossSalary(BigDecimal grossSalary) {
        this.grossSalary = grossSalary;
    }

    public void setTaxName(String taxName) {
        this.taxName = taxName;
    }

    public void setTaxRate(BigDecimal taxRate) {
        this.taxRate = taxRate;
    }

    public void setTaxType(String taxType) {
        this.taxType = taxType;
    }

    public void setTaxAmount(BigDecimal taxAmount) {
        this.taxAmount = taxAmount;
    }

    public void setDateCreated(LocalDateTime dateCreated) {
        this.dateCreated = dateCreated;
    }

    public void setDateModified(LocalDateTime dateModified) {
        this.dateModified = dateModified;
    }
}
