package com.bookingsmart.controllers;

import com.bookingsmart.models.Service;

import com.bookingsmart.services.booking.ServiceService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/services")
public class ServiceController {

    private final ServiceService serviceService;

    public ServiceController(ServiceService serviceService) {
        this.serviceService = serviceService;
    }

    @PostMapping
    @PreAuthorize("hasRole('PROFESSIONAL')")
    public ResponseEntity<Service> createService(@RequestBody Service service) {
        return ResponseEntity.ok(serviceService.createService(service));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Service> getServiceById(@PathVariable Long id) {
        return ResponseEntity.ok(serviceService.getServiceById(id));
    }

    @GetMapping("/professional/{professionalId}")
    public ResponseEntity<List<Service>> getProfessionalServices(@PathVariable Long professionalId) {
        return ResponseEntity.ok(serviceService.getProfessionalServices(professionalId));
    }

    @GetMapping("/category/{categoryId}")
    public ResponseEntity<List<Service>> getServicesByCategory(@PathVariable Long categoryId) {
        return ResponseEntity.ok(serviceService.getServicesByCategory(categoryId));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('PROFESSIONAL')")
    public ResponseEntity<Service> updateService(@PathVariable Long id, @RequestBody Service serviceDetails) {
        return ResponseEntity.ok(serviceService.updateService(id, serviceDetails));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('PROFESSIONAL')")
    public ResponseEntity<Void> deleteService(@PathVariable Long id) {
        serviceService.deleteService(id);
        return ResponseEntity.ok().build();
    }
}