package com.bikerental.backend.controller;

import com.bikerental.backend.model.Report;
import com.bikerental.backend.repository.ReportRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reports")
@CrossOrigin(origins = "http://localhost:4200")
public class ReportController {

    @Autowired
    private ReportRepository reportRepository;

    @GetMapping
    public ResponseEntity<List<Report>> getAllReports() {
        List<Report> reports = reportRepository.findAll();
        return ResponseEntity.ok(reports);
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteReportsByBikeId(@RequestParam Long bikeId) {
        reportRepository.deleteByBikeId(bikeId);
        return ResponseEntity.noContent().build();
    }
}
