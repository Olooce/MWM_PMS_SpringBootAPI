package oloo.mwm_pms.repositories;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Repository
public class DataRepository {

    final JdbcTemplate jdbcTemplate;
    final DataSource dataSource;

    @Autowired
    public DataRepository(JdbcTemplate jdbcTemplate,DataSource dataSource) {
        this.jdbcTemplate = jdbcTemplate;
       this.dataSource = dataSource;
    }

    public List<String> getTableHeaders(String tableName) throws SQLException {
        String schemaPattern = null;
        String catalogPattern = "mwm_pms_db";
        List<String> headers = new ArrayList<>();
        try (Connection connection = dataSource.getConnection()) {
            ResultSet rs = connection.getMetaData().getColumns(catalogPattern, schemaPattern, tableName, null);
            while (rs.next()) {
                headers.add(rs.getString("COLUMN_NAME"));
            }
        }

        return headers;
    }

//    public List<String> getTableHeaders(String tableName) throws SQLException {
//        List<String> headers = new ArrayList<>();
//
//        if (!isValidTableName(tableName)) {
//            throw new SQLException("Invalid table name.");
//        }
//
//        String query = String.format("SELECT * FROM %s LIMIT 1", tableName);
//        try (Connection conn = dataSource.getConnection();
//             Statement st = conn.createStatement();
//             ResultSet rset = st.executeQuery(query)) {
//
//            ResultSetMetaData md = rset.getMetaData();
//            for (int i = 1; i <= md.getColumnCount(); i++) {
//                headers.add(md.getColumnLabel(i));
//            }
//        }
////        System.out.println(headers);
//        return headers;
//    }

    private boolean isValidTableName(String tableName) {
        // Sanitize to avoid SQL Injection, check for null, empty, non-alphanumeric characters, etc.
        return tableName != null && tableName.matches("[a-zA-Z0-9_]+");
    }

    public String getPrimaryKey(String tableName) throws SQLException {
        if (!isValidTableName(tableName)) {
            throw new SQLException("Invalid table name.");
        }

        try (Connection conn = dataSource.getConnection()) {
            DatabaseMetaData metaData = conn.getMetaData();
            try (ResultSet pkResultSet = metaData.getPrimaryKeys(null, null, tableName)) {
                if (pkResultSet.next()) {
                    return pkResultSet.getString("COLUMN_NAME");
                }
            }
        }
        return null;
    }

    public void getTableData(String tableName,String primaryKey, long offset, long limit, RowCallbackHandler callbackHandler) {
        String query = String.format("SELECT * FROM %s ORDER BY %s LIMIT %d OFFSET %d", tableName,primaryKey,limit, offset);
        jdbcTemplate.query(query, callbackHandler);
    }



    public void searchTable(String tableName,String primaryKey, List<String> headers, Object searchTerm, long offset, long limit, RowCallbackHandler callbackHandler) {
        String searchPattern = "%" + searchTerm + "%";

        // Check column existence and dynamically build the SQL query
        String columnsClause = headers.stream()
                .map(column -> String.format("%s LIKE ?", column))
                .collect(Collectors.joining(" OR "));

        String sql = String.format(
                "SELECT * FROM %s WHERE %s ORDER BY %s LIMIT ? OFFSET ?",
                tableName,
                columnsClause,
                primaryKey
        );

        // Parameters for the query
        Object[] params = IntStream.range(0, headers.size())
                .mapToObj(i -> searchPattern)
                .toArray();

        params = append(params, limit, offset);

        try {
            jdbcTemplate.query(sql, params, callbackHandler);
        } catch (BadSqlGrammarException e) {
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
