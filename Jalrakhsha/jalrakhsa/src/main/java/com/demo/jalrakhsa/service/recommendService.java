package com.demo.jalrakhsa.service;

import com.demo.jalrakhsa.entity.Assessment;
import com.demo.jalrakhsa.entity.Recommendation;
import com.demo.jalrakhsa.repository.RecommendationRepo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class recommendService {
    private final RecommendationRepo recommendationRepo;

    public recommendService(RecommendationRepo recommendationRepo) { this.recommendationRepo = recommendationRepo; }

    @Transactional
    public Recommendation generateRecommandation(Assessment assessment) {
        double rechargeScore = assessment.getRechargeScore();
        double openSpace = assessment.getOpenSpace() == null ? 0 : assessment.getOpenSpace();

        String type;
        String dimensions;
        double capacity;
        String reason;

        if (rechargeScore >= 80 && openSpace >= 20) {
            type = "RECHARGE PIT";
            dimensions = openSpace >= 40 ? "3m × 3m × 2m" : "2m × 2m × 2m";
            capacity = openSpace >= 40 ? 18000 : 8000;
            reason = "Recharge pit is recommended because recharge suitability is high and sufficient open space is available.";
        } else if (rechargeScore >= 60 && openSpace >= 15) {
            type = "RECHARGE TRENCH";
            dimensions = openSpace >= 30 ? "10m × 1m × 1.5m" : "6m × 1m × 1m";
            capacity = openSpace >= 30 ? 15000 : 6000;
            reason = "Recharge trench is recommended because recharge suitability is moderate/high and adequate open space is available.";
        } else if (rechargeScore >= 50 && openSpace < 15) {
            type = "RECHARGE SHAFT";
            dimensions = "1.5m diameter × 6m depth";
            capacity = 10000;
            reason = "Recharge shaft is recommended because open space is limited while recharge conditions are moderately suitable.";
        } else {
            type = "RAINWATER STORAGE TANK";
            dimensions = "Based on required storage capacity";
            capacity = Math.max(0, assessment.getAnnualHarvestPotential() * 0.10);
            reason = "Rainwater storage is recommended because direct groundwater recharge feasibility is low.";
        }

        Recommendation old = recommendationRepo.findByAssessmentId(assessment.getId());
        Recommendation recommendation = old == null ? new Recommendation() : old;
        recommendation.setAssessment(assessment);
        recommendation.setStructureType(type);
        recommendation.setRecommendedDimensions(dimensions);
        recommendation.setEstimatedCapacity(round(capacity));
        recommendation.setSuitabilityScore(round(rechargeScore));
        recommendation.setReason(reason);
        return recommendationRepo.save(recommendation);
    }

    @Transactional(readOnly = true)
    public Recommendation getRecommendationByAssessment(Long assessmentId) {
        return recommendationRepo.findByAssessmentId(assessmentId);
    }

    private double round(double v) { return Math.round(v * 100.0) / 100.0; }
}
