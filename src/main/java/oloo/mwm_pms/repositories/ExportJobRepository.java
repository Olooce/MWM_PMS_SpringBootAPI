package oloo.mwm_pms.repositories;

import oloo.mwm_pms.entinties.ExportJob;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class ExportJobRepository {

    private final JdbcTemplate jdbcTemplate;

    public ExportJobRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // Save a new ExportJob record
    public void save(ExportJob exportJob) {
        String sql = "INSERT INTO exports (file_id, file_name, total_rows, file_size, error_message, status, time_initiated, time_completed, last_access_time, file_path) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql,
                exportJob.getFileId(),
                exportJob.getFileName(),
                exportJob.getTotalRows(),
                exportJob.getFileSize(),
                exportJob.getErrorMessage(),
                exportJob.getStatus(),
                exportJob.getTimeInitiated(),
                exportJob.getTimeCompleted(),
                exportJob.getLastAccessTime(),
                exportJob.getFilePath()

        );
    }

    // Update an existing ExportJob record
    public void update(ExportJob exportJob) {
        String sql = "UPDATE exports SET file_name = ?, total_rows = ?, file_size = ?, error_message = ?, status = ?, time_initiated = ?, time_completed = ?, last_access_time = ?, file_path = ?" +
                "WHERE file_id = ?";
        jdbcTemplate.update(sql,
                exportJob.getFileName(),
                exportJob.getTotalRows(),
                exportJob.getFileSize(),
                exportJob.getErrorMessage(),
                exportJob.getStatus(),
                exportJob.getTimeInitiated(),
                exportJob.getTimeCompleted(),
                exportJob.getLastAccessTime(),
                exportJob.getFilePath(),
                exportJob.getFileId()
        );
    }

    // Find all ExportJob records with pagination
    public List<ExportJob> findAll(int page, int size) {
        int offset = page * size;
        return jdbcTemplate.query("SELECT * FROM exports LIMIT ? OFFSET ?",
                new Object[]{size, offset},
                new ExportJobRowMapper()
        );
    }

    // RowMapper implementation to map rows to ExportJob objects
    private static class ExportJobRowMapper implements RowMapper<ExportJob> {
        @Override
        public ExportJob mapRow(ResultSet rs, int rowNum) throws SQLException {
            ExportJob exportJob = new ExportJob();

            exportJob.setExportId(rs.getLong("export_id"));
            exportJob.setFileId(rs.getString("file_id"));
            exportJob.setFileName(rs.getString("file_name"));
            exportJob.setTotalRows(rs.getLong("total_rows"));
            exportJob.setFileSize(rs.getLong("file_size")); // Assuming fileSize corresponds to size
            exportJob.setErrorMessage(rs.getString("error_message"));
            exportJob.setStatus(rs.getString("status"));
            exportJob.setTimeInitiated(rs.getTimestamp("time_initiated").toLocalDateTime());
            exportJob.setTimeCompleted(rs.getTimestamp("time_completed").toLocalDateTime());
            exportJob.setLastAccessTime(rs.getTimestamp("last_access_time").toLocalDateTime());
            exportJob.setFilePath(rs.getString("file_path"));
            exportJob.setDateCreated(rs.getTimestamp("date_created").toLocalDateTime());
            exportJob.setDateModified(rs.getTimestamp("date_modified").toLocalDateTime());
            return exportJob;
        }
    }
}
