package oloo.mwm_pms.repositories;

import oloo.mwm_pms.entinties.Deduction;
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
public class DeductionRepository {

    private final JdbcTemplate jdbcTemplate;

    public DeductionRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Deduction> findAll(int page, int size) {
        int offset = page * size;
        return jdbcTemplate.query("SELECT * FROM deductions LIMIT ? OFFSET ?",
                new Object[]{size, offset},
                new DeductionRowMapper()
        );
    }

    public int count() {
        return jdbcTemplate.queryForObject("SELECT COUNT(*) FROM deductions", Integer.class);
    }

    private static class DeductionRowMapper implements RowMapper<Deduction> {
        @Override
        public Deduction mapRow(ResultSet rs, int rowNum) throws SQLException {
            Deduction deduction = new Deduction();
            deduction.setDeductionId(rs.getLong("deduction_id"));
            deduction.setEmployeeId(rs.getLong("employee_id"));
            deduction.setMonth(rs.getObject("month", LocalDate.class));
            deduction.setDeductionName(rs.getString("deduction_name"));
            deduction.setDeductionDescription(rs.getString("deduction_description"));
            deduction.setDeductionType(rs.getString("deduction_type"));
            deduction.setDeductionAmount(rs.getBigDecimal("deduction_amount"));
            deduction.setDateCreated(rs.getTimestamp("date_created").toLocalDateTime());
            deduction.setDateModified(rs.getTimestamp("date_modified").toLocalDateTime());
            return deduction;
        }
    }
}
