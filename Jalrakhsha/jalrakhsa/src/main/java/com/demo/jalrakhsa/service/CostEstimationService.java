package com.demo.jalrakhsa.service;

import com.demo.jalrakhsa.entity.Assessment;
import com.demo.jalrakhsa.entity.CostEstimate;
import com.demo.jalrakhsa.entity.Recommendation;
import com.demo.jalrakhsa.repository.CostEstimateRepo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CostEstimationService {
    private final CostEstimateRepo repo;

    public CostEstimationService(CostEstimateRepo repo) { this.repo = repo; }

    @Transactional
    public CostEstimate calculateAndSave(Assessment a, Recommendation r) {
        double capacity = r.getEstimatedCapacity() == null ? 0 : r.getEstimatedCapacity();
        double material = Math.max(5000, capacity * 0.45);
        double labour = Math.max(2500, capacity * 0.20);
        double excavation = r.getStructureType() != null && r.getStructureType().contains("STORAGE") ? 0 : Math.max(1500, capacity * 0.10);
        double maintenance = Math.max(500, material * 0.05);
        double total = material + labour + excavation + maintenance;
        double annualBenefit = (a.getAnnualHarvestPotential() == null ? 0 : a.getAnnualHarvestPotential()) * 0.05;
        double payback = annualBenefit <= 0 ? 0 : total / annualBenefit;
        double roi = total <= 0 ? 0 : (annualBenefit / total) * 100;

        CostEstimate ce = repo.findByAssessmentId(a.getId()).orElseGet(CostEstimate::new);
        ce.setAssessment(a);
        ce.setMaterialCost(round(material));
        ce.setLabourCost(round(labour));
        ce.setExcavationCost(round(excavation));
        ce.setMaintenanceCost(round(maintenance));
        ce.setTotalCost(round(total));
        ce.setAnnualBenefit(round(annualBenefit));
        ce.setPaybackPeriodYears(round(payback));
        ce.setRoiPercentage(round(roi));
        return repo.save(ce);
    }

    private double round(double v) { return Math.round(v * 100.0) / 100.0; }
}
