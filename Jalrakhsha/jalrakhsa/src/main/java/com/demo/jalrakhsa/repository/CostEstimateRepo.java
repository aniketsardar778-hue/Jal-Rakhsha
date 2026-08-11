package com.demo.jalrakhsa.repository;

import com.demo.jalrakhsa.entity.CostEstimate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CostEstimateRepo extends JpaRepository<CostEstimate, Long> {
    Optional<CostEstimate> findByAssessmentId(Long assessmentId);
}
