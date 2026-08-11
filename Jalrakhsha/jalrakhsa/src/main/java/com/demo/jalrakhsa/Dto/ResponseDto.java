package com.demo.jalrakhsa.Dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResponseDto {
    private Long assessmentId;
    private String username;
    private LocalDateTime createdAt;

    // Selected location
    private Long locationId;
    private String locationName;
    private String district;
    private String state;
    private Double latitude;
    private Double longitude;

    // Environmental data fetched from LocationData - NOT user input
    private Double annualRainfall;
    private Double groundwaterDepth;
    private String soilType;
    private String aquiferType;
    private String rechargePotential;

    // User input
    private Double roofArea;
    private String roofType;
    private Integer dwellers;
    private Double openSpace;

    // Water calculations
    private Double runoffCoefficient;
    private Double annualHarvestPotential;
    private Double annualWaterDemand;
    private Double waterSavingPercentage;
    private Double annualSurplus;
    private Double annualDeficit;
    private Double rechargeVolume;

    // RTRWH feasibility components
    private Double rainfallScore;
    private Double roofAreaScore;
    private Double roofTypeScore;
    private Double demandCoverageScore;
    private Double collectionEfficiencyScore;
    private Double rtrwhScore;
    private String rtrwhStatus;

    // Recharge feasibility components
    private Double soilScore;
    private Double openSpaceScore;
    private Double aquiferSuitabilityScore;
    private Double groundwaterScore;
    private Double rechargePotentialScore;
    private Double rechargeScore;
    private String rechargeStatus;

    // Overall
    private Double overallScore;
    private String overallStatus;

    // Recommendation
    private String recommendedStructure;
    private String recommendedDimensions;
    private Double recommendedCapacity;
    private String recommendationReason;
    private Double recommendationSuitabilityScore;

    // Cost/economic feasibility
    private Double materialCost;
    private Double labourCost;
    private Double excavationCost;
    private Double maintenanceCost;
    private Double totalCost;
    private Double annualBenefit;
    private Double paybackPeriodYears;
    private Double roiPercentage;

    private String reportStatus;
}
