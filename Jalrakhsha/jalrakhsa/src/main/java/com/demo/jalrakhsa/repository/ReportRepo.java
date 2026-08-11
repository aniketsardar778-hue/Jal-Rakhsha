package com.demo.jalrakhsa.repository;

import com.demo.jalrakhsa.entity.Report;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReportRepo extends JpaRepository<Report,Long> {
}
