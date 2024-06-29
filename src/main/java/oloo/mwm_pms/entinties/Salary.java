package oloo.mwm_pms.entinties;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class Salary {
    private Long salaryId;
    private Long employeeId;
    private LocalDate month;
    private BigDecimal basicSalary;
    private BigDecimal totalAllowances;
    private BigDecimal totalDeductions;
    private BigDecimal totalGrossEarnings;
    private BigDecimal taxRelief;
    private String taxReliefDescription;
    private BigDecimal totalTaxes;
    private BigDecimal netSalary;
    private LocalDateTime dateCreated;
    private LocalDateTime dateModified;

    // Getters
    public Long getSalaryId() {
        return salaryId;
    }

    public Long getEmployeeId() {
        return employeeId;
    }

    public LocalDate getMonth() {
        return month;
    }

    public BigDecimal getBasicSalary() {
        return basicSalary;
    }

    public BigDecimal getTotalAllowances() {
        return totalAllowances;
    }

    public BigDecimal getTotalDeductions() {
        return totalDeductions;
    }

    public BigDecimal getTotalGrossEarnings() {
        return totalGrossEarnings;
    }

    public BigDecimal getTaxRelief() {
        return taxRelief;
    }

    public String getTaxReliefDescription() {
        return taxReliefDescription;
    }

    public BigDecimal getTotalTaxes() {
        return totalTaxes;
    }

    public BigDecimal getNetSalary() {
        return netSalary;
    }

    public LocalDateTime getDateCreated() {
        return dateCreated;
    }

    public LocalDateTime getDateModified() {
        return dateModified;
    }

    // Setters
    public void setSalaryId(Long salaryId) {
        this.salaryId = salaryId;
    }

    public void setEmployeeId(Long employeeId) {
        this.employeeId = employeeId;
    }

    public void setMonth(LocalDate month) {
        this.month = month;
    }

    public void setBasicSalary(BigDecimal basicSalary) {
        this.basicSalary = basicSalary;
    }

    public void setTotalAllowances(BigDecimal totalAllowances) {
        this.totalAllowances = totalAllowances;
    }

    public void setTotalDeductions(BigDecimal totalDeductions) {
        this.totalDeductions = totalDeductions;
    }

    public void setTotalGrossEarnings(BigDecimal totalGrossEarnings) {
        this.totalGrossEarnings = totalGrossEarnings;
    }

    public void setTaxRelief(BigDecimal taxRelief) {
        this.taxRelief = taxRelief;
    }

    public void setTaxReliefDescription(String taxReliefDescription) {
        this.taxReliefDescription = taxReliefDescription;
    }

    public void setTotalTaxes(BigDecimal totalTaxes) {
        this.totalTaxes = totalTaxes;
    }

    public void setNetSalary(BigDecimal netSalary) {
        this.netSalary = netSalary;
    }

    public void setDateCreated(LocalDateTime dateCreated) {
        this.dateCreated = dateCreated;
    }

    public void setDateModified(LocalDateTime dateModified) {
        this.dateModified = dateModified;
    }
}
