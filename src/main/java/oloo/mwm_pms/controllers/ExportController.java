package oloo.mwm_pms.controllers;

import oloo.mwm_pms.services.ExportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import org.springframework.web.bind.annotation.*;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;


import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.Date;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;

@RestController
public class ExportController {

    private final ExportService exportService;

    @Autowired
    public ExportController(ExportService exportService) {
        this.exportService = exportService;
    }

    @GetMapping("/api/export/{tableName}")
    public SseEmitter initiateExport(@PathVariable String tableName) {
        SseEmitter emitter = new SseEmitter();
        String fileId = tableName + "_" + new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());

        CompletableFuture.runAsync(() -> {
            try {
                exportService.exportTableToExcelAsync(tableName, fileId, emitter);
            } catch (Exception e) {
                emitter.completeWithError(e);
            }
        });

        return emitter;
    }

    @PostMapping("/api/exportSearch/{tableName}")
    public SseEmitter initiateExportSearch(@PathVariable String tableName, @RequestParam Object searchTerm) {
        SseEmitter emitter = new SseEmitter();
        String fileId = tableName + "_" + new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());

        CompletableFuture.runAsync(() -> {
            try {
                exportService.exportSearchResultsToExcelAsync(tableName, searchTerm, fileId, emitter);
            } catch (Exception e) {
                emitter.completeWithError(e);
            }
        });

        return emitter;
    }

    @PostMapping("/api/download/{fileId}")
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

    @GetMapping("/sse-emitter")
    public SseEmitter sseEmitter() {
        SseEmitter emitter = new SseEmitter();
        Executors.newSingleThreadExecutor().execute(() -> {
            try {
                int count = 0;
                while (!Thread.currentThread().isInterrupted()) { // Better loop control
                    SseEmitter.SseEventBuilder event = SseEmitter.event()
                            .id(String.valueOf(count++))
                            .name("SSE_EMITTER_EVENT")
                            .data("SSE EMITTER - " + LocalTime.now().toString());
                    emitter.send(event);
                    Thread.sleep(1000);
                }
            } catch (InterruptedException ex) {
                Thread.currentThread().interrupt(); // Handle thread interruption
            } catch (Exception ex) {
                emitter.completeWithError(ex);
            } finally {
                emitter.complete(); // Ensure emitter is closed
            }
        });
        return emitter;
    }

}
