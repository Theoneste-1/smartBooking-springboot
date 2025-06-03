package com.example.smartbooking.services.booking;

import com.example.smartbooking.models.Appointment;
import com.example.smartbooking.models.Appointment.AppointmentStatus;
import com.example.smartbooking.repositories.AppointmentRepository;
import com.example.smartbooking.services.notification.NotificationService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final BookingValidationService bookingValidationService;
    private final NotificationService notificationService;

    public AppointmentService(AppointmentRepository appointmentRepository,
            BookingValidationService bookingValidationService,
            NotificationService notificationService) {
        this.appointmentRepository = appointmentRepository;
        this.bookingValidationService = bookingValidationService;
        this.notificationService = notificationService;
    }

    @Transactional
    public Appointment createAppointment(Appointment appointment) {
        bookingValidationService.validateBooking(appointment);
        Appointment savedAppointment = appointmentRepository.save(appointment);
        notificationService.sendAppointmentConfirmation(savedAppointment);
        return savedAppointment;
    }

    @Transactional
    public Appointment updateAppointment(Long id, Appointment appointmentDetails) {
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Appointment not found"));

        appointment.setStartTime(appointmentDetails.getStartTime());
        appointment.setEndTime(appointmentDetails.getEndTime());
        appointment.setNotes(appointmentDetails.getNotes());

        return appointmentRepository.save(appointment);
    }

    @Transactional
    public Appointment updateStatus(Long id, AppointmentStatus status) {
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Appointment not found"));

        appointment.setStatus(status);
        Appointment updatedAppointment = appointmentRepository.save(appointment);

        notificationService.sendAppointmentStatusUpdate(updatedAppointment);
        return updatedAppointment;
    }

    public List<Appointment> getClientAppointments(Long clientId) {
        return appointmentRepository.findByClientId(clientId);
    }

    public List<Appointment> getProfessionalAppointments(Long professionalId) {
        return appointmentRepository.findByProfessionalId(professionalId);
    }

    public List<Appointment> getAppointmentsByDateRange(LocalDateTime start, LocalDateTime end) {
        return appointmentRepository.findByStartTimeBetween(start, end);
    }
}