package com.bikerental.backend.controller;

import com.bikerental.backend.model.History;
import com.bikerental.backend.repository.HistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/history")
@CrossOrigin(origins = "http://localhost:4200")
public class HistoryController {

    @Autowired
    private HistoryRepository historyRepository;

    @GetMapping
    public ResponseEntity<List<History>> getAllHistory() {
        List<History> history = historyRepository.findAll();
        return ResponseEntity.ok(history);
    }

    @GetMapping("/{id}")
    public ResponseEntity<List<History>> getHistoryByUserId(@PathVariable Long id) {
        List<History> history = historyRepository.findByUserId(id);
        return ResponseEntity.ok(history);
    }
}
