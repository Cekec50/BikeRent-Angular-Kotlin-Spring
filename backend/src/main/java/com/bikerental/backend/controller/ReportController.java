package com.bikerental.backend.controller;

import com.bikerental.backend.model.Report;
import com.bikerental.backend.model.Ride;
import com.bikerental.backend.repository.ReportRepository;
import com.bikerental.backend.repository.RideRepository;
import net.coobird.thumbnailator.Thumbnails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@RestController
@RequestMapping("/api/reports")
@CrossOrigin(origins = "http://localhost:4200")
public class ReportController {

    @Autowired
    private ReportRepository reportRepository;

    @Autowired
    private RideRepository rideRepository;

    private static final String UPLOAD_DIR = "uploads/reports/";

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

    @PostMapping
    public ResponseEntity<Void> createReport(
            @RequestParam("photo") MultipartFile photo,
            @RequestParam("rideId") Long rideId,
            @RequestParam("description") String description) throws IOException {

        Ride ride = rideRepository.findById(rideId)
                .orElseThrow(() -> new RuntimeException("Ride not found"));

        Path uploadPath = Paths.get(UPLOAD_DIR);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }
        String originalFilename = photo.getOriginalFilename();
        String uniqueFilename = System.currentTimeMillis() + "_" + originalFilename;
        Path filePath = uploadPath.resolve(uniqueFilename);
        
        // Save and rotate image if needed using Thumbnailator
        Thumbnails.of(photo.getInputStream())
                .scale(1.0)
                .toFile(filePath.toFile());

        String photoUrl = "http://localhost:8080/uploads/reports/" + uniqueFilename;

        Report report = new Report();
        report.setBikeId(ride.getBike().getId());
        report.setDescription(description);
        report.setPhotoUrl(photoUrl);
        
        reportRepository.save(report);

        return ResponseEntity.ok().build();
    }
}
