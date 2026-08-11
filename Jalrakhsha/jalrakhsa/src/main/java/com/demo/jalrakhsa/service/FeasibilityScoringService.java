package com.demo.jalrakhsa.service;

import com.demo.jalrakhsa.entity.Assessment;
import com.demo.jalrakhsa.entity.LocationData;
import org.springframework.stereotype.Service;

@Service
public class FeasibilityScoringService {

    public void calculate(Assessment a, LocationData l) {
        double rainfall = rainfallScore(l.getAnnualRainfall());
        double roofArea = roofAreaScore(a.getRoofArea());
        double roofType = roofTypeScore(a.getRoofType());
        double demandCoverage = demandCoverageScore(a.getAnnualHarvestPotential(), a.getAnnualWaterDemand());
        double collection = collectionEfficiencyScore(a.getRoofType());

        double rtrwh = rainfall * .30 + roofArea * .25 + roofType * .15 + demandCoverage * .20 + collection * .10;

        double soil = soilScore(l.getSoilType());
        double open = openSpaceScore(a.getOpenSpace());
        double aquifer = aquiferScore(l.getAquiferType());
        double groundwater = groundwaterScore(l.getGroundWaterDepth());
        double rechargePotential = rechargePotentialScore(l.getRechargePotential());

        double recharge = soil * .25 + open * .20 + aquifer * .25 + groundwater * .15 + rechargePotential * .15;
        double overall = rtrwh * .50 + recharge * .50;

        a.setRainfallScore(round(rainfall));
        a.setRoofAreaScore(round(roofArea));
        a.setRoofTypeScore(round(roofType));
        a.setDemandCoverageScore(round(demandCoverage));
        a.setCollectionEfficiencyScore(round(collection));
        a.setRtrwhScore(round(rtrwh));
        a.setRtrwhStatus(status(rtrwh));

        a.setSoilScore(round(soil));
        a.setOpenSpaceScore(round(open));
        a.setAquiferSuitabilityScore(round(aquifer));
        a.setGroundwaterScore(round(groundwater));
        a.setRechargePotentialScore(round(rechargePotential));
        a.setRechargeScore(round(recharge));
        a.setRechargeStatus(status(recharge));

        a.setOverallScore(round(overall));
        a.setOverallStatus(status(overall));
    }

    public String status(double score) {
        if (score >= 80) return "HIGHLY_FEASIBLE";
        if (score >= 60) return "FEASIBLE";
        if (score >= 40) return "MODERATELY_FEASIBLE";
        return "LOW_FEASIBILITY";
    }

    public double rainfallScore(Double v) {
        if (v == null) return 0;
        if (v < 500) return 20;
        if (v < 750) return 40;
        if (v < 1000) return 60;
        if (v < 1500) return 80;
        return 100;
    }

    public double roofAreaScore(Double v) {
        if (v == null || v <= 0) return 0;
        if (v < 50) return 40;
        if (v < 100) return 60;
        if (v < 200) return 80;
        return 100;
    }

    public double roofTypeScore(String v) {
        if (v == null) return 0;
        return switch (v.trim().toLowerCase()) {
            case "metal" -> 90;
            case "concrete", "rcc" -> 85;
            case "tiles", "tile" -> 75;
            case "asbestos" -> 65;
            default -> 50;
        };
    }

    public double demandCoverageScore(Double harvest, Double demand) {
        if (harvest == null || demand == null || demand <= 0) return 0;
        return Math.min(100, (harvest / demand) * 100);
    }

    public double collectionEfficiencyScore(String roofType) {
        if (roofType == null) return 50;
        return switch (roofType.trim().toLowerCase()) {
            case "metal" -> 90;
            case "concrete", "rcc" -> 80;
            case "tiles", "tile" -> 75;
            case "asbestos" -> 70;
            default -> 50;
        };
    }

    public double soilScore(String soil) {
        if (soil == null) return 50;
        return switch (soil.trim().toLowerCase()) {
            case "sand" -> 100;
            case "sandy loam" -> 80;
            case "loam" -> 60;
            case "silty clay" -> 40;
            case "clay" -> 30;
            default -> 50;
        };
    }

    public double openSpaceScore(Double v) {
        if (v == null || v <= 0) return 0;
        if (v < 5) return 20;
        if (v < 10) return 40;
        if (v < 20) return 60;
        if (v < 40) return 80;
        return 100;
    }

    public double aquiferScore(String aquifer) {

        if (aquifer == null || aquifer.trim().isEmpty()) {
            return 50.0;
        }

        String s = aquifer.trim().toLowerCase();

        // Very favorable aquifer conditions
        if (s.contains("unconfined") && s.contains("alluvial")) {
            return 95.0;
        }

        if (s.contains("unconfined")) {
            return 85.0;
        }

        // Quaternary alluvial aquifers
        if (s.contains("quaternary") && s.contains("alluvial")) {
            return 90.0;
        }

        if (s.contains("quaternary")) {
            return 85.0;
        }

        // Tertiary aquifers
        if (s.contains("tertiary") && s.contains("alluvial")) {
            return 80.0;
        }

        if (s.contains("tertiary")) {
            return 75.0;
        }

        // General alluvial aquifer
        if (s.contains("alluvial")) {
            return 85.0;
        }

        // Mixed alluvial / hard-rock conditions
        if (s.contains("alluvial") && s.contains("hard rock")) {
            return 70.0;
        }

        if (s.contains("hard rock")) {
            return 55.0;
        }

        // Confined aquifers generally have slower direct recharge
        if (s.contains("confined")) {
            return 50.0;
        }

        if (s.contains("semi-confined") || s.contains("semiconfined")) {
            return 65.0;
        }

        // Explicit recharge descriptions
        if (s.contains("very high") && s.contains("recharge")) {
            return 95.0;
        }

        if (s.contains("high") && s.contains("recharge")) {
            return 85.0;
        }

        if (s.contains("moderate") && s.contains("recharge")) {
            return 70.0;
        }

        if (s.contains("low") && s.contains("recharge")) {
            return 40.0;
        }

        return 50.0;
    }

    public double groundwaterScore(Double depth) {
        if (depth == null || depth <= 0) return 50;
        if (depth < 3) return 30;
        if (depth < 5) return 50;
        if (depth < 10) return 80;
        if (depth < 20) return 90;
        return 70;
    }

    public double rechargePotentialScore(String value) {
        if (value == null) return 50;
        return switch (value.trim().toLowerCase()) {
            case "high" -> 100;
            case "moderate", "medium" -> 70;
            case "low" -> 40;
            default -> 50;
        };
    }

    private double round(double v) { return Math.round(v * 100.0) / 100.0; }
}
