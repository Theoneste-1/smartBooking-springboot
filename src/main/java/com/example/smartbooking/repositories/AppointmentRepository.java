package com.example.smartbooking.repositories;

import com.example.smartbooking.models.Appointment;
import com.example.smartbooking.models.Appointment.AppointmentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    List<Appointment> findByClientId(Long clientId);

    List<Appointment> findByProfessionalId(Long professionalId);

    List<Appointment> findByStatus(AppointmentStatus status);

    List<Appointment> findByStartTimeBetween(LocalDateTime start, LocalDateTime end);

    List<Appointment> findByProfessionalIdAndStartTimeBetween(Long professionalId, LocalDateTime start,
            LocalDateTime end);
}