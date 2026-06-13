package com.seminarhub.controller;

import com.seminarhub.entity.SeminarDTO;
import com.seminarhub.service.SeminarService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/seminar")
public class SeminarController {

    private final SeminarService seminarService;

    public SeminarController(SeminarService seminarService) {
        this.seminarService = seminarService;
    }

    // Create seminar
    @PostMapping
    public ResponseEntity<String> createSeminar(@RequestBody SeminarDTO seminar) {
        int result = seminarService.createSeminar(seminar);
        if (result > 0) {
            return ResponseEntity.ok("Seminar created successfully");
        }
        return ResponseEntity.status(500).body("Failed to create seminar");
    }

    // Get seminar by ID
    @GetMapping("/{seminar_no}")
    public ResponseEntity<SeminarDTO> getSeminarById(@PathVariable Long seminar_no) {
        SeminarDTO seminar = seminarService.getSeminarById(seminar_no);
        if (seminar != null) {
            return ResponseEntity.ok(seminar);
        }
        return ResponseEntity.status(404).body(null);
    }

    // Get seminars by company_no
    @GetMapping("/company/{company_no}")
    public ResponseEntity<List<SeminarDTO>> getSeminarsByCompanyNo(@PathVariable Long company_no) {
        List<SeminarDTO> seminars = seminarService.getSeminarsByCompanyNo(company_no);
        if (!seminars.isEmpty()) {
            return ResponseEntity.ok(seminars);
        }
        return ResponseEntity.status(404).body(null);
    }

    // Update seminar
    @PutMapping("/{seminar_no}")
    public ResponseEntity<String> updateSeminar(@PathVariable Long seminar_no, @RequestBody SeminarDTO seminar) {
        seminar.setSeminar_no(seminar_no);  // Ensure seminar_no is set
        int result = seminarService.updateSeminar(seminar);
        if (result > 0) {
            return ResponseEntity.ok("Seminar updated successfully");
        }
        return ResponseEntity.status(500).body("Failed to update seminar");
    }

    // Soft delete seminar
    @DeleteMapping("/{seminar_no}")
    public ResponseEntity<String> softDeleteSeminar(@PathVariable Long seminar_no) {
        int result = seminarService.softDeleteSeminar(seminar_no);
        if (result > 0) {
            return ResponseEntity.ok("Seminar deleted successfully");
        }
        return ResponseEntity.status(500).body("Failed to delete seminar");
    }
}