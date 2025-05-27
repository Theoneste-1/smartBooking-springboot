package com.bookingsmart.services.booking;

import com.bookingsmart.models.Availability;
import com.bookingsmart.repositories.AvailabilityRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class AvailabilityService {

    private final AvailabilityRepository availabilityRepository;

    public AvailabilityService(AvailabilityRepository availabilityRepository) {
        this.availabilityRepository = availabilityRepository;
    }

    @Transactional
    public Availability createAvailability(Availability availability) {
        return availabilityRepository.save(availability);
    }

    @Transactional
    public Availability updateAvailability(Long id, Availability availabilityDetails) {
        Availability availability = availabilityRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Availability not found"));

        availability.setStartTime(availabilityDetails.getStartTime());
        availability.setEndTime(availabilityDetails.getEndTime());
        availability.setAvailable(availabilityDetails.isAvailable());

        return availabilityRepository.save(availability);
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

    public List<Availability> getProfessionalAvailability(Long professionalId) {
        return availabilityRepository.findByProfessionalId(professionalId);
    }

    public List<Availability> getProfessionalAvailabilityByDay(Long professionalId, DayOfWeek dayOfWeek) {
        return availabilityRepository.findByProfessionalIdAndDayOfWeek(professionalId, dayOfWeek);
    }
}