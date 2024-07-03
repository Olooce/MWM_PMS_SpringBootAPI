package oloo.mwm_pms.repositories;

import oloo.mwm_pms.entinties.Tax;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

import java.util.List;

@Repository
public class TaxRepository {

    private final JdbcTemplate jdbcTemplate;

    public TaxRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Tax> findAll(int page, int size) {
        int offset = page * size;
        return jdbcTemplate.query("SELECT * FROM taxes LIMIT ? OFFSET ?",
                new Object[]{size, offset},
                new TaxRowMapper()
        );
    }

    public int count() {
        return jdbcTemplate.queryForObject("SELECT COUNT(*) FROM taxes", Integer.class);
    }

    private static class TaxRowMapper implements RowMapper<Tax> {
        @Override
        public Tax mapRow(ResultSet rs, int rowNum) throws SQLException {
            Tax tax = new Tax();
            tax.setTaxId(rs.getLong("tax_id"));
            tax.setEmployeeId(rs.getLong("employee_id"));
            tax.setMonth(rs.getObject("month", LocalDate.class));
            tax.setGrossSalary(rs.getBigDecimal("gross_salary"));
            tax.setTaxName(rs.getString("tax_name"));
            tax.setTaxRate(rs.getBigDecimal("tax_rate"));
            tax.setTaxType(rs.getString("tax_type"));
            tax.setTaxAmount(rs.getBigDecimal("tax_amount"));
            tax.setDateCreated(rs.getTimestamp("date_created").toLocalDateTime());
            tax.setDateModified(rs.getTimestamp("date_modified").toLocalDateTime());
            return tax;
        }
    }
}
