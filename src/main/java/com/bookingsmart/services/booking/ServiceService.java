package com.bookingsmart.services.booking;

import com.bookingsmart.models.Service;
import com.bookingsmart.repositories.ServiceRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@org.springframework.stereotype.Service
public class ServiceService {

    private final ServiceRepository serviceRepository;

    public ServiceService(ServiceRepository serviceRepository) {
        this.serviceRepository = serviceRepository;
    }

    @Transactional
    public Service createService(Service service) {
        // Validate service data
        if (service.getName() == null || service.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Service name cannot be empty");
        }
        if (service.getDurationMinutes() == null || service.getDurationMinutes() <= 0) {
            throw new IllegalArgumentException("Service duration must be greater than 0");
        }
        if (service.getPrice() == null || service.getPrice().compareTo(java.math.BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Service price must be greater than 0");
        }
        if (service.getProfessional() == null) {
            throw new IllegalArgumentException("Service must be associated with a professional");
        }

        return serviceRepository.save(service);
    }

    public Service getServiceById(Long id) {
        return serviceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Service not found"));
    }

    public List<Service> getProfessionalServices(Long professionalId) {
        return serviceRepository.findByProfessionalId(professionalId);
    }

    public List<Service> getServicesByCategory(Long categoryId) {
        return serviceRepository.findByCategoryId(categoryId);
    }

    @Transactional
    public Service updateService(Long id, Service serviceDetails) {
        Service service = serviceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Service not found"));

        // Update service details
        service.setName(serviceDetails.getName());
        service.setDescription(serviceDetails.getDescription());
        service.setDurationMinutes(serviceDetails.getDurationMinutes());
        service.setPrice(serviceDetails.getPrice());
        service.setCategory(serviceDetails.getCategory());
        service.setActive(serviceDetails.isActive());

        return serviceRepository.save(service);
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

    public List<Service> getActiveServices() {
        return serviceRepository.findByIsActive(true);
    }
}