package com.demo.jalrakhsa.service;

import com.demo.jalrakhsa.entity.Assessment;
import com.demo.jalrakhsa.entity.LocationData;
import org.springframework.stereotype.Service;

@Service
public class RainwaterCalculationService {

    public void calculate(Assessment assessment, LocationData location) {
        double roofArea = requiredPositive(assessment.getRoofArea(), "Roof area");
        int dwellers = requiredPositive(assessment.getDwellers(), "Dwellers");
        double rainfall = requiredPositive(location.getAnnualRainfall(), "Annual rainfall in LocationData");

        double coefficient = getRunoffCoefficient(assessment.getRoofType());
        double harvest = roofArea * rainfall * coefficient;
        double demand = dwellers * 135.0 * 365.0;
        double saving = demand == 0 ? 0 : Math.min(100.0, (harvest / demand) * 100.0);
        double surplus = Math.max(0, harvest - demand);
        double deficit = Math.max(0, demand - harvest);

        // Recharge volume is the harvestable volume potentially directed to recharge.
        // The final suitability is decided separately by FeasibilityScoringService.
        double rechargeFactor = getRechargeFactor(location.getRechargePotential());
        double rechargeVolume = harvest * rechargeFactor;

        assessment.setRunoffCoefficient(round(coefficient));
        assessment.setAnnualHarvestPotential(round(harvest));
        assessment.setAnnualWaterDemand(round(demand));
        assessment.setWaterSavingPercentage(round(saving));
        assessment.setAnnualSurplus(round(surplus));
        assessment.setAnnualDeficit(round(deficit));
        assessment.setRechargeVolume(round(rechargeVolume));
    }

    public double getRunoffCoefficient(String roofType) {
        if (roofType == null) return 0.80;
        return switch (roofType.trim().toLowerCase()) {
            case "concrete", "rcc" -> 0.85;
            case "metal" -> 0.90;
            case "tiles", "tile" -> 0.75;
            case "asbestos" -> 0.80;
            default -> 0.80;
        };
    }

    private double getRechargeFactor(String potential) {
        if (potential == null) return 0.50;
        return switch (potential.trim().toLowerCase()) {
            case "high" -> 0.80;
            case "moderate", "medium" -> 0.60;
            case "low" -> 0.40;
            default -> 0.50;
        };
    }

    private double requiredPositive(Double value, String field) {
        if (value == null || value <= 0) throw new IllegalArgumentException(field + " must be greater than 0");
        return value;
    }

    private int requiredPositive(Integer value, String field) {
        if (value == null || value <= 0) throw new IllegalArgumentException(field + " must be greater than 0");
        return value;
    }

    private double round(double value) { return Math.round(value * 100.0) / 100.0; }
}
