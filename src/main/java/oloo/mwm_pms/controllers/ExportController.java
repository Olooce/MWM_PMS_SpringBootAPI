package oloo.mwm_pms.controllers;

import oloo.mwm_pms.entinties.Employee;
import oloo.mwm_pms.repositories.EmployeeRepository;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.concurrent.CompletableFuture;

@RestController
public class ExportController {

    private static final Logger LOGGER = Logger.getLogger(ExportController.class.getName());
    private final EmployeeRepository employeeRepository;

    public ExportController(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    @GetMapping("/api/export/table")
    @Async
    public CompletableFuture<ResponseEntity<StreamingResponseBody>> exportTableToExcel() {
        return CompletableFuture.supplyAsync(() -> {
            StreamingResponseBody responseBody = outputStream -> {
                try (Workbook workbook = new SXSSFWorkbook()) {  // Use SXSSFWorkbook for streaming
                    Sheet sheet = workbook.createSheet("Table Data");

                    // Write header row
                    Row headerRow = sheet.createRow(0);
                    headerRow.createCell(0).setCellValue("Employee ID");
                    headerRow.createCell(1).setCellValue("Name");
                    headerRow.createCell(2).setCellValue("Gender");

                    // Stream data in chunks
                    int rowIndex = 1;
                    int chunkSize = 10000;
                    boolean moreData = true;

                    while (moreData) {
                        List<Employee> chunk = employeeRepository.findAllInChunks(rowIndex - 1, chunkSize);
                        if (chunk.isEmpty()) {
                            moreData = false;
                        } else {
                            for (Employee employee : chunk) {
                                Row excelRow = sheet.createRow(rowIndex++);
                                excelRow.createCell(0).setCellValue(employee.getEmployeeId());
                                excelRow.createCell(1).setCellValue(employee.getName());
                                excelRow.createCell(2).setCellValue(String.valueOf(employee.getGender()));
                            }
                        }
                    }

                    // Write the workbook to the output stream
                    workbook.write(outputStream);
                } catch (IOException e) {
                    LOGGER.log(Level.SEVERE, "Error writing Excel file", e);
                    throw new RuntimeException("Error writing Excel file", e);
                }
            };

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(org.springframework.http.MediaType.APPLICATION_OCTET_STREAM);
            headers.setContentDispositionFormData("attachment", "table_data.xlsx");
            return new ResponseEntity<>(responseBody, headers, HttpStatus.OK);
        });
    }
}
