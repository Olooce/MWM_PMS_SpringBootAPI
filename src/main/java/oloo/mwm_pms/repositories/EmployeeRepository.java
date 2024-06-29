package oloo.mwm_pms.repositories;

import oloo.mwm_pms.entinties.Employee;
import oloo.mwm_pms.entinties.EmployeeStatus;
import oloo.mwm_pms.entinties.EmploymentType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public class EmployeeRepository {

    private final JdbcTemplate jdbcTemplate;

    public EmployeeRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Employee> findAll(int page, int size) {
        int offset = page * size;
        return jdbcTemplate.query("SELECT * FROM employees LIMIT ? OFFSET ?",
                new Object[]{size, offset},
                new EmployeeRowMapper()
        );
    }

    public int count() {
        return jdbcTemplate.queryForObject("SELECT COUNT(*) FROM employees", Integer.class);
    }

    private static class EmployeeRowMapper implements RowMapper<Employee> {
        @Override
        public Employee mapRow(ResultSet rs, int rowNum) throws SQLException {
            Employee employee = new Employee();
            employee.setEmployeeId(rs.getLong("employee_id"));
            employee.setName(rs.getString("name"));
            employee.setDob(rs.getObject("dob", LocalDate.class));
            employee.setGender(rs.getString("gender"));
            employee.setDepartmentId(rs.getLong("department_id"));
            employee.setEmploymentType(EmploymentType.valueOf(rs.getString("employment_type")));
            employee.setEmploymentDate(rs.getObject("employment_date", LocalDate.class));
            employee.setStatus(EmployeeStatus.valueOf(rs.getString("status")));
            employee.setStatusDescription(rs.getString("status_description"));
            employee.setTerminationDate(rs.getObject("termination_date", LocalDate.class));
            employee.setDateCreated(rs.getTimestamp("date_created").toLocalDateTime());
            employee.setDateModified(rs.getTimestamp("date_modified").toLocalDateTime());
            return employee;
        }
    }
}
