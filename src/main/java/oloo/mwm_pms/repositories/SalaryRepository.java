package oloo.mwm_pms.repositories;

import oloo.mwm_pms.entinties.Salary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

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
