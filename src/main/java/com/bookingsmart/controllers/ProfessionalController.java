package com.bookingsmart.controllers;

import com.bookingsmart.models.Professional;
import com.bookingsmart.services.user.ProfessionalService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/professionals")
public class ProfessionalController {

    private final ProfessionalService professionalService;

    public ProfessionalController(ProfessionalService professionalService) {
        this.professionalService = professionalService;
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Professional> createProfessional(@RequestBody Professional professional) {
        return ResponseEntity.ok(professionalService.createProfessional(professional));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Professional> getProfessionalById(@PathVariable Long id) {
        return ResponseEntity.ok(professionalService.getProfessionalById(id).isPresent() ? professionalService.getProfessionalById(id).get() : null);
    }

    @GetMapping
    public ResponseEntity<List<Professional>> getAllProfessionals() {
        return ResponseEntity.ok(professionalService.getAllProfessionals());
    }

    @GetMapping("/verified")
    public ResponseEntity<List<Professional>> getVerifiedProfessionals() {
        return ResponseEntity.ok(professionalService.getVerifiedProfessionals());
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or #id == authentication.principal.id")
    public ResponseEntity<Professional> updateProfessional(@PathVariable Long id,
            @RequestBody Professional professionalDetails) {
        return ResponseEntity.ok(professionalService.updateProfessional(id, professionalDetails));
    }

    @PutMapping("/{id}/verify")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Professional> verifyProfessional(@PathVariable Long id) {
        professionalService.verifyProfessional(id);
        return ResponseEntity.ok().build();
    }
}