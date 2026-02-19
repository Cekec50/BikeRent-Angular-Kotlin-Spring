package com.bikerental.backend.controller;

import com.bikerental.backend.model.Bike;
import com.bikerental.backend.model.Parking;
import com.bikerental.backend.repository.BikeRepository;
import com.bikerental.backend.repository.ParkingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bikes")
@CrossOrigin(origins = "http://localhost:4200")
public class BikeController {

    @Autowired
    private BikeRepository bikeRepository;

    @Autowired
    private ParkingRepository parkingRepository;

    @GetMapping("/accessible")
    public ResponseEntity<List<Bike>> getAllBikesAccessible() {
        List<Bike> bikes = bikeRepository.findByStatusNot(-1);
        return ResponseEntity.ok(bikes);
    }

    @GetMapping
    public ResponseEntity<List<Bike>> getAllBikes() {
        List<Bike> bikes = bikeRepository.findAll();
        return ResponseEntity.ok(bikes);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Bike> getBikeById(@PathVariable Long id) {
        return bikeRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/accessible/{id}")
    public ResponseEntity<Bike> getBikeByIdAccessible(@PathVariable Long id) {
        return bikeRepository.findByIdAndStatusNot(id, -1)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Bike> createBike(@RequestBody Bike bike) {
        updateNearestParking(bike);
        Bike savedBike = bikeRepository.save(bike);
        return ResponseEntity.ok(savedBike);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Bike> updateBike(@PathVariable Long id, @RequestBody Bike bikeDetails) {
        return bikeRepository.findById(id)
                .map(bike -> {
                    bike.setType(bikeDetails.getType());
                    bike.setPrice(bikeDetails.getPrice());
                    bike.setStatus(bikeDetails.getStatus());
                    bike.setLocation(bikeDetails.getLocation());
                    bike.setLatitude(bikeDetails.getLatitude());
                    bike.setLongitude(bikeDetails.getLongitude());
                    
                    updateNearestParking(bike);
                    
                    Bike updatedBike = bikeRepository.save(bike);
                    return ResponseEntity.ok(updatedBike);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    private void updateNearestParking(Bike bike) {
        if (bike.getLatitude() == null || bike.getLongitude() == null) {
            return;
        }

        List<Parking> parkings = parkingRepository.findAll();
        Parking nearest = null;
        int minDistance = Integer.MAX_VALUE;

        for (Parking parking : parkings) {
            int distance = (int) calculateDistance(
                    bike.getLatitude(), bike.getLongitude(),
                    parking.getLatitude(), parking.getLongitude()
            );
            if (distance < minDistance) {
                minDistance = distance;
                nearest = parking;
            }
        }

        bike.setNearestParking(nearest);
        if (nearest != null) {
            bike.setDistanceToNearestParking(minDistance);
        }
    }

    // Haversine formula to calculate distance in meters
    private double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        final int R = 6371; // Radius of the earth in km
        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return R * c * 1000; // convert to meters
    }
}
