package com.example.smartbooking.services.booking;

import com.example.smartbooking.dto.request.ServiceCreateDto;
import com.example.smartbooking.dto.response.ServiceResponseDto;
import com.example.smartbooking.models.Category;
import com.example.smartbooking.models.Service;
import com.example.smartbooking.repositories.ServiceRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@org.springframework.stereotype.Service
public class ServiceService {

    private final ServiceRepository serviceRepository;
    private final CategoryService categoryService;

    public ServiceService(ServiceRepository serviceRepository, CategoryService categoryService) {
        this.serviceRepository = serviceRepository;
        this.categoryService = categoryService;
    }

    @Transactional
    public ServiceResponseDto createService(ServiceCreateDto service) {
        if (service.getDurationMinutes() == null || service.getDurationMinutes() <= 0) {
            throw new IllegalArgumentException("Service duration must be greater than 0");
        }
        if (service.getPrice() == null || service.getPrice().compareTo(java.math.BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Service price must be greater than 0");
        }

        Service serv = new Service();
        serv.setName(service.getName());
        serv.setDurationMinutes(service.getDurationMinutes());
        serv.setPrice(service.getPrice());
        serv.setDescription(service.getDescription());

        Category cat = categoryService.getSingleCategory(service.getCategoryId());
        serv.setCategory(cat);

        return toResponse(serviceRepository.save(serv));
    }

    public ServiceResponseDto getServiceById(Long id) {
        return serviceRepository.findById(id)
                .map(this::toResponse)
                .orElseThrow(() -> new RuntimeException("Service not found"));
    }

    public List<ServiceResponseDto> getServicesByCategory(Long categoryId) {
        return serviceRepository.findByCategoryId(categoryId).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }


    public List<ServiceResponseDto> getActiveServices() {
        return serviceRepository.findByIsActive(true).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public ServiceResponseDto updateService(Long id, Service serviceDetails) {
        Service service = serviceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Service not found"));

        service.setName(serviceDetails.getName());
        service.setDescription(serviceDetails.getDescription());
        service.setDurationMinutes(serviceDetails.getDurationMinutes());
        service.setPrice(serviceDetails.getPrice());
        service.setCategory(serviceDetails.getCategory());
        service.setActive(serviceDetails.isActive());

        return toResponse(serviceRepository.save(service));
    }

    @Transactional
    public void deleteService(Long id) {
        Service service = serviceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Service not found"));

        // Check if there are any upcoming appointments
        if (!service.getAppointments().isEmpty()) {
            // Instead of deleting, mark as inactive
            service.setActive(false);
            serviceRepository.save(service);
        } else {
            serviceRepository.delete(service);
        }
    }

    private ServiceResponseDto toResponse(Service service) {
        return new ServiceResponseDto(
                service.getId(),
                service.getName(),
                service.getDescription(),
                service.getDurationMinutes(),
                service.getPrice(),
                service.isActive(),
                service.getCategory() != null ? service.getCategory().getId() : null,
                service.getCategory() != null ? service.getCategory().getName() : null
        );
    }
}