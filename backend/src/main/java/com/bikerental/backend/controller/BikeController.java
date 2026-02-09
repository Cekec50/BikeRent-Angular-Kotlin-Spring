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

    @GetMapping
    public ResponseEntity<List<Bike>> getAllBikes() {
        List<Bike> bikes = bikeRepository.findAll();
        return ResponseEntity.ok(bikes);
    }
}
