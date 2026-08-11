package com.demo.jalrakhsa.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "reports")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Report {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "assessment_id", unique = true)
    private Assessment assessment;

    @OneToOne
    @JoinColumn(name = "recommendation_id")
    private Recommendation recommendation;

    private String reportName;
    private String filePath;
    private String language;
    private LocalDateTime generatedAt;
}
