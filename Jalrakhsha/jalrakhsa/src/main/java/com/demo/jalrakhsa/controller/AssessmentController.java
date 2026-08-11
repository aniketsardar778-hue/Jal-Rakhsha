package com.demo.jalrakhsa.controller;

import com.demo.jalrakhsa.Dto.RequestDto;
import com.demo.jalrakhsa.Dto.ResponseDto;
import com.demo.jalrakhsa.service.AssessmentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/assessments")
@CrossOrigin(origins = "*")
public class AssessmentController {
    private final AssessmentService assessmentService;

    public AssessmentController(AssessmentService assessmentService) { this.assessmentService = assessmentService; }

    @PostMapping("/generate")
    public ResponseEntity<ResponseDto> generateAssessment(@RequestBody RequestDto request) {
        return ResponseEntity.ok(assessmentService.generateCompleteAssessment(request));
    }

    @GetMapping("/{assessmentId}")
    public ResponseEntity<ResponseDto> getAssessment(@PathVariable Long assessmentId) {
        return ResponseEntity.ok(assessmentService.getAssessmentReport(assessmentId));
    }
}
