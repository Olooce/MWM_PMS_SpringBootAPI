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
import java.util.List;
import java.util.Optional;

@Repository
public class EmployeeRepository {

    private final JdbcTemplate jdbcTemplate;

    public EmployeeRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Employee> findAll(int page, int size) {
        int offset = (page-1) * size;
        return jdbcTemplate.query("SELECT * FROM employees ORDER BY employee_id LIMIT ? OFFSET ? ",
                new Object[]{size, offset},
                new EmployeeRowMapper()
        );
    }

    

//    public List<Employee> findAll(int page, int size) {
//        int offset = (page-1) * size;
//        return jdbcTemplate.query("SELECT e.employee_id, e.name, e.gender,e.dob, d.department_name, e.employment_type,e.status, e.status_description, e.date_created, e.date_modified FROM employees e JOIN departments d ON e.department_id = d.department_id ORDER BY employee_id LIMIT ? OFFSET ? ",
//                new Object[]{size, offset},
//                new EmployeeRowMapper()
//        );
//    }

    public List<Employee> searchEmployees(String searchTerm, int page, int size) {
        int offset = (page-1) * size;
        String sql = "SELECT * FROM employees WHERE " +
                "name LIKE ? OR " +
                "CAST(employee_id AS CHAR) LIKE ? OR " +
                "gender LIKE ? OR " +
                "CAST(department_id AS CHAR) LIKE ? OR " +
                "employment_type LIKE ? OR " +
                "status LIKE ? LIMIT ? OFFSET ?";

        String searchPattern = "%" + searchTerm + "%";

        return jdbcTemplate.query(sql,
                new Object[]{searchPattern, searchPattern, searchPattern, searchPattern, searchPattern, searchPattern, size, offset},
                new EmployeeRowMapper()
        );
    }

    public int count() {
        return jdbcTemplate.queryForObject("SELECT COUNT(*) FROM employees", Integer.class);
    }

    public List<Employee> findNewEmployeesGroupedByDepartment(LocalDate startDate, LocalDate endDate, int page, int size) {
//        return jdbcTemplate.query("SELECT Memployee_id), MAX(name), MAX(dob), MAX(gender),"
//                + "MAX(gender), MAX(department_id), MAX(employment_type), MAX(employment_date), MAX(status), " +
//                        "MAX(status_description) FROM employees WHERE date_created BETWEEN ? AND ? GROUP BY department_id",
//                new Object[]{startDate, endDate},
//                new EmployeeRowMapper()
//        );
        return jdbcTemplate.query( "CALL sp_new_employees_by_department()", new EmployeeRowMapper());
//                new Object[]{startDate, endDate},
//                new EmployeeRowMapper()
//        );
    }

    public Long countActiveEmployeesInDepartment(Long departmentId) {
        return jdbcTemplate.queryForObject("SELECT COUNT(*) FROM employees WHERE department_id = ? AND status = 'ACTIVE'",
                new Object[]{departmentId},
                Long.class
        );
    }

    public Long countNewEmployeesInDepartment(long departmentId) {
        return jdbcTemplate.queryForObject("SELECT COUNT(*) FROM employees WHERE department_id = ? AND status= 'NEW'",
                new Object[]{departmentId},
                Long.class
        );
    }

    public Employee addEmployee(Employee employee) {
        String sql = "INSERT INTO employees (name, dob, gender, department_id, employment_type, employment_date, status, status_description, termination_date, date_created, date_modified) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        jdbcTemplate.update(sql,
                employee.getName(),
                employee.getDob(),
                employee.getGender(),
                employee.getDepartmentId(),
                employee.getEmploymentType().name(),
                employee.getEmploymentDate(),
                employee.getStatus().name(),
                employee.getStatusDescription(),
                employee.getTerminationDate(),
                employee.getDateCreated(),
                employee.getDateModified()
        );

        Long generatedId = jdbcTemplate.queryForObject("SELECT LAST_INSERT_ID()", Long.class);
        employee.setEmployeeId(generatedId);

        return employee;
    }

    public Optional<Employee> findById(Long employeeId) {
        String sql = "SELECT * FROM employees WHERE employee_id = ?";
        List<Employee> employees = jdbcTemplate.query(sql, new Object[]{employeeId}, new EmployeeRowMapper());
        return employees.isEmpty() ? Optional.empty() : Optional.of(employees.get(0));
    }

    public Employee save(Employee employee) {
        String sql = "UPDATE employees SET name = ?, dob = ?, gender = ?, department_id = ?, employment_type = ?, employment_date = ?, status = ?, status_description = ?, termination_date = ?, date_modified = ? WHERE employee_id = ?";
        jdbcTemplate.update(sql,
                employee.getName(),
                employee.getDob(),
                employee.getGender(),
                employee.getDepartmentId(),
                employee.getEmploymentType().name(),
                employee.getEmploymentDate(),
                employee.getStatus().name(),
                employee.getStatusDescription(),
                employee.getTerminationDate(),
                employee.getDateModified(),
                employee.getEmployeeId()
        );
        return employee;
    }

    public void deleteById(Long employeeId) {
        String sql = "DELETE FROM employees WHERE employee_id = ?";
        jdbcTemplate.update(sql, employeeId);
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
