package oloo.mwm_pms.repositories;

import oloo.mwm_pms.entinties.ExportJob;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

@Repository
public class ExportJobRepository {

    private final JdbcTemplate jdbcTemplate;

    public ExportJobRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

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

    public List<ExportJob> findAll(int page, int size) {
        int offset = (page -1) * size;
        return jdbcTemplate.query("SELECT * FROM exports ORDER BY time_completed DESC LIMIT ? OFFSET ?",
                new Object[]{size, offset},
                new ExportJobRowMapper()
        );
    }


    private static class ExportJobRowMapper implements RowMapper<ExportJob> {
        @Override
        public ExportJob mapRow(ResultSet rs, int rowNum) throws SQLException {
            ExportJob exportJob = new ExportJob();

            exportJob.setExportId(rs.getLong("export_id"));
            exportJob.setFileId(rs.getString("file_id"));
            exportJob.setFileName(rs.getString("file_name"));
            exportJob.setTotalRows(rs.getLong("total_rows"));
            exportJob.setFileSize(rs.getLong("file_size"));
            exportJob.setErrorMessage(rs.getString("error_message"));
            exportJob.setStatus(rs.getString("status"));

            // Check for null timestamps before calling toLocalDateTime()
            Timestamp timeInitiated = rs.getTimestamp("time_initiated");
            if (timeInitiated != null) {
                exportJob.setTimeInitiated(timeInitiated.toLocalDateTime());
            }

            Timestamp timeCompleted = rs.getTimestamp("time_completed");
            if (timeCompleted != null) {
                exportJob.setTimeCompleted(timeCompleted.toLocalDateTime());
            }

            Timestamp lastAccessTime = rs.getTimestamp("last_access_time");
            if (lastAccessTime != null) {
                exportJob.setLastAccessTime(lastAccessTime.toLocalDateTime());
            }

            Timestamp dateCreated = rs.getTimestamp("date_created");
            if (dateCreated != null) {
                exportJob.setDateCreated(dateCreated.toLocalDateTime());
            }

            Timestamp dateModified = rs.getTimestamp("date_modified");
            if (dateModified != null) {
                exportJob.setDateModified(dateModified.toLocalDateTime());
            }

            exportJob.setFilePath(rs.getString("file_path"));
            return exportJob;
        }
    }

}
