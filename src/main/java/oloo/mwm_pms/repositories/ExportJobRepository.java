package oloo.mwm_pms.repositories;

import oloo.mwm_pms.entinties.Branch;
import oloo.mwm_pms.entinties.ExportJob;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;

@Repository
public class ExportJobRepository {
    public void save(ExportJob exportJob) {
    }

    private static class BranchRowMapper implements RowMapper<Branch> {
        @Override
        public ExportJob mapRow(ResultSet rs, int rowNum) throws SQLException {
            ExportJob exportJob = new ExportJob();

            exportJob.setDateCreated(rs.getTimestamp("date_created").toLocalDateTime());
            exportJob.setDateModified(rs.getTimestamp("date_modified").toLocalDateTime());
            return exportJob;
        }
    }
}
