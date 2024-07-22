package oloo.mwm_pms.controllers;

import oloo.mwm_pms.entinties.Employee;
import oloo.mwm_pms.repositories.EmployeeRepository;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
public class ExportController {
    private final EmployeeRepository employeeRepository;

    public ExportController(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    @GetMapping("/api/export/table")
    public ResponseEntity<StreamingResponseBody> exportTableToExcel() throws IOException {
//        // Create a sample table data
//        List<String[]> tableData = new ArrayList<>();
//        tableData.add(new String[]{"ID", "Name", "Age"});
//        tableData.add(new String[]{"1", "John Doe", "25"});
//        tableData.add(new String[]{"2", "Jane Doe", "30"});
//        tableData.add(new String[]{"3", "Bob Smith", "35"});

        List<Employee> tableData = employeeRepository.findAll(1, 10000);
        // Create a new Excel workbook
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Table Data");

        // Write the table data to the Excel sheet
        int rowIndex = 0;
        for (Employee row : tableData) {
            Row excelRow = sheet.createRow(rowIndex++);
            int colIndex = 0;
            Cell cell = excelRow.createCell(colIndex++);
            cell.setCellValue(row.getEmployeeId());
            cell = excelRow.createCell(colIndex++);
            cell.setCellValue(row.getName());
            cell = excelRow.createCell(colIndex++);
            cell.setCellValue(String.valueOf(row.getGender()));
        }

        // Create a byte array output stream to write the Excel file to
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        workbook.write(outputStream);

        // Create a response entity with the Excel file
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(org.springframework.http.MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDispositionFormData("attachment", "table_data.xlsx");
        return new ResponseEntity<>(outputStream.toByteArray(), headers, HttpStatus.OK);
    }
}