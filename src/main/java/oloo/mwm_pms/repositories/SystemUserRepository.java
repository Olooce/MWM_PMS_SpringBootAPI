package oloo.mwm_pms.repositories;

import oloo.mwm_pms.entinties.SystemUser;
import oloo.mwm_pms.entinties.UserStatus;
import oloo.mwm_pms.entinties.PasswordStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;


import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;


@Repository
public class SystemUserRepository {

    private final JdbcTemplate jdbcTemplate;

    public SystemUserRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<SystemUser> findAll(int page, int size) {
        int offset = page * size;
        return jdbcTemplate.query("SELECT * FROM system_users ORDER BY user_id LIMIT ? OFFSET ?",
                new Object[]{size, offset},
                new SystemUserRowMapper()
        );
    }

    public int count() {
        return jdbcTemplate.queryForObject("SELECT COUNT(*) FROM system_users", Integer.class);
    }

    public SystemUser findByUsername(String username) {
        return jdbcTemplate.queryForObject("SELECT * FROM  system_users WHERE username = ? LIMIT 1",new Object[]{username}, new SystemUserRowMapper());
    }

    private static class SystemUserRowMapper implements RowMapper<SystemUser> {
        @Override
        public SystemUser mapRow(ResultSet rs, int rowNum) throws SQLException {
            SystemUser user = new SystemUser();
            user.setUserId(rs.getLong("user_id"));
            user.setEmployeeId(rs.getLong("employee_id"));
            user.setUsername(rs.getString("username"));
            user.setUserStatus(UserStatus.valueOf(rs.getString("user_status")));
            user.setUserStatusDescription(rs.getString("user_status_description"));
            user.setPwd(rs.getString("pwd"));
            user.setPwdStatus(PasswordStatus.valueOf(rs.getString("pwd_status")));
            user.setPwdStatusDescription(rs.getString("pwd_status_description"));
            user.setPwdResetCode(rs.getString("pwd_reset_code"));
            user.setPwdLastSet(rs.getTimestamp("pwd_last_set").toLocalDateTime());
            user.setRole(rs.getString("role"));
            user.setDateCreated(rs.getTimestamp("date_created").toLocalDateTime());
            user.setDateModified(rs.getTimestamp("date_modified").toLocalDateTime());
            return user;
        }
    }
}
