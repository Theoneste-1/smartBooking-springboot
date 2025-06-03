package com.example.smartbooking.controllers;

import com.example.smartbooking.dto.request.AvailabilityCreateRequestDto;
import com.example.smartbooking.dto.response.AvailabilityResponse;
import com.example.smartbooking.models.Availability;
import com.example.smartbooking.services.booking.AvailabilityService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/availability")
public class AvailabilityController {

    private final AvailabilityService availabilityService;

    public AvailabilityController(AvailabilityService availabilityService) {
        this.availabilityService = availabilityService;
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<AvailabilityResponse> createAvailability(@RequestBody AvailabilityCreateRequestDto availability) {
        return ResponseEntity.ok(availabilityService.createAvailability(availability));
    }

    @GetMapping("/professional/{professionalId}")
    public ResponseEntity<List<AvailabilityResponse>> getProfessionalAvailability(@PathVariable Long professionalId) {
        return ResponseEntity.ok(availabilityService.getProfessionalAvailability(professionalId));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<AvailabilityResponse> updateAvailability(@PathVariable Long id,
                                                                   @RequestBody Availability availabilityDetails) {
        return ResponseEntity.ok(availabilityService.updateAvailability(id, availabilityDetails));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteAvailability(@PathVariable Long id) {
        availabilityService.deleteAvailability(id);
        return ResponseEntity.ok().build();
    }
}