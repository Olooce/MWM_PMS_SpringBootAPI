package oloo.mwm_pms.repositories;

import oloo.mwm_pms.entinties.Department;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class DepartmentRepository {

    private final JdbcTemplate jdbcTemplate;

    public DepartmentRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Department> findAll(int page, int size) {
        int offset = page * size;
        return jdbcTemplate.query("SELECT * FROM departments LIMIT ? OFFSET ?",
                new Object[]{size, offset},
                new DepartmentRowMapper()
        );
    }

    public int count() {
        return jdbcTemplate.queryForObject("SELECT COUNT(*) FROM departments", Integer.class);
    }

    private static class DepartmentRowMapper implements RowMapper<Department> {
        @Override
        public Department mapRow(ResultSet rs, int rowNum) throws SQLException {
            Department department = new Department();
            department.setDepartmentId(rs.getLong("department_id"));
            department.setBranchId(rs.getLong("branch_id"));
            department.setDepartmentName(rs.getString("department_name"));
            department.setDepartmentCode(rs.getString("department_code"));
            department.setDateCreated(rs.getTimestamp("date_created").toLocalDateTime());
            department.setDateModified(rs.getTimestamp("date_modified").toLocalDateTime());
            return department;
        }
    }
}
