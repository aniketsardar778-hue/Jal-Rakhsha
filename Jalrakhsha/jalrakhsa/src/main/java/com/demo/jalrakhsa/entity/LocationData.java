package com.demo.jalrakhsa.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Table(name = "Local_data")
public class LocationData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String state;
    private String district;
    private String locationName;

    private Double latitude;
    private Double longitude;

    private Double annualRainfall;
    private Double groundWaterDepth;
    private String aquiferType;
    private String soilType;
    private String rechargePotential;
}
