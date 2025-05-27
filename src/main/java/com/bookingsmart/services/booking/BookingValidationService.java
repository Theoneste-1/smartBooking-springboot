package com.bookingsmart.services.booking;

import com.bookingsmart.models.Appointment;
import com.bookingsmart.repositories.AppointmentRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class BookingValidationService {

    private final AppointmentRepository appointmentRepository;

    public BookingValidationService(AppointmentRepository appointmentRepository) {
        this.appointmentRepository = appointmentRepository;
    }

    public void validateBooking(Appointment appointment) {
        validateTimeSlot(appointment);
        validateProfessionalAvailability(appointment);
        validateClientEligibility(appointment);
    }

    private void validateTimeSlot(Appointment appointment) {
        LocalDateTime start = appointment.getStartTime();
        LocalDateTime end = appointment.getEndTime();

        if (start.isAfter(end)) {
            throw new RuntimeException("Start time must be before end time");
        }

        if (start.isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Cannot book appointments in the past");
        }

        List<Appointment> conflictingAppointments = appointmentRepository
                .findByProfessionalIdAndStartTimeBetween(
                        appointment.getProfessional().getId(),
                        start,
                        end);

        if (!conflictingAppointments.isEmpty()) {
            throw new RuntimeException("Time slot is already booked");
        }
    }

    private void validateProfessionalAvailability(Appointment appointment) {
        // Check if the professional is available during the requested time slot
        // Implementation depends on your availability model
    }

    private void validateClientEligibility(Appointment appointment) {
        // Check if the client is eligible to book (e.g., no outstanding payments)
        // Implementation depends on your business rules
    }
}