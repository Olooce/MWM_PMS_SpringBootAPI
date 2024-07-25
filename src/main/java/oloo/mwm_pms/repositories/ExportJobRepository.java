package oloo.mwm_pms.repositories;

import oloo.mwm_pms.entinties.ExportJob;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;

@Repository
public class ExportJobRepository {
    public void save(ExportJob exportJob) {
    }
}
 class ExportJobRowMapper implements RowMapper<ExportJob> {
    @Override
    public ExportJob mapRow(ResultSet rs, int rowNum) throws SQLException {
        ExportJob exportJob = new ExportJob();

        exportJob.setFileId(rs.getString("file_id"));
        exportJob.setFileName(rs.getString("file_name"));
        exportJob.setTotalRows(rs.getLong("total_rows"));
        exportJob.setFileSize(rs.getLong("file_size"));
        exportJob.setErrorMessage(rs.getString("error_message"));
        exportJob.setStatus(rs.getString("status"));
        exportJob.setTimeInitiated(rs.getTimestamp("time_initiated").toLocalDateTime());
        exportJob.setDateCreated(rs.getTimestamp("date_created").toLocalDateTime());
        exportJob.setDateModified(rs.getTimestamp("date_modified").toLocalDateTime());
        return exportJob;
    }
}