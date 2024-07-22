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
import java.util.Map;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.HashMap;

@Service
public class ExportService {

    private static final Logger LOGGER = Logger.getLogger(ExportService.class.getName());
    private final DataRepository dataRepository;
    private final Path fileStorageLocation = Paths.get("exported_files").toAbsolutePath().normalize();
    private static final int CHUNK_SIZE = 100;
    private static final int MAX_ROWS_PER_SHEET = 1048576;

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
        final long[] totalRowsCreated = {0};
        try (Workbook workbook = new SXSSFWorkbook()) {
            final int[] sheetIndex = {0};
            final Sheet[] sheet = {workbook.createSheet("Sheet " + (sheetIndex[0] + 1))};
            List<String> headers = dataRepository.getTableHeaders(tableName);
            Row headerRow = sheet[0].createRow(0);
            for (int i = 0; i < headers.size(); i++) {
                headerRow.createCell(i).setCellValue(headers.get(i));
            }

            System.out.println("Created header row for Sheet " + (sheetIndex[0] + 1));

            int offset = 0;
            int rowIndex = 1;
            boolean moreData = true;

            while (moreData) {
                final int currentRowIndex = rowIndex;
                dataRepository.getTableData(tableName, offset, CHUNK_SIZE, new RowCallbackHandler() {
                    Map<String, Integer> columnNameIndexMap = new HashMap<>();
                    int rowCounter = currentRowIndex;

                    @Override
                    public void processRow(ResultSet rs) throws SQLException {
                        if (rowCounter > MAX_ROWS_PER_SHEET) {
                            System.out.println("Sheet row limit reached. Creating new sheet.");
                            sheetIndex[0]++;
                            sheet[0] = workbook.createSheet("Sheet " + (sheetIndex[0] + 1));
                            Row newHeaderRow = sheet[0].createRow(0);
                            for (int i = 0; i < headers.size(); i++) {
                                newHeaderRow.createCell(i).setCellValue(headers.get(i));
                            }
                            rowCounter = 1;
                            System.out.println("Created header row for Sheet " + (sheetIndex[0] + 1));
                        }

                        if (columnNameIndexMap.isEmpty()) {
                            for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {
                                columnNameIndexMap.put(rs.getMetaData().getColumnName(i), i);
                            }
                        }

                        Row excelRow = sheet[0].createRow(rowCounter++);
                        for (int i = 0; i < headers.size(); i++) {
                            Integer columnIndex = columnNameIndexMap.get(headers.get(i));
                            if (columnIndex != null) {
                                excelRow.createCell(i).setCellValue(rs.getString(columnIndex));
                            } else {
                                excelRow.createCell(i).setCellValue("");
                            }
                        }

                        totalRowsCreated[0]++;
                        if (totalRowsCreated[0] % 1000 == 0) {  // Print every 1000 rows
                            System.out.println("Total rows created: " + totalRowsCreated[0]);
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
