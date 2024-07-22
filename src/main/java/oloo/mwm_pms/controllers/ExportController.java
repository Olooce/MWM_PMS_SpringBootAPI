package oloo.mwm_pms.controllers;

import oloo.mwm_pms.entities.Employee;
import oloo.mwm_pms.repositories.EmployeeRepository;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.StreamingResponseBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

@RestController
public class ExportController {
    private final EmployeeRepository employeeRepository;

    public ExportController(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    @GetMapping("/api/export/table")
    public ResponseEntity<StreamingResponseBody> exportTableToExcel() {
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_TYPE, "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=table_data.xlsx")
                .body(outputStream -> {
                    try (Workbook workbook = new XSSFWorkbook()) {
                        Sheet sheet = workbook.createSheet("Table Data");
                        List<Employee> tableData = employeeRepository.findAll(1, 10000);

                        for (int rowIndex = 0; rowIndex < tableData.size(); rowIndex++) {
                            Row excelRow = sheet.createRow(rowIndex);
                            Employee row = tableData.get(rowIndex);

                            for (int colIndex = 0; colIndex < 3; colIndex++) {
                                Cell cell = excelRow.createCell(colIndex);
                                switch (colIndex) {
                                    case 0:
                                        cell.setCellValue(row.getEmployeeId());
                                        break;
                                    case 1:
                                        cell.setCellValue(row.getName());
                                        break;
                                    case 2:
                                        cell.setCellValue(String.valueOf(row.getGender()));
                                        break;
                                }
                            }
                        }
                        workbook.write(outputStream);
                    } catch (IOException e) {
                        throw new RuntimeException("Failed to write file", e);
                    }
                });
    }
}
