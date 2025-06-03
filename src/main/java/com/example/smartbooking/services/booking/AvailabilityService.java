package com.example.smartbooking.services.booking;

import com.example.smartbooking.dto.request.AvailabilityCreateRequestDto;
import com.example.smartbooking.dto.response.AvailabilityResponse;
import com.example.smartbooking.models.Availability;
import com.example.smartbooking.models.Professional;
import com.example.smartbooking.repositories.AvailabilityRepository;
import com.example.smartbooking.services.user.ProfessionalService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class AvailabilityService {

    private final AvailabilityRepository availabilityRepository;
    private final ProfessionalService professionalService;

    public AvailabilityService(AvailabilityRepository availabilityRepository, ProfessionalService professionalService) {
        this.availabilityRepository = availabilityRepository;
        this.professionalService = professionalService;
    }

    @Transactional
    public AvailabilityResponse createAvailability(AvailabilityCreateRequestDto availability) {
        Availability av = new Availability();
        Professional prof = this.professionalService.getSingleProfessional(availability.getProfessionalId());
        if (prof == null) {
            throw new RuntimeException("Professional not found with ID: " + availability.getProfessionalId());
        }
        av.setAvailable(availability.isAvailable());
        av.setDayOfWeek(availability.getDayOfWeek());
        av.setProfessional(prof);
        av.setEndTime(availability.getEndTime());
        av.setStartTime(availability.getStartTime());

        Availability saved = availabilityRepository.save(av);

        return toResponse(saved);
    }


    @Transactional
    public AvailabilityResponse updateAvailability(Long id, Availability availabilityDetails) {
        Availability availability = availabilityRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Availability not found"));

        availability.setStartTime(availabilityDetails.getStartTime());
        availability.setEndTime(availabilityDetails.getEndTime());
        availability.setAvailable(availabilityDetails.isAvailable());

        return toResponse(availabilityRepository.save(availability));
    }

    @Transactional
    public void deleteAvailability(Long id) {
        Availability availability = availabilityRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Availability not found"));

        // Check if there are any upcoming appointments
        if (availability.getProfessional().getAppointments().stream()
                .anyMatch(appointment -> appointment.getStartTime().isAfter(LocalDateTime.now()))) {
            throw new RuntimeException("Cannot delete availability with upcoming appointments");
        }

        availabilityRepository.delete(availability);
    }

    public List<AvailabilityResponse> getProfessionalAvailability(Long professionalId) {
        return availabilityRepository.findByProfessionalId(professionalId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public List<Availability> getProfessionalAvailabilityByDay(Long professionalId, DayOfWeek dayOfWeek) {
        return availabilityRepository.findByProfessionalIdAndDayOfWeek(professionalId, dayOfWeek);
    }


    public AvailabilityResponse toResponse(Availability availability) {
        return new AvailabilityResponse(
                availability.getId(),
                availability.getProfessional().getId(),
                availability.getDayOfWeek(),
                availability.getStartTime(),
                availability.getEndTime(),
                availability.isAvailable()
        );
    }

}