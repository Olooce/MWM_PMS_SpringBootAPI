package oloo.mwm_pms.services;

import oloo.mwm_pms.repositories.DataRepository;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@Service
public class ExportService {

    @Autowired
    private DataRepository dataRepository;

    public void exportDataToExcel(HttpServletResponse response) throws IOException {
        try (Workbook workbook = new XSSFWorkbook()) {
            List<String> headers = dataRepository.getTableHeaders("your_table");

            Sheet sheet = workbook.createSheet("Data");
            createHeaderRow(sheet, headers);

            dataRepository.getTableData("your_table", rs -> {
                int rowNum = sheet.getLastRowNum();
                while (rs.next()) {
                    Row row = sheet.createRow(++rowNum);
                    for (int i = 0; i < headers.size(); i++) {
                        Cell cell = row.createCell(i);
                        cell.setCellValue(rs.getString(headers.get(i)));
                    }
                }
            });

            workbook.write(response.getOutputStream());
            response.flushBuffer();
        }
    }

    private void createHeaderRow(Sheet sheet, List<String> headers) {
        Row headerRow = sheet.createRow(0);
        for (int i = 0; i < headers.size(); i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers.get(i));
        }
    }
}

