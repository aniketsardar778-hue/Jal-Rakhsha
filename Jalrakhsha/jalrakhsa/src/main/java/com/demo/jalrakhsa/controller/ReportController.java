package com.demo.jalrakhsa.controller;

import com.demo.jalrakhsa.Dto.ResponseDto;
import com.demo.jalrakhsa.entity.Report;
import com.demo.jalrakhsa.service.ReportService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/report")
@CrossOrigin(origins = "*")
public class ReportController {
    private final ReportService reportService;
    public ReportController(ReportService reportService) { this.reportService = reportService; }

    @PostMapping("/{id}/report")
    public ResponseEntity<Report> generateReport(@PathVariable Long id,
                                                  @RequestParam(defaultValue = "en") String language) {
        return ResponseEntity.ok(reportService.generateReport(id, language));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseDto> getCompleteReport(@PathVariable Long id) {
        return ResponseEntity.ok(reportService.getCompleteReport(id));
    }
}
