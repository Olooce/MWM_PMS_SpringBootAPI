package oloo.mwm_pms.services;

import oloo.mwm_pms.entinties.ExportJob;
import oloo.mwm_pms.repositories.ExportJobRepository;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import oloo.mwm_pms.repositories.DataRepository;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class ExportService {
    private static final Logger LOGGER = Logger.getLogger(ExportService.class.getName());
    private static final int CHUNK_SIZE = 25000;
    private static final int MAX_ROWS_PER_SHEET = 1048574;
    private static final String TEMPDIR = "/home/oloo/IdeaProjects/mwm_pms/temp";

    private final DataRepository dataRepository;
    private final ExportJobRepository exportJobRepository;
    private final Path fileStorageLocation;

    @Autowired
    public ExportService(DataRepository dataRepository, ExportJobRepository exportJobRepository) {
        this.dataRepository = dataRepository;
        this.exportJobRepository = exportJobRepository;
        this.fileStorageLocation = Paths.get("exported_files").toAbsolutePath();
        File directory = new File(fileStorageLocation.toString());
        if (!directory.exists()) {
            directory.mkdirs();
        }
    }

    @Async
    public void exportTableToExcelAsync(String tableName, String fileId) {
        setUpTempDir();
        ExportJob exportJob = initializeExportJob(fileId);
        try (Workbook workbook = new SXSSFWorkbook()) {
            processTableData(tableName, workbook, exportJob);
            writeWorkbookToFile(workbook, exportJob);
        } catch (IOException | SQLException e) {
            handleExportError(exportJob, e);
        } finally {
            cleanupTempFiles();
        }
    }

    @Async
    public void exportSearchResultsToExcelAsync(String tableName, Object searchTerm, String fileId) {
        setUpTempDir();
        ExportJob exportJob = initializeExportJob(fileId);
        try (Workbook workbook = new SXSSFWorkbook()) {
            processSearchResults(tableName, searchTerm, workbook, exportJob);
            writeWorkbookToFile(workbook, exportJob);
        } catch (IOException | SQLException e) {
            handleExportError(exportJob, e);
        } finally {
            cleanupTempFiles();
        }
    }

    private void setUpTempDir() {
        System.setProperty("java.io.tmpdir", TEMPDIR);
    }

    private ExportJob initializeExportJob(String fileId) {
        ExportJob exportJob = new ExportJob();
        exportJob.setFileId(fileId);
        exportJob.setFilePath(fileStorageLocation.resolve(fileId + ".xlsx").toString());
        exportJob.setFileName(fileId + ".xlsx");
        exportJob.setStatus("IN_PROGRESS");
        exportJob.setTimeInitiated(LocalDateTime.now());
        exportJobRepository.save(exportJob);
        return exportJob;
    }

    private void processTableData(String tableName, Workbook workbook, ExportJob exportJob) throws SQLException, IOException {
        processData(tableName, null, workbook, exportJob, (rs, headers, columnNameIndexMap, rowCounter, sheet, dataAvailable) -> {
            createExcelRow(rs, headers, columnNameIndexMap, rowCounter, sheet);
            dataAvailable[0] = false;
        });
    }

    private void processSearchResults(String tableName, Object searchTerm, Workbook workbook, ExportJob exportJob) throws SQLException, IOException {
        processData(tableName, searchTerm, workbook, exportJob, (rs, headers, columnNameIndexMap, rowCounter, sheet, dataAvailable) -> {
            createExcelRow(rs, headers, columnNameIndexMap, rowCounter, sheet);
            dataAvailable[0] = true;
        });
    }

    private void processData(String tableName, Object searchTerm, Workbook workbook, ExportJob exportJob, RowProcessor rowProcessor) throws SQLException, IOException {
        final long[] totalRowsCreated = {0};
        final long[] startTime = {System.currentTimeMillis()};
        final long[] currentTime = {startTime[0]};
        final long[] elapsedTime = new long[1];

        final int[] sheetIndex = {0};
        Sheet[] sheet = {createNewSheet(workbook, sheetIndex[0], tableName)};
        List<String> headers = dataRepository.getTableHeaders(tableName);
        String primaryKey = dataRepository.getPrimaryKey(tableName);
        createHeaderRow(sheet[0], headers);

        int offset = 0;
        boolean moreData = true;

        while (moreData) {
            final Map<String, Integer> columnNameIndexMap = new HashMap<>();
            final boolean[] dataAvailable = {false};
            final int initialRowCounter = sheet[0].getLastRowNum() + 1;
            final int[] rowCounter = {initialRowCounter};
            if(searchTerm != null){
                dataRepository.searchTable(tableName, primaryKey, headers, searchTerm, offset, CHUNK_SIZE, rs -> {
                    if (rs.next()) {
                        rowProcessor.processRow(rs, headers, columnNameIndexMap, rowCounter, sheet[0], dataAvailable);
                        if (rowCounter[0] >= MAX_ROWS_PER_SHEET) {
                            try {
                                disposeRows(sheet[0]);
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                            sheetIndex[0]++;
                            sheet[0] = createNewSheet(workbook, sheetIndex[0], tableName);
                            createHeaderRow(sheet[0], headers);
                            rowCounter[0] = 1;
                        }
                        totalRowsCreated[0]++;
                        if (totalRowsCreated[0] % 100000 == 0) {
                            logProgress(totalRowsCreated[0], startTime[0], currentTime, elapsedTime);
                        }
                        dataAvailable[0] = true;
                    } else {
                        dataAvailable[0] = false;
                    }
                });
            }else {
                dataRepository.getTableData(tableName, primaryKey, offset, CHUNK_SIZE, rs -> {
                    if (rs.next()) {
                        rowProcessor.processRow(rs, headers, columnNameIndexMap, rowCounter, sheet[0], dataAvailable);
                        if (rowCounter[0] >= MAX_ROWS_PER_SHEET) {
                            try {
                                disposeRows(sheet[0]);
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                            sheetIndex[0]++;
                            sheet[0] = createNewSheet(workbook, sheetIndex[0], tableName);
                            createHeaderRow(sheet[0], headers);
                            rowCounter[0] = 1;
                        }
                        totalRowsCreated[0]++;
                        if (totalRowsCreated[0] % 100000 == 0) {
                            logProgress(totalRowsCreated[0], startTime[0], currentTime, elapsedTime);
                        }
                        dataAvailable[0] = true;
                    } else {
                        dataAvailable[0] = false;
                    }
                });
            }

            offset += CHUNK_SIZE;
            moreData = dataAvailable[0] && rowCounter[0] > initialRowCounter;
        }

        finalizeExportJob(exportJob, totalRowsCreated[0]);
    }

    private void createExcelRow(ResultSet rs, List<String> headers, Map<String, Integer> columnNameIndexMap, int[] rowCounter, Sheet sheet) throws SQLException {
        if (columnNameIndexMap.isEmpty()) {
            for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {
                columnNameIndexMap.put(rs.getMetaData().getColumnName(i), i);
            }
        }

        Row excelRow = sheet.createRow(rowCounter[0]++);
        for (int i = 0; i < headers.size(); i++) {
            Integer columnIndex = columnNameIndexMap.get(headers.get(i));
            if (columnIndex != null) {
                excelRow.createCell(i).setCellValue(rs.getString(columnIndex));
            } else {
                excelRow.createCell(i).setCellValue("");
            }
        }
    }

    private Sheet createNewSheet(Workbook workbook, int sheetIndex, String tableName) {
        return workbook.createSheet("Sheet " + (sheetIndex + 1));
    }

    private void createHeaderRow(Sheet sheet, List<String> headers) {
        Row headerRow = sheet.createRow(0);
        for (int i = 0; headers != null && i < headers.size(); i++) {
            headerRow.createCell(i).setCellValue(headers.get(i));
        }
    }

    private void disposeRows(Sheet sheet) throws IOException {
        for (int rowNum = 1; rowNum <= sheet.getLastRowNum(); rowNum++) {
            Row row = sheet.getRow(rowNum);
            if (row != null) {
                sheet.removeRow(row);
            }
        }
        ((SXSSFSheet) sheet).flushRows();
    }

    private void writeWorkbookToFile(Workbook workbook, ExportJob exportJob) throws IOException {
        try (FileOutputStream fos = new FileOutputStream(exportJob.getFilePath())) {
            workbook.write(fos);
        }
    }

    private void finalizeExportJob(ExportJob exportJob, long totalRowsCreated) {
        File file = new File(exportJob.getFilePath());
        exportJob.setTotalRows(totalRowsCreated);
        exportJob.setFileSize(file.length());
        exportJob.setStatus("COMPLETED");
        exportJob.setTimeCompleted(LocalDateTime.now());
        exportJobRepository.save(exportJob);
    }

    private void logProgress(long totalRowsCreated, long startTime, long[] currentTime, long[] elapsedTime) {
        currentTime[0] = System.currentTimeMillis();
        elapsedTime[0] = (currentTime[0] - startTime) / 1000;
        LOGGER.info(String.format("Elapsed Time: %dH %dM %dS", elapsedTime[0] / 3600, (elapsedTime[0] % 3600) / 60, elapsedTime[0] % 60));
        LOGGER.info("Total rows created: " + totalRowsCreated);
    }

    private void handleExportError(ExportJob exportJob, Exception e) {
        LOGGER.log(Level.SEVERE, "Error during export", e);
        exportJob.setStatus("FAILED");
        exportJobRepository.save(exportJob);
    }

    private void cleanupTempFiles() {
        File tempDir = new File(TEMPDIR);
        if (tempDir.exists() && tempDir.isDirectory()) {
            File[] files = tempDir.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (!file.delete()) {
                        LOGGER.warning("Failed to delete temporary file: " + file.getPath());
                    }
                }
            }
        }
    }

    public Resource loadFileAsResource(String fileId) throws IOException {
        Path filePath = fileStorageLocation.resolve(fileId).normalize();
        Resource resource = new UrlResource(filePath.toUri());
        if (resource.exists() && resource.isReadable()) {
            return resource;
        } else {
            throw new FileNotFoundException("File not found " + fileId);
        }
    }

    private interface RowProcessor {
        void processRow(ResultSet rs, List<String> headers, Map<String, Integer> columnNameIndexMap, int[] rowCounter, Sheet sheet, boolean[] dataAvailable) throws SQLException;
    }
}
