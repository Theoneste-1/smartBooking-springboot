package com.example.smartbooking.controllers;

import com.example.smartbooking.dto.response.AvailabilityResponse;
import com.example.smartbooking.dto.response.CategoryResponseDto;
import com.example.smartbooking.dto.response.ProfessionalDTO;
import com.example.smartbooking.models.Appointment;
import com.example.smartbooking.models.Availability;
import com.example.smartbooking.models.Category;
import com.example.smartbooking.models.Professional;
import com.example.smartbooking.services.user.ProfessionalService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;
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

    @GetMapping
    public ResponseEntity<List<ProfessionalDTO>> getAllProfessionals() {
        return ResponseEntity.ok(professionalService.getAllProfessionalDTOs());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProfessionalDTO> getProfessionalById(@PathVariable Long id) {
        return ResponseEntity.ok(professionalService.getProfessionalDTOById(id));
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



    @GetMapping("/profile")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Professional> getCurrentProfessionalProfile() {
       return ResponseEntity.ok(professionalService.getProfessionalByUserId());
    }



    @GetMapping("/{id}/availabilities")
    public ResponseEntity<Set<AvailabilityResponse>> getProfessionalAvailabilities(@PathVariable Long id) {
        return ResponseEntity.ok(professionalService.getAvailabilitiesByProfessionalId(id));
    }

    @GetMapping("/{id}/appointments")
    public ResponseEntity<Set<Appointment>> getProfessionalAppointments(@PathVariable Long id) {
        return ResponseEntity.ok(professionalService.getAppointmentsByProfessionalId(id));
    }

    @GetMapping("/{id}/categories")
    public ResponseEntity<Set<CategoryResponseDto>> getProfessionalCategories(@PathVariable Long id) {
        return ResponseEntity.ok(professionalService.getCategoriesByProfessionalId(id));
    }
}