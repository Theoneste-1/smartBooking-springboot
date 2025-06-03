package com.example.smartbooking.controllers;

import com.example.smartbooking.dto.request.ServiceCreateDto;
import com.example.smartbooking.dto.response.ServiceResponseDto;
import com.example.smartbooking.models.Service;

import com.example.smartbooking.services.booking.ServiceService;
import jakarta.validation.Valid;
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
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ServiceResponseDto> createService(@Valid @RequestBody ServiceCreateDto service) {
        return ResponseEntity.ok(serviceService.createService(service));
    }

    @GetMapping("/active")
    public ResponseEntity<List<ServiceResponseDto>> getActiveServices() {
        return ResponseEntity.ok(serviceService.getActiveServices());
    }


    @GetMapping("/{id}")
    public ResponseEntity<ServiceResponseDto> getServiceById(@PathVariable Long id) {
        return ResponseEntity.ok(serviceService.getServiceById(id));
    }

    @GetMapping("/category/{categoryId}")
    public ResponseEntity<List<ServiceResponseDto>> getServicesByCategory(@PathVariable Long categoryId) {
        return ResponseEntity.ok(serviceService.getServicesByCategory(categoryId));
    }


    @PutMapping("/{id:\\d+}")
    @PreAuthorize("hasRole('PROFESSIONAL')")
    public ResponseEntity<ServiceResponseDto> updateService(@PathVariable Long id, @RequestBody Service serviceDetails) {
        return ResponseEntity.ok(serviceService.updateService(id, serviceDetails));
    }



    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('PROFESSIONAL')")
    public ResponseEntity<Void> deleteService(@PathVariable Long id) {
        serviceService.deleteService(id);
        return ResponseEntity.ok().build();
    }
}