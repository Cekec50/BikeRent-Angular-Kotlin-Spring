package com.bikerental.backend.controller;

import com.bikerental.backend.dto.RideRequest;
import com.bikerental.backend.model.Bike;
import com.bikerental.backend.model.Ride;
import com.bikerental.backend.model.User;
import com.bikerental.backend.repository.BikeRepository;
import com.bikerental.backend.repository.RideRepository;
import com.bikerental.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping("/start")
    public ResponseEntity<Void> startRide(@RequestBody RideRequest rideRequest) {
        // The frontend sends full User and Bike objects, but we should rely on IDs to fetch fresh data from DB
        // or use the provided objects if we trust them. Usually better to fetch by ID.
        // However, the RideRequest now has User and Bike objects, not just IDs.
        
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
        
        // Update bike status to rented (0)
        bike.setStatus(0);
        bikeRepository.save(bike);

        return ResponseEntity.ok().build();
    }

    @GetMapping("/active")
    public ResponseEntity<Ride> getActiveRide(@RequestParam Long userId) {
        return rideRepository.findByUserId(userId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
