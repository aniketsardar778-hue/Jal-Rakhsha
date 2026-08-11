package com.demo.jalrakhsa.repository;

import com.demo.jalrakhsa.entity.Assessment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AssessmentRepo extends JpaRepository<Assessment,Long>
{

    List<Assessment>
    findByUserUsernameOrderByCreatedAtDesc(
            String username);
}
