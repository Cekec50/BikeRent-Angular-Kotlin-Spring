package com.bikerental.backend.repository;

import com.bikerental.backend.model.Report;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface ReportRepository extends JpaRepository<Report, Long> {
    @Transactional
    void deleteByBikeId(Long bikeId);
}
