package oloo.mwm_pms.controllers;

import oloo.mwm_pms.services.ExportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.CompletableFuture;

@RestController
public class ExportController {

    private final ExportService exportService;

    @Autowired
    public ExportController(ExportService exportService) {
        this.exportService = exportService;
    }

    @PostMapping("/api/export/{tableName}")
    public ResponseEntity<String> initiateExport(@PathVariable String tableName) {
        // Generate a unique fileId based on the table name and current date/time
        String fileId = tableName + "_" + new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());

        // Start the export process asynchronously
        CompletableFuture.runAsync(() -> exportService.exportTableToExcelAsync(tableName, fileId));

        return ResponseEntity.status(HttpStatus.ACCEPTED).body("Export job initiated. File ID: " + fileId);
    }

    @GetMapping("/api/download/{fileId}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String fileId) {
        try {
            Resource resource = exportService.loadFileAsResource(fileId);
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileId + ".xlsx")
                    .body(resource);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }
}
