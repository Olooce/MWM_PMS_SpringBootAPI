package oloo.mwm_pms.repositories;

import oloo.mwm_pms.entinties.BankDetails;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class BankDetailsRepository {

    private final JdbcTemplate jdbcTemplate;

    public BankDetailsRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<BankDetails> findAll(int page, int size) {
        int offset = page * size;
        return jdbcTemplate.query("SELECT * FROM bank_details LIMIT ? OFFSET ?",
                new Object[]{size, offset},
                new BankDetailsRowMapper()
        );
    }

    public int count() {
        return jdbcTemplate.queryForObject("SELECT COUNT(*) FROM bank_details", Integer.class);
    }


    private static class BankDetailsRowMapper implements RowMapper<BankDetails> {
        @Override
        public BankDetails mapRow(ResultSet rs, int rowNum) throws SQLException {
            BankDetails bankDetails = new BankDetails();
            bankDetails.setBankDetailId(rs.getLong("bank_detail_id"));
            bankDetails.setEmployeeId(rs.getLong("employee_id"));
            bankDetails.setAccountNo(rs.getString("account_no"));
            bankDetails.setBankName(rs.getString("bank_name"));
            bankDetails.setBranchCode(rs.getString("branch_code"));
            bankDetails.setDateCreated(rs.getTimestamp("date_created").toLocalDateTime());
            bankDetails.setDateModified(rs.getTimestamp("date_modified").toLocalDateTime());
            return bankDetails;
        }
    }
}
