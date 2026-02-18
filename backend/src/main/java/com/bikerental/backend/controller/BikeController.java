package com.bikerental.backend.controller;

import com.bikerental.backend.model.Bike;
import com.bikerental.backend.repository.BikeRepository;
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
                    Bike updatedBike = bikeRepository.save(bike);
                    return ResponseEntity.ok(updatedBike);
                })
                .orElse(ResponseEntity.notFound().build());
    }
}
