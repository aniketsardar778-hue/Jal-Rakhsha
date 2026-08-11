package com.demo.jalrakhsa.service;

import com.demo.jalrakhsa.Dto.ResponseDto;
import com.demo.jalrakhsa.entity.Assessment;
import com.demo.jalrakhsa.entity.Recommendation;
import com.demo.jalrakhsa.entity.Report;
import com.demo.jalrakhsa.repository.AssessmentRepo;
import com.demo.jalrakhsa.repository.RecommendationRepo;
import com.demo.jalrakhsa.repository.ReportRepo;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class ReportService {
    private final AssessmentRepo assessmentRepository;
    private final RecommendationRepo recommendationRepository;
    private final ReportRepo reportRepository;
    private final AssessmentService assessmentService;

    public ReportService(AssessmentRepo assessmentRepository, RecommendationRepo recommendationRepository,
                         ReportRepo reportRepository, AssessmentService assessmentService) {
        this.assessmentRepository = assessmentRepository;
        this.recommendationRepository = recommendationRepository;
        this.reportRepository = reportRepository;
        this.assessmentService = assessmentService;
    }

    public Report generateReport(Long assessmentId, String language) {
        Assessment assessment = assessmentRepository.findById(assessmentId)
                .orElseThrow(() -> new IllegalArgumentException("Assessment not found"));
        Recommendation recommendation = recommendationRepository.findByAssessmentId(assessmentId);

        Report report = Report.builder()
                .assessment(assessment)
                .recommendation(recommendation)
                .reportName("Rainwater Assessment Report - " + assessmentId)
                .language(language == null || language.isBlank() ? "en" : language)
                .generatedAt(LocalDateTime.now())
                .build();
        return reportRepository.save(report);
    }

    public ResponseDto getCompleteReport(Long assessmentId) {
        // The response contains environmental data + every calculation + every feasibility component.
        return assessmentService.getAssessmentReport(assessmentId);
    }
}
