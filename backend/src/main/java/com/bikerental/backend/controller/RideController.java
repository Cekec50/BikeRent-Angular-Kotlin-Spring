package com.bikerental.backend.controller;

import com.bikerental.backend.dto.RideRequest;
import com.bikerental.backend.model.Bike;
import com.bikerental.backend.model.History;
import com.bikerental.backend.model.Ride;
import com.bikerental.backend.model.User;
import com.bikerental.backend.repository.BikeRepository;
import com.bikerental.backend.repository.HistoryRepository;
import com.bikerental.backend.repository.RideRepository;
import com.bikerental.backend.repository.UserRepository;
import net.coobird.thumbnailator.Thumbnails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/rides")
@CrossOrigin(origins = "http://localhost:4200")
public class RideController {

    @Autowired
    private RideRepository rideRepository;

    @Autowired
    private BikeRepository bikeRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private HistoryRepository historyRepository;

    private static final String UPLOAD_DIR = "uploads/rentals/";

    @PostMapping("/start")
    public ResponseEntity<Void> startRide(@RequestBody RideRequest rideRequest) {
        if (rideRequest.getBike() == null || rideRequest.getBike().getId() == null) {
             throw new RuntimeException("Bike ID is required");
        }
        if (rideRequest.getUser() == null || rideRequest.getUser().getId() == null) {
             throw new RuntimeException("User ID is required");
        }

        Bike bike = bikeRepository.findById(rideRequest.getBike().getId())
                .orElseThrow(() -> new RuntimeException("Bike not found"));
        User user = userRepository.findById(rideRequest.getUser().getId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Ride ride = new Ride();
        ride.setBike(bike);
        ride.setUser(user);
        
        try {
            String dateStr = rideRequest.getStartTime().replace("Z", "");
            ride.setStartTime(LocalDateTime.parse(dateStr));
        } catch (Exception e) {
             throw new RuntimeException("Invalid date format: " + rideRequest.getStartTime());
        }

        rideRepository.save(ride);
        
        bike.setStatus(0); // Rented
        bikeRepository.save(bike);

        return ResponseEntity.ok().build();
    }

    @GetMapping("/active")
    public ResponseEntity<Ride> getActiveRide(@RequestParam Long userId) {
        return rideRepository.findByUserId(userId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/end")
    public ResponseEntity<Void> endRide(
            @RequestParam("photo") MultipartFile photo,
            @RequestParam("rideId") Long rideId,
            @RequestParam("endTime") String endTimeStr,
            @RequestParam("totalPrice") Double totalPrice,
            @RequestParam("duration") Long duration) throws IOException {

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

        String photoUrl = "http://localhost:8080/uploads/rentals/" + uniqueFilename;

        History history = new History();
        history.setUser(ride.getUser());
        history.setBike(ride.getBike());
        history.setStartTime(ride.getStartTime());
        history.setEndTime(LocalDateTime.parse(endTimeStr.replace("Z", "")));
        history.setTotalPrice(totalPrice);
        history.setDuration(duration);
        history.setPhotoUrl(photoUrl);
        historyRepository.save(history);

        Bike bike = ride.getBike();
        bike.setStatus(1); // Available
        bikeRepository.save(bike);

        rideRepository.delete(ride);

        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRide(@PathVariable Long id) {
        rideRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
