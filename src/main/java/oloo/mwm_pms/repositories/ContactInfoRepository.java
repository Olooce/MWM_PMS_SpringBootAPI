package oloo.mwm_pms.repositories;

import oloo.mwm_pms.entinties.ContactInfo;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class ContactInfoRepository {

    private final JdbcTemplate jdbcTemplate;

    public ContactInfoRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<ContactInfo> findAll(int page, int size) {
        int offset = page * size;
        return jdbcTemplate.query("SELECT * FROM contact_info LIMIT ? OFFSET ?",
                new Object[]{size, offset},
                new ContactInfoRowMapper()
        );
    }

    public int count() {
        return jdbcTemplate.queryForObject("SELECT COUNT(*) FROM contact_info", Integer.class);
    }

    private static class ContactInfoRowMapper implements RowMapper<ContactInfo> {
        @Override
        public ContactInfo mapRow(ResultSet rs, int rowNum) throws SQLException {
            ContactInfo contactInfo = new ContactInfo();
            contactInfo.setContactInfoId(rs.getLong("contact_info_id"));
            contactInfo.setEmployeeId(rs.getLong("employee_id"));
            contactInfo.setAddress(rs.getString("address"));
            contactInfo.setPhoneNo(rs.getString("phone_no"));
            contactInfo.setEmail(rs.getString("email"));
            contactInfo.setEmergencyContactNo(rs.getString("emergency_contact_no"));
            contactInfo.setEmergencyContactName(rs.getString("emergency_contact_name"));
            contactInfo.setDateCreated(rs.getTimestamp("date_created").toLocalDateTime());
            contactInfo.setDateModified(rs.getTimestamp("date_modified").toLocalDateTime());
            return contactInfo;
        }
    }
}
