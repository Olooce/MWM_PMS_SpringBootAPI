package oloo.mwm_pms.repositories;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Repository
public class DataRepository {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public DataRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<String> getTableHeaders(String tableName) {
        List<String> headers = new ArrayList<>();
        String query = "SELECT COLUMN_NAME FROM INFORMATION_SCHEMA.COLUMNS " +
                "WHERE TABLE_SCHEMA = 'mwm_pms_db' AND TABLE_NAME = ?";

        // Execute the query and process the ResultSet
        jdbcTemplate.query(query, new Object[]{tableName}, rs -> {
            while (rs.next()) {
                headers.add(rs.getString("COLUMN_NAME"));
            }
        });

        System.out.println(headers);
        return headers;
    }
}


    public void getTableData(String tableName, int offset, int limit, RowCallbackHandler callbackHandler) {
        String query = String.format("SELECT * FROM %s LIMIT %d OFFSET %d", tableName, limit, offset);
        jdbcTemplate.query(query, callbackHandler);
    }

    public void searchTable(String tableName, List<String> headers, Object searchTerm, int page, int size, RowCallbackHandler callbackHandler) {
        System.out.println(headers);
        int offset = (page - 1) * size;
        String searchPattern = "%" + searchTerm + "%";

        // Check column existence and dynamically build the SQL query
        String columnsClause = headers.stream()
                .map(column -> String.format("%s LIKE ?", column))
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

        try {
            jdbcTemplate.query(sql, params, callbackHandler);
        } catch (BadSqlGrammarException e) {
            // Log or handle the exception
            System.err.println("SQL Error: " + e.getMessage());
        }
    }

    private Object[] append(Object[] array, Object... elements) {
        Object[] result = new Object[array.length + elements.length];
        System.arraycopy(array, 0, result, 0, array.length);
        System.arraycopy(elements, 0, result, array.length, elements.length);
        return result;
    }

}
