package oloo.mwm_pms.repositories;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Repository
public class DataRepository {

    private final JdbcTemplate jdbcTemplate;
    private final DataSource dataSource;

    @Autowired
    public DataRepository(JdbcTemplate jdbcTemplate, DataSource dataSource) {
        this.jdbcTemplate = jdbcTemplate;
        this.dataSource = dataSource;
    }

    public List<String> getTableHeaders(String tableName) throws SQLException {
        List<String> headers = new ArrayList<>();
        try (Connection connection = dataSource.getConnection()) {
            ResultSet rs = connection.getMetaData().getColumns(null, null, tableName, null);
            while (rs.next()) {
                headers.add(rs.getString("COLUMN_NAME"));
            }
        }
        return headers;
    }

    public void getTableData(String tableName, RowCallbackHandler callbackHandler) {
        jdbcTemplate.query("SELECT * FROM " + tableName, callbackHandler);
    }
}
