package oloo.mwm_pms.repositories;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class DataRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<String> getTableHeaders(String tableName) {
        return jdbcTemplate.query(
                "SELECT COLUMN_NAME FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_NAME = ?",
                new Object[]{tableName.toUpperCase()},
                (rs, rowNum) -> rs.getString("COLUMN_NAME")
        );
    }

    public void getTableData(String tableName, RowCallbackHandler callbackHandler) {
        jdbcTemplate.query("SELECT * FROM " + tableName, callbackHandler);
    }
}

