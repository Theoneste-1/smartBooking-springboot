package com.bookingsmart.controllers;

import com.bookingsmart.models.Availability;
import com.bookingsmart.services.booking.AvailabilityService;
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
    @PreAuthorize("hasRole('PROFESSIONAL')")
    public ResponseEntity<Availability> createAvailability(@RequestBody Availability availability) {
        return ResponseEntity.ok(availabilityService.createAvailability(availability));
    }

    @GetMapping("/professional/{professionalId}")
    public ResponseEntity<List<Availability>> getProfessionalAvailability(@PathVariable Long professionalId) {
        return ResponseEntity.ok(availabilityService.getProfessionalAvailability(professionalId));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('PROFESSIONAL')")
    public ResponseEntity<Availability> updateAvailability(@PathVariable Long id,
            @RequestBody Availability availabilityDetails) {
        return ResponseEntity.ok(availabilityService.updateAvailability(id, availabilityDetails));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('PROFESSIONAL')")
    public ResponseEntity<Void> deleteAvailability(@PathVariable Long id) {
        availabilityService.deleteAvailability(id);
        return ResponseEntity.ok().build();
    }
}