package com.demo.jalrakhsa.entity;


import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "Recommendations")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Recommendation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "assessment_id")
    private Assessment assessment;

    private String structureType;

    private String recommendedDimensions;

    private Double estimatedCapacity;

    private String reason;

    private Double suitabilityScore;
}
