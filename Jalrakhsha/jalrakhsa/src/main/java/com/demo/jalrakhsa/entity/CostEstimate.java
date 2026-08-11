package com.demo.jalrakhsa.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "cost_estimates")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CostEstimate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "assessment_id", nullable = false, unique = true)
    private Assessment assessment;

    private Double materialCost;
    private Double labourCost;
    private Double excavationCost;
    private Double maintenanceCost;
    private Double totalCost;
    private Double annualBenefit;
    private Double paybackPeriodYears;
    private Double roiPercentage;
}
