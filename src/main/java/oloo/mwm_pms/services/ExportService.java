package oloo.mwm_pms.services;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import oloo.mwm_pms.repositories.DataRepository;
import org.springframework.jdbc.core.RowCallbackHandler;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class ExportService {

    private static final Logger LOGGER = Logger.getLogger(ExportService.class.getName());
    private final DataRepository dataRepository;
    private final Path fileStorageLocation = Paths.get("exported_files").toAbsolutePath().normalize();
    private static final int CHUNK_SIZE = 100;  // Define the size of each chunk
    private static final int MAX_ROWS_PER_SHEET = 1048576;  // Excel row limit

    @Autowired
    public ExportService(DataRepository dataRepository) {
        this.dataRepository = dataRepository;
        File directory = new File(fileStorageLocation.toString());
        if (!directory.exists()) {
            directory.mkdirs();
        }
    }

    @Async
    public void exportTableToExcelAsync(String tableName, String fileId) {
        try (Workbook workbook = new SXSSFWorkbook()) {
            final int[] sheetIndex = {0};
            final Sheet[] sheet = {workbook.createSheet("Sheet " + (sheetIndex[0] + 1))};
            List<String> headers = dataRepository.getTableHeaders(tableName);
            Row headerRow = sheet[0].createRow(0);
            for (int i = 0; i < headers.size(); i++) {
                headerRow.createCell(i).setCellValue(headers.get(i));
            }

            int offset = 0;
            int rowIndex = 1;
            boolean moreData = true;

            while (moreData) {
                final int currentRowIndex = rowIndex;
                dataRepository.getTableData(tableName, offset, CHUNK_SIZE, new RowCallbackHandler() {
                    int currentRow = currentRowIndex;

                    @Override
                    public void processRow(ResultSet rs) throws SQLException {
                        if (currentRow > MAX_ROWS_PER_SHEET) {
                            sheetIndex[0]++;
                            sheet[0] = workbook.createSheet("Sheet " + (sheetIndex[0] + 1));
                            Row headerRow = sheet[0].createRow(0);
                            for (int i = 0; i < headers.size(); i++) {
                                headerRow.createCell(i).setCellValue(headers.get(i));
                            }
                            currentRow = 1;
                        }
                        Row excelRow = sheet[0].createRow(currentRow++);
                        for (int i = 0; i < headers.size(); i++) {
                            excelRow.createCell(i).setCellValue(rs.getString(i + 1));
                        }
                    }
                });

                rowIndex += CHUNK_SIZE;
                offset += CHUNK_SIZE;
                moreData = rowIndex > currentRowIndex;  // Check if more data was processed
            }

            File file = new File(fileStorageLocation.resolve(fileId + ".xlsx").toString());
            try (FileOutputStream fos = new FileOutputStream(file)) {
                workbook.write(fos);
            }
        } catch (IOException | SQLException e) {
            LOGGER.log(Level.SEVERE, "Error writing Excel file", e);
        }
    }

    public Resource loadFileAsResource(String fileId) throws Exception {
        Path filePath = fileStorageLocation.resolve(fileId + ".xlsx").normalize();
        Resource resource = new UrlResource(filePath.toUri());
        if (resource.exists()) {
            return resource;
        } else {
            throw new Exception("File not found " + fileId);
        }
    }
}
