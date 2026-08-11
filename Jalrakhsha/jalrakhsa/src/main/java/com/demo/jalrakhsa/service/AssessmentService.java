package com.demo.jalrakhsa.service;

import com.demo.jalrakhsa.Dto.RequestDto;
import com.demo.jalrakhsa.Dto.ResponseDto;
import com.demo.jalrakhsa.entity.Assessment;
import com.demo.jalrakhsa.entity.CostEstimate;
import com.demo.jalrakhsa.entity.LocationData;
import com.demo.jalrakhsa.entity.Recommendation;
import com.demo.jalrakhsa.entity.User;
import com.demo.jalrakhsa.repository.AssessmentRepo;
import com.demo.jalrakhsa.repository.CostEstimateRepo;
import com.demo.jalrakhsa.repository.LocationDataRepo;
import com.demo.jalrakhsa.repository.RecommendationRepo;
import com.demo.jalrakhsa.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class AssessmentService {
    private final AssessmentRepo assessmentRepository;
    private final LocationDataRepo locationDataRepository;
    private final UserRepo userRepository;
    private final RecommendationRepo recommendationRepository;
    private final CostEstimateRepo costEstimateRepository;
    private final RainwaterCalculationService rainwaterCalculationService;
    private final FeasibilityScoringService feasibilityScoringService;
    private final recommendService recommendationService;
    private final CostEstimationService costEstimationService;

    Assessment assessment = new Assessment();


    public AssessmentService(AssessmentRepo assessmentRepository, LocationDataRepo locationDataRepository,
                             UserRepo userRepository, RecommendationRepo recommendationRepository,
                             CostEstimateRepo costEstimateRepository,
                             RainwaterCalculationService rainwaterCalculationService,
                             FeasibilityScoringService feasibilityScoringService,
                             recommendService recommendationService,
                             CostEstimationService costEstimationService) {
        this.assessmentRepository = assessmentRepository;
        this.locationDataRepository = locationDataRepository;
        this.userRepository = userRepository;
        this.recommendationRepository = recommendationRepository;
        this.costEstimateRepository = costEstimateRepository;
        this.rainwaterCalculationService = rainwaterCalculationService;
        this.feasibilityScoringService = feasibilityScoringService;
        this.recommendationService = recommendationService;
        this.costEstimationService = costEstimationService;
    }

    @Transactional
    public ResponseDto generateCompleteAssessment(RequestDto request) {
        validateRequest(request);

        User user = userRepository.findByUsernameIgnoreCase(request.getUserName())
                .orElseThrow(() -> new IllegalArgumentException("User not found with username: " + request.getUserName()));
        assessment.setUser(user);

        LocationData location = locationDataRepository.findFirstByLocationNameIgnoreCase(request.getLocationName())
                .orElseThrow(() -> new IllegalArgumentException(
                        "Selected location/environmental data not found: " + request.getLocationName()));

        Assessment a = Assessment.builder()
                .user(user)
                .locationData(location)
                .roofArea(request.getRoofArea())
                .roofType(request.getRoofType())
                .dwellers(request.getDwellers())
                .openSpace(request.getOpenSpace() == null ? 0.0 : request.getOpenSpace())
                .createdAt(LocalDateTime.now())
                .build();

        // Environmental values are fetched ONLY from LocationData.
        rainwaterCalculationService.calculate(a, location);
        feasibilityScoringService.calculate(a, location);

        Assessment saved = assessmentRepository.save(a);
        Recommendation recommendation = recommendationService.generateRecommandation(saved);
        CostEstimate cost = costEstimationService.calculateAndSave(saved, recommendation);

        return toResponse(saved, recommendation, cost, "GENERATED");
    }

    @Transactional(readOnly = true)
    public ResponseDto getAssessmentReport(Long assessmentId) {
        Assessment a = assessmentRepository.findById(assessmentId)
                .orElseThrow(() -> new IllegalArgumentException("Assessment not found for ID: " + assessmentId));
        Recommendation r = recommendationRepository.findByAssessmentId(assessmentId);
        CostEstimate c = costEstimateRepository.findByAssessmentId(assessmentId).orElse(null);
        return toResponse(a, r, c, "AVAILABLE");
    }

    @Transactional(readOnly = true)
    public List<ResponseDto> getUserAssessmentHistory(String username) {
        return assessmentRepository.findByUserUsernameOrderByCreatedAtDesc(username)
                .stream()
                .map(a -> toResponse(a, recommendationRepository.findByAssessmentId(a.getId()),
                        costEstimateRepository.findByAssessmentId(a.getId()).orElse(null), "AVAILABLE"))
                .toList();
    }

    private ResponseDto toResponse(Assessment a, Recommendation r, CostEstimate c, String reportStatus) {
        LocationData l = a.getLocationData();
        return ResponseDto.builder()
                .assessmentId(a.getId())
                .username(a.getUser().getUsername())
                .createdAt(a.getCreatedAt())
                .locationId(l.getId())
                .locationName(l.getLocationName())
                .district(l.getDistrict())
                .state(l.getState())
                .latitude(l.getLatitude())
                .longitude(l.getLongitude())
                .annualRainfall(l.getAnnualRainfall())
                .groundwaterDepth(l.getGroundWaterDepth())
                .soilType(l.getSoilType())
                .aquiferType(l.getAquiferType())
                .rechargePotential(l.getRechargePotential())
                .roofArea(a.getRoofArea())
                .roofType(a.getRoofType())
                .dwellers(a.getDwellers())
                .openSpace(a.getOpenSpace())
                .runoffCoefficient(a.getRunoffCoefficient())
                .annualHarvestPotential(a.getAnnualHarvestPotential())
                .annualWaterDemand(a.getAnnualWaterDemand())
                .waterSavingPercentage(a.getWaterSavingPercentage())
                .annualSurplus(a.getAnnualSurplus())
                .annualDeficit(a.getAnnualDeficit())
                .rechargeVolume(a.getRechargeVolume())
                .rainfallScore(a.getRainfallScore())
                .roofAreaScore(a.getRoofAreaScore())
                .roofTypeScore(a.getRoofTypeScore())
                .demandCoverageScore(a.getDemandCoverageScore())
                .collectionEfficiencyScore(a.getCollectionEfficiencyScore())
                .rtrwhScore(a.getRtrwhScore())
                .rtrwhStatus(a.getRtrwhStatus())
                .soilScore(a.getSoilScore())
                .openSpaceScore(a.getOpenSpaceScore())
                .aquiferSuitabilityScore(a.getAquiferSuitabilityScore())
                .groundwaterScore(a.getGroundwaterScore())
                .rechargePotentialScore(a.getRechargePotentialScore())
                .rechargeScore(a.getRechargeScore())
                .rechargeStatus(a.getRechargeStatus())
                .overallScore(a.getOverallScore())
                .overallStatus(a.getOverallStatus())
                .recommendedStructure(r == null ? null : r.getStructureType())
                .recommendedDimensions(r == null ? null : r.getRecommendedDimensions())
                .recommendedCapacity(r == null ? null : r.getEstimatedCapacity())
                .recommendationReason(r == null ? null : r.getReason())
                .recommendationSuitabilityScore(r == null ? null : r.getSuitabilityScore())
                .materialCost(c == null ? null : c.getMaterialCost())
                .labourCost(c == null ? null : c.getLabourCost())
                .excavationCost(c == null ? null : c.getExcavationCost())
                .maintenanceCost(c == null ? null : c.getMaintenanceCost())
                .totalCost(c == null ? null : c.getTotalCost())
                .annualBenefit(c == null ? null : c.getAnnualBenefit())
                .paybackPeriodYears(c == null ? null : c.getPaybackPeriodYears())
                .roiPercentage(c == null ? null : c.getRoiPercentage())
                .reportStatus(reportStatus)
                .build();
    }

    private void validateRequest(RequestDto r) {
        if (r == null) throw new IllegalArgumentException("Request body is required");
        if (r.getUserName() == null || r.getUserName().isBlank()) throw new IllegalArgumentException("Username is required");
        if (r.getLocationName() == null || r.getLocationName().isBlank()) throw new IllegalArgumentException("Location must be selected");
        if (r.getRoofArea() == null || r.getRoofArea() <= 0) throw new IllegalArgumentException("Roof area must be greater than 0");
        if (r.getRoofType() == null || r.getRoofType().isBlank()) throw new IllegalArgumentException("Roof type is required");
        if (r.getDwellers() == null || r.getDwellers() <= 0) throw new IllegalArgumentException("Number of people must be greater than 0");
    }
}
