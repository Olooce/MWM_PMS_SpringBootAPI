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
import org.springframework.web.socket.BinaryMessage;
import org.springframework.web.socket.WebSocketSession;

import java.util.stream.IntStream;

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
    public void exportTableToExcel(WebSocketSession session) throws IOException {
        List<Employee> tableData = employeeRepository.findAll(1, 1000);
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Table Data");

        IntStream.range(0, tableData.size())
                .forEach(rowIndex -> {
                    Row excelRow = sheet.createRow(rowIndex);
                    Employee row = tableData.get(rowIndex);
                    IntStream.range(0, 3)
                            .forEach(colIndex -> {
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
                            });
                });

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        workbook.write(outputStream);

        BinaryMessage binaryMessage = new BinaryMessage(outputStream.toByteArray());
        session.sendMessage(binaryMessage);
    }
}