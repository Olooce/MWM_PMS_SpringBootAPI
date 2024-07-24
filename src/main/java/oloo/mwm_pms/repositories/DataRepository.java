package oloo.mwm_pms.repositories;

import oloo.mwm_pms.entinties.Employee;
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

    public void getTableData(String tableName, int offset, int limit, RowCallbackHandler callbackHandler) {
        String query = String.format("SELECT * FROM %s LIMIT %d OFFSET %d", tableName, limit, offset);
        jdbcTemplate.query(query, callbackHandler);
    }

    public void searchTable(String tableName, List<String> headers, String searchTerm, int page, int size, RowCallbackHandler callbackHandler) {
        int offset = (page - 1) * size;
        String searchPattern = "%" + searchTerm + "%";

        // Build the SQL query dynamically
        String columnsClause = headers.stream()
                .map(column -> column + " LIKE ?")
                .collect(Collectors.joining(" OR "));

        String sql = String.format(
                "SELECT * FROM %s WHERE %s LIMIT ? OFFSET ?",
                tableName,
                columnsClause
        );

        // Parameters for the query
        Object[] params = IntStream.range(0, headers.size())
                .mapToObj(i -> searchPattern)
                .toArray();

        // Add pagination parameters
        params = append(params, size, offset);

        // Execute the query
        jdbcTemplate.query(
                sql,
                params,
                callbackHandler
        );
    }

    private Object[] append(Object[] array, Object... elements) {
        Object[] result = new Object[array.length + elements.length];
        System.arraycopy(array, 0, result, 0, array.length);
        System.arraycopy(elements, 0, result, array.length, elements.length);
        return result;
    }
}
