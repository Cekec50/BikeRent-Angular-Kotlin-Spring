package com.bikerental.backend.controller;

import com.bikerental.backend.model.Parking;
import com.bikerental.backend.repository.ParkingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/parkings")
@CrossOrigin(origins = "http://localhost:4200")
public class ParkingController {

    @Autowired
    private ParkingRepository parkingRepository;

    @GetMapping
    public ResponseEntity<List<Parking>> getAllParkings() {
        List<Parking> parkings = parkingRepository.findAll();
        return ResponseEntity.ok(parkings);
    }
}
