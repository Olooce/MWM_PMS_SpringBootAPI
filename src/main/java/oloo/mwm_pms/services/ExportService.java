package oloo.mwm_pms.services;

import oloo.mwm_pms.entinties.ExportJob;
import oloo.mwm_pms.repositories.ExportJobRepository;
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
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class ExportService {
    ExportJobRepository exportJobRepository;

    private static final Logger LOGGER = Logger.getLogger(ExportService.class.getName());
    private final DataRepository dataRepository;
    private final Path fileStorageLocation = Paths.get("exported_files").toAbsolutePath().normalize();
    private static final int CHUNK_SIZE = 25000;
    private static final int MAX_ROWS_PER_SHEET = 1048574;
    private static final String TEMPDIR = "/home/oloo/IdeaProjects/mwm_pms/temp";

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
        System.setProperty("java.io.tmpdir", TEMPDIR);

        long startTime = System.currentTimeMillis();
        final long[] currentTime = {startTime};
        final long[] elapsedTime = new long[1];

//        ExportJob exportJob = new ExportJob();
//        exportJob.setFileId(fileId);
//        exportJob.setFilePath(fileStorageLocation.resolve(fileId + ".xlsx").toString());
//        exportJob.setFileName(fileId + ".xlsx");
//        exportJob.setStatus("IN_PROGRESS");
//        exportJob.setTimeInitiated((new Date()).toLocalDateTime());
//        exportJobRepository.save(exportJob);

        File file = new File(fileStorageLocation.resolve(fileId + ".xlsx").toString());

        final long[] totalRowsCreated = {0};

        try (Workbook workbook = new SXSSFWorkbook(1500)) {
            final int[] sheetIndex = {0};
            final Sheet[] sheet = {workbook.createSheet("Sheet " + (sheetIndex[0] + 1))};
            List<String> headers = dataRepository.getTableHeaders(tableName);
            String primaryKey = dataRepository.getPrimaryKey(tableName);
//            System.out.println(headers);

            createHeaderRow(sheet[0], headers);
            System.out.println("Created header row for Sheet " + (sheetIndex[0] + 1));

            int offset = 0;
            boolean moreData = true;

            while (moreData) {
                final boolean[] dataAvailable = {false};

                dataRepository.getTableData(tableName,primaryKey, offset, CHUNK_SIZE, new RowCallbackHandler() {
                    final Map<String, Integer> columnNameIndexMap = new HashMap<>();
                    int rowCounter = sheet[0].getLastRowNum() + 1;

                    @Override
                    public void processRow(ResultSet rs) throws SQLException {
                        if (rowCounter >= MAX_ROWS_PER_SHEET) {
                            System.out.println("Sheet row limit reached. Creating new sheet.");
                            sheetIndex[0]++;
                            sheet[0] = workbook.createSheet("Sheet " + (sheetIndex[0] + 1));
                            createHeaderRow(sheet[0], headers);
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
                        dataAvailable[0] = true; // Indicate that data was processed
                        if (totalRowsCreated[0] % 100000 == 0) {  // Print every 1000 rows
                            currentTime[0] = System.currentTimeMillis();
                            elapsedTime[0] = (currentTime[0] - startTime)/1000;
                            System.out.println("Elapsed Time: " + elapsedTime[0] /3600+ "H " + (elapsedTime[0] % 3600) /60 + "M " + elapsedTime[0] % 60 + "S");
                            System.out.println("Total rows created: " + totalRowsCreated[0]);
                        }
                    }
                });

                offset += CHUNK_SIZE;
                moreData = dataAvailable[0];
            }

            try (FileOutputStream fos = new FileOutputStream(file)) {
                workbook.write(fos);
                fos.close();
                workbook.close();
            }
//            exportJob.setTotalRows(totalRowsCreated[0]);
//            exportJob.setFileSize(file.length());
//            exportJob.setStatus("COMPLETED");
//            exportJob.setTimeCompleted(new LocalDateTime());
//            exportJobRepository.save(exportJob);

            System.out.println("Export completed. Total rows created: " + totalRowsCreated[0]);
            currentTime[0] = System.currentTimeMillis();
            elapsedTime[0] = (currentTime[0] - startTime)/1000;
            System.out.println("Elapsed Time: " + elapsedTime[0] /3600+ "H " + (elapsedTime[0] % 3600) /60 + "M " + elapsedTime[0] % 60 + "S");
        } catch (IOException | SQLException e) {
            LOGGER.log(Level.SEVERE, "Error writing Excel file", e);
//            exportJob.setStatus("FAILED");
//            exportJob.setErrorMessage(e.getMessage());
//            exportJob.setTimeCompleted(new LocalDateTime());
//            exportJobRepository.save(exportJob);
        }

    }

    @Async
    public void exportSearchResultsToExcelAsync(String tableName, Object searchTerm, String fileId) {
        System.setProperty("java.io.tmpdir", TEMPDIR);

        long startTime = System.currentTimeMillis();
        final long[] currentTime = {startTime};
        final long[] elapsedTime = new long[1];

//        ExportJob exportJob = new ExportJob();
//        exportJob.setFileId(fileId);
//        exportJob.setFilePath(fileStorageLocation.resolve(fileId + ".xlsx").toString());
//        exportJob.setFileName(fileId + ".xlsx");
//        exportJob.setStatus("IN_PROGRESS");
//        exportJob.setTimeInitiated(new LocalDateTime());
//        exportJobRepository.save(exportJob);

        File file = new File(fileStorageLocation.resolve(fileId + ".xlsx").toString());

        final long[] totalRowsCreated = {0};

        try (Workbook workbook = new SXSSFWorkbook()) {
            final int[] sheetIndex = {0};
            final Sheet[] sheet = {workbook.createSheet("Sheet " + (sheetIndex[0] + 1))};
            List<String> headers = dataRepository.getTableHeaders(tableName);
            String primaryKey = dataRepository.getPrimaryKey(tableName);
            createHeaderRow(sheet[0], headers);

            System.out.println("Created header row for Sheet " + (sheetIndex[0] + 1));

            int offset = 0;
            boolean moreData = true;

            while (moreData) {
                final boolean[] dataAvailable = {false};

                dataRepository.searchTable(tableName,primaryKey,headers, searchTerm, offset / CHUNK_SIZE + 1, CHUNK_SIZE, new RowCallbackHandler() {
                    final Map<String, Integer> columnNameIndexMap = new HashMap<>();
                    int rowCounter = sheet[0].getLastRowNum() + 1;

                    @Override
                    public void processRow(ResultSet rs) throws SQLException {
                        if (rowCounter >= MAX_ROWS_PER_SHEET) {
                            System.out.println("Sheet row limit reached. Creating new sheet.");
                            sheetIndex[0]++;
                            sheet[0] = workbook.createSheet("Sheet " + (sheetIndex[0] + 1));
                            createHeaderRow(sheet[0], headers);
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
                            String columnName = headers.get(i);
                            Integer columnIndex = columnNameIndexMap.get(columnName);
                            if (columnIndex != null) {
                                excelRow.createCell(i).setCellValue(rs.getString(columnIndex));
                            } else {
                                excelRow.createCell(i).setCellValue("");
                            }
                        }

                        totalRowsCreated[0]++;
                        dataAvailable[0] = true; // Indicate that data was processed
                        if (totalRowsCreated[0] % 100000 == 0) {  // Print every 100,000 rows
                            currentTime[0] = System.currentTimeMillis();
                            elapsedTime[0] = (currentTime[0] - startTime) / 1000;
                            System.out.println("Elapsed Time: " + elapsedTime[0] / 3600 + "H " + (elapsedTime[0] % 3600) / 60 + "M " + elapsedTime[0] % 60 + "S");
                            System.out.println("Total rows created: " + totalRowsCreated[0]);
                        }
                    }
                });

                offset += CHUNK_SIZE;
                moreData = dataAvailable[0];
            }

            try (FileOutputStream fos = new FileOutputStream(file)) {
                workbook.write(fos);
                fos.close();
                workbook.close();
            }

//            exportJob.setTotalRows(totalRowsCreated[0]);
//            exportJob.setFileSize(file.length());
//            exportJob.setStatus("COMPLETED");
//            exportJob.setTimeCompleted(new LocalDateTime());
//            exportJobRepository.save(exportJob);

            System.out.println("Export completed. Total rows created: " + totalRowsCreated[0]);
            currentTime[0] = System.currentTimeMillis();
            elapsedTime[0] = (currentTime[0] - startTime) / 1000;
            System.out.println("Elapsed Time: " + elapsedTime[0] / 3600 + "H " + (elapsedTime[0] % 3600) / 60 + "M " + elapsedTime[0] % 60 + "S");
        } catch (IOException | SQLException e) {
            LOGGER.log(Level.SEVERE, "Error writing Excel file", e);
//            exportJob.setStatus("FAILED");
//            exportJob.setErrorMessage(e.getMessage());
//            exportJob.setTimeCompleted(new LocalDateTime());
//            exportJobRepository.save(exportJob);
        }
    }

    private void createHeaderRow(Sheet sheet, List<String> headers) {
        Row headerRow = sheet.createRow(0);
        for (int i = 0; i < headers.size(); i++) {
            headerRow.createCell(i).setCellValue(headers.get(i));
        }
    }    public Resource loadFileAsResource(String fileId) throws Exception {
        Path filePath = fileStorageLocation.resolve(fileId + ".xlsx").normalize();
        Resource resource = new UrlResource(filePath.toUri());
        if (resource.exists()) {
            return resource;
        } else {
            throw new Exception("File not found " + fileId);
        }
    }
}
