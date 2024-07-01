package oloo.mwm_pms.repositories;

import oloo.mwm_pms.entinties.Allowance;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

@Repository
public class AllowanceRepository {

    private final JdbcTemplate jdbcTemplate;

    public AllowanceRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Allowance> findAll(int page, int size) {
        int offset = page * size;
        return jdbcTemplate.query("SELECT * FROM allowances LIMIT ? OFFSET ?",
                new Object[]{size, offset},
                new AllowanceRowMapper()
        );
    }

    public int count() {
        return jdbcTemplate.queryForObject("SELECT COUNT(*) FROM allowances", Integer.class);
    }

    private static class AllowanceRowMapper implements RowMapper<Allowance> {
        @Override
        public Allowance mapRow(ResultSet rs, int rowNum) throws SQLException {
            Allowance allowance = new Allowance();
            allowance.setAllowanceId(rs.getLong("allowance_id"));
            allowance.setEmployeeId(rs.getLong("employee_id"));
            allowance.setMonth(rs.getObject("month", LocalDate.class));
            allowance.setAllowanceName(rs.getString("allowance_name"));
            allowance.setAllowanceDescription(rs.getString("allowance_description"));
            allowance.setAllowanceRate(rs.getBigDecimal("allowance_rate"));
            allowance.setAllowanceType(rs.getString("allowance_type"));
            allowance.setAllowanceAmount(rs.getBigDecimal("allowance_amount"));
            allowance.setDateCreated(rs.getTimestamp("date_created").toLocalDateTime());
            allowance.setDateModified(rs.getTimestamp("date_modified").toLocalDateTime());
            return allowance;
        }
    }
}
