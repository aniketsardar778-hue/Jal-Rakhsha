package com.demo.jalrakhsa.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "assessments")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Assessment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "location_data_id", nullable = false)
    private LocationData locationData;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // ================= USER INPUTS =================
    @Column(nullable = false)
    private Double roofArea;

    @Column(nullable = false)
    private String roofType;

    @Column(nullable = false)
    private Integer dwellers;

    private Double openSpace;

    private LocalDateTime createdAt;

    // ================= WATER CALCULATIONS =================
    private Double runoffCoefficient;
    private Double annualHarvestPotential;
    private Double annualWaterDemand;
    private Double waterSavingPercentage;
    private Double annualSurplus;
    private Double annualDeficit;
    private Double rechargeVolume;

    // ================= RTRWH COMPONENTS =================
    private Double rainfallScore;
    private Double roofAreaScore;
    private Double roofTypeScore;
    private Double demandCoverageScore;
    private Double collectionEfficiencyScore;
    private Double rtrwhScore;
    private String rtrwhStatus;

    // ================= RECHARGE COMPONENTS =================
    private Double soilScore;
    private Double openSpaceScore;
    private Double aquiferSuitabilityScore;
    private Double groundwaterScore;
    private Double rechargePotentialScore;
    private Double rechargeScore;
    private String rechargeStatus;

    // ================= OVERALL =================
    private Double overallScore;
    private String overallStatus;
}
