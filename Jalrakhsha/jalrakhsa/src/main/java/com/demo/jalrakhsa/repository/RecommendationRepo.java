package com.demo.jalrakhsa.repository;

import com.demo.jalrakhsa.entity.Recommendation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.lang.Long;

@Repository
public interface RecommendationRepo extends JpaRepository<Recommendation,Long> {
    Recommendation findByAssessmentId(Long assessmentId);

}
