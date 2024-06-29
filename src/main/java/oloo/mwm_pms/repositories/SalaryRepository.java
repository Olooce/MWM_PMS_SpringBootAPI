package oloo.mwm_pms.repositories;

import oloo.mwm_pms.entinties.Salary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Repository
public class SalaryRepository {

    private final JdbcTemplate jdbcTemplate;

    public SalaryRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Salary> findAll(int page, int size) {
        int offset = page * size;
        return jdbcTemplate.query("SELECT * FROM salaries LIMIT ? OFFSET ?",
                new Object[]{size, offset},
                new SalaryRowMapper()
        );
    }

    public int count() {
        return jdbcTemplate.queryForObject("SELECT COUNT(*) FROM salaries", Integer.class);
    }

    public Map<String, Object> getEarningsAndDeductionsByEmployee(Long employeeId) {
        return jdbcTemplate.queryForMap(
                "SELECT SUM(total_gross_earnings) AS totalEarnings, SUM(total_deductions) AS totalDeductions, SUM(net_salary) AS netPay FROM salaries WHERE employee_id = ?",
                employeeId
        );
    }

    public List<Map<String, BigDecimal>> getTotalAllowancesAndNetSalariesByDepartment(Long departmentId) {
        return jdbcTemplate.query(
                "SELECT
                s.employee_id,
                SUM(s.total_allowances) AS totalAllowances,
                SUM(s.net_salary) AS netSalary
                FROM
                salaries s
                JOIN
                mwm_pms_db.departments d
                ON
                s.department_id = d.department_id
                WHERE
                d.department_id = ?
                GROUP BY
        s.employee_id;
,
                new Object[]{departmentId},
                (rs, rowNum) -> Map.of(
                        "employeeId", rs.getBigDecimal("employee_id"),
                        "totalAllowances", rs.getBigDecimal("total_allowances"),
                        "netSalary", rs.getBigDecimal("net_salary")
                )
        );
    }

    public List<Map<String, Object>> getPaymentHistoryByEmployee(Long employeeId) {
        return jdbcTemplate.query(
                "SELECT month, total_gross_earnings, total_taxes, net_salary FROM salaries WHERE employee_id = ? AND month < CURRENT_DATE ORDER BY month DESC",
                new Object[]{employeeId},
                (rs, rowNum) -> Map.of(
                        "month", rs.getObject("month", LocalDate.class),
                        "totalGrossEarnings", rs.getBigDecimal("total_gross_earnings"),
                        "totalTaxes", rs.getBigDecimal("total_taxes"),
                        "netSalary", rs.getBigDecimal("net_salary")
                )
        );
    }

    public BigDecimal getTotalNetSalaryToPay() {
        return jdbcTemplate.queryForObject(
                "SELECT SUM(net_salary) FROM salaries",
                BigDecimal.class
        );
    }

    private static class SalaryRowMapper implements RowMapper<Salary> {
        @Override
        public Salary mapRow(ResultSet rs, int rowNum) throws SQLException {
            Salary salary = new Salary();
            salary.setSalaryId(rs.getLong("salary_id"));
            salary.setEmployeeId(rs.getLong("employee_id"));
            salary.setMonth(rs.getObject("month", LocalDate.class));
            salary.setBasicSalary(rs.getBigDecimal("basic_salary"));
            salary.setTotalAllowances(rs.getBigDecimal("total_allowances"));
            salary.setTotalDeductions(rs.getBigDecimal("total_deductions"));
            salary.setTotalGrossEarnings(rs.getBigDecimal("total_gross_earnings"));
            salary.setTaxRelief(rs.getBigDecimal("tax_relief"));
            salary.setTaxReliefDescription(rs.getString("tax_relief_description"));
            salary.setTotalTaxes(rs.getBigDecimal("total_taxes"));
            salary.setNetSalary(rs.getBigDecimal("net_salary"));
            salary.setDateCreated(rs.getTimestamp("date_created").toLocalDateTime());
            salary.setDateModified(rs.getTimestamp("date_modified").toLocalDateTime());
            return salary;
        }
    }
}
