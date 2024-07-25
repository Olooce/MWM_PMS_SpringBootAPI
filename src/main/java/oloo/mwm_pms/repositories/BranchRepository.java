package oloo.mwm_pms.repositories;

import oloo.mwm_pms.entinties.Branch;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class BranchRepository {

    private final JdbcTemplate jdbcTemplate;

    public BranchRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Branch> findAll(int page, int size) {
        int offset = page * size;
        return jdbcTemplate.query("SELECT * FROM branches LIMIT ? OFFSET ?",
                new Object[]{size, offset},
                new BranchRowMapper()
        );
    }


    public int count() {
        return jdbcTemplate.queryForObject("SELECT COUNT(*) FROM branches", Integer.class);
    }

    private static class BranchRowMapper implements RowMapper<Branch> {
        @Override
        public Branch mapRow(ResultSet rs, int rowNum) throws SQLException {
            Branch branch = new Branch();
            branch.setBranchId(rs.getLong("branch_id"));
            branch.setBranchName(rs.getString("branch_name"));
            branch.setBranchCode(rs.getString("branch_code"));
            branch.setNoEmployees(rs.getLong("no_employees"));
            branch.setDescription(rs.getString("description"));
            branch.setDateCreated(rs.getTimestamp("date_created").toLocalDateTime());
            branch.setDateModified(rs.getTimestamp("date_modified").toLocalDateTime());
            return branch;
        }
    }
}
