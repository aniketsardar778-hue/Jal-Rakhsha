package com.demo.jalrakhsa.controller;

import com.demo.jalrakhsa.entity.Assessment;
import com.demo.jalrakhsa.entity.Recommendation;
import com.demo.jalrakhsa.repository.AssessmentRepo;
import com.demo.jalrakhsa.repository.RecommendationRepo;
import com.demo.jalrakhsa.service.recommendService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/recommendation")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class RecommendationController {

    private final AssessmentRepo assessmentRepository;

    private final RecommendationRepo recommendationRepository;

    private final recommendService recommendationService;


    // ==========================================
    // GENERATE RECOMMENDATION
    // ==========================================

    @PostMapping("/{id}/recommendation")
    public ResponseEntity<Recommendation>
    generateRecommendation(
            @PathVariable Long id) {

        Assessment assessment =
                assessmentRepository
                        .findById(id)
                        .orElseThrow(
                                () -> new RuntimeException(
                                        "Assessment not found"
                                )
                        );

        Recommendation recommendation =recommendationService.generateRecommandation(
                                assessment );

        return ResponseEntity.ok(
                recommendation
        );
    }


    // ==========================================
    // GET RECOMMENDATION
    // ==========================================

    @GetMapping("/{id}/recommendation")
    public ResponseEntity<Recommendation>
    getRecommendation(
            @PathVariable Long id) {

        Recommendation recommendation =
                recommendationRepository
                        .findByAssessmentId(id);

        if (recommendation == null) {

            throw new RuntimeException(
                    "Recommendation not found"
            );
        }

       return ResponseEntity.ok(recommendation);
    }
}